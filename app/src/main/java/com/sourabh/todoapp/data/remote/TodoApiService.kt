package com.sourabh.todoapp.data.remote

import retrofit2.http.GET


data class RemoteTodo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

interface ApiService {

    @GET("todos")
    suspend fun getTodos(): List<RemoteTodo>
}