package dev.sotoestevez.allforone.api.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dev.sotoestevez.allforone.api.data.BaseErrorResponse
import dev.sotoestevez.allforone.api.data.ErrorResponse
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import dev.sotoestevez.allforone.util.rules.WebServerRule
import dev.sotoestevez.allforone.util.webserver.AuthDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class AuthServiceTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	@get:Rule
	var webServerRule: WebServerRule = WebServerRule()

	// Object to test
	private val api: AuthService = webServerRule.api.create(AuthService::class.java)

	@Before
	fun beforeEach() {
		webServerRule.mock.dispatcher = AuthDispatcher
	}

	@Test
	fun `should parse SignInResponse with valid token`(): Unit = runBlocking {
		val expected = SignInResponse(
			"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXNzaW9uSWQiOiI2MTE5OGZmMjQwY2VjMzA2N2E2NmMwYjEiLCJpYXQiOjE2MjkwNjUyMDIsImV4cCI6MTYyOTA2ODgwMn0.g-5EN1u69zj0c3mVVI4zKiQfuy-OIKqa3pIqWiHXlqk",
			"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXNzaW9uSWQiOiI2MTE5OGZmMjQwY2VjMzA2N2E2NmMwYjEiLCJpYXQiOjE2MjkwNjUyMDIsImV4cCI6MTYyOTE1MTYwMn0.u052QG8_ktGJC077CHbH8cRrNRDf4m1K-RM4O9QbXrU",
			1629068802,
			User(
				"61198ff240cec3067a66c0b1",
				"valid",
				User.Role.BLANK,
				null
			)
		)
		val actual = api.signIn("valid")
		assertTrue(actual is NetworkResponse.Success)
		assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should parse ErrorResponse with no token provided`(): Unit = runBlocking {
		val expected = BaseErrorResponse("The specified user does not have a valid ID")
		val actual = api.signIn("")
		assertTrue(actual is NetworkResponse.ServerError)
		assertEquals(expected, (actual as NetworkResponse.ServerError).body)
	}

	@Test
	fun `should parse ErrorResponse with invalid token`(): Unit = runBlocking {
		val expected = BaseErrorResponse("The specified user does not have a valid ID")
		val actual = api.signIn("invalid")
		assertTrue(actual is NetworkResponse.ServerError)
		assertEquals(expected, (actual as NetworkResponse.ServerError).body)
	}

}