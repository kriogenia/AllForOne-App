package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.BaseErrorResponse
import dev.sotoestevez.allforone.api.schemas.TaskListResponse
import dev.sotoestevez.allforone.api.schemas.TaskRequest
import dev.sotoestevez.allforone.api.schemas.TaskResponse
import retrofit2.http.*

/** Service to handle the operations related to the /tasks endpoints of the API */
interface TaskService {

    /**
     * Retrieves the list of relevant tasks of the user
     *
     * @param token Authorization token to perform the request
     * @return      Response with the list of tasks
     */
    @GET("/tasks")
    suspend fun get(
        @Header("Authorization") token: String
    ): NetworkResponse<TaskListResponse, BaseErrorResponse>

    /**
     * Sends a new task to create to the API
     *
     * @param token Authorization token to perform the request
     * @param task  Page of messages to retrieve
     * @return      Response with the list of messages of the batch
     */
    @POST("/tasks")
    suspend fun new(
        @Header("Authorization") token: String,
        @Body task: TaskRequest
    ): NetworkResponse<TaskResponse, BaseErrorResponse>

}