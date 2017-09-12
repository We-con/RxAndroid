package com.example.lf_wannabe.rxandroid

import com.example.lf_wannabe.rxandroid.model.Post
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Created by lf_wannabe on 05/09/2017.
 */
interface CustomService {
    companion object {
        var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://crud-mimmim.c9users.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @GET("/index")
    fun getPosts(): Observable<List<Post>>

    @POST("/write")
    fun writePost(@Body post: Post): Observable<List<Post>>

    @POST("/delete")
    fun deletePost(@Body post:Post): Observable<List<Post>>
}