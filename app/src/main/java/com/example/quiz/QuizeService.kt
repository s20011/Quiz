package com.example.quiz

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface QuizeService {
    @GET("exec?f=data")
    fun getQuizProperties():
            Call<List<Quize>>
}

fun createService(): QuizeService{
    val URL = """https://script.google.com/macros/s/
        |AKfycbznWpk2m8q6lbLWSS6qaz3uS6j3L4zPwv7CqDEiC
        |433YOgAdaFekGJmjoAO60quMg6l/""".trimMargin()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(URL)
        .build()

    return retrofit.create(QuizeService::class.java)
}