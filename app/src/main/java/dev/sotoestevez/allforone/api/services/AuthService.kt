package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.requests.RefreshRequest
import dev.sotoestevez.allforone.api.responses.BaseErrorResponse
import dev.sotoestevez.allforone.api.responses.RefreshResponse
import dev.sotoestevez.allforone.api.responses.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/** Service to handle the operations related to the /auth endpoints of the API */
interface AuthService {

	/**
	 * Sends the user credentials to the server to handle the log in request
	 *
	 * @param token Google Id Token to perform the authentication
	 * @return      Response with the authentication data (session and user) or with an error
	 */
	@GET("/auth/signin/{token}")
	suspend fun signIn(@Path("token") token: String): NetworkResponse<SignInResponse, BaseErrorResponse>

	/**
	 * Sends the current session data to the server to renew the tokens
	 *
	 * @param session   Current session tokens to renew (auth and refresh token)
	 * @return          Response with the new session data
	 */
	@POST("/auth/refresh")
	suspend fun refresh(@Body session: RefreshRequest): NetworkResponse<RefreshResponse, BaseErrorResponse>

}