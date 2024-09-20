package com.example.todolistapi

import retrofit2.Call
import retrofit2.http.*

interface TodoApiService {
    @GET("todos")
    fun getTodos(): Call<List<Todo>>

    @POST("todos")
    fun createTodo(@Body todo: Todo): Call<Todo>

    @PUT("todos/{id}")
    fun updateTodo(@Path("id") id: String, @Body todo: Todo): Call<Todo>

    @DELETE("todos/{id}")
    fun deleteTodo(@Path("id") id: String): Call<Void>
}
