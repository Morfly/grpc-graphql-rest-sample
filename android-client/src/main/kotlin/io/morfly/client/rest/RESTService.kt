package io.morfly.client.rest

import io.morfly.client.LOCALHOST
import io.morfly.client.domain.*
import retrofit2.http.*


interface RESTService {

    @GET("user")
    suspend fun listUsers(): List<User>

    @GET("user/{id}")
    suspend fun getUser(@Path("id") userId: String): User?

    @POST("user")
    suspend fun createUser(@Body user: NewUser): User?

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Boolean


    @GET("post")
    suspend fun listPosts(@Query("includeComments") includeComments: Boolean): List<Post>

    @GET("post/{id}")
    suspend fun getPost(
        @Path("id") postId: String,
        @Query("includeComments") includeComments: Boolean
    ): Post?

    @POST("post")
    suspend fun createPost(@Body post: NewPost): Post?

    @DELETE("post/{id}")
    suspend fun deletePost(postId: String): Boolean


    @GET("post/{id}/comment")
    suspend fun listComments(@Path("id") postId: String): List<Comment>

    @GET("post/comment/{id}")
    suspend fun getComment(@Path("id") commentId: String): Comment?

    @POST("post/comment")
    suspend fun createComment(@Body comment: NewComment): Comment?

    @DELETE("post/comment/{id}")
    suspend fun deleteComment(@Path("id") commentId: String): Boolean


    companion object {
        const val BASE_URL = "http://$LOCALHOST:8080/rest/"
    }
}