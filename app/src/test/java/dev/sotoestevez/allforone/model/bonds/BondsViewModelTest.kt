package dev.sotoestevez.allforone.model.bonds

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.model.launch.LaunchViewModel
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.*
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BondsViewModelTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	@get:Rule
	var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

	// Mocks
	private lateinit var mockUserRepository: UserRepository

	// Test object
	private lateinit var model: BondsViewModel

	@Before
	fun beforeEach() {
		// Mocks
		mockUserRepository = mockk()
		mockkConstructor(SessionManager::class)
		every { anyConstructed<SessionManager>().getAuthToken() } returns "token"
		// Init test object
		model = BondsViewModel(mockk(relaxed = true), coroutineRule.testDispatcherProvider, mockk(), mockUserRepository)
		model.injectUser(User("id", "google", User.Role.BLANK))
	}

	@Test
	fun `should set a new qrCode only if the request is new`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		val code = "qrCode"
		coEvery { mockUserRepository.requestBondingCode(any()) } returns code

		model.generateNewQRCode()   // Generates new code

		coEvery { mockUserRepository.requestBondingCode(any()) } returns "different"

		assertEquals(true, model.loadingQr.value)
		assertEquals(code, model.qrCode.value)

		model.generateNewQRCode()   // Doesn't generate a new code, the previous is still valid

		assertEquals(code, model.qrCode.value)
		coVerify(exactly = 1) { mockUserRepository.requestBondingCode(any()) }
	}

}