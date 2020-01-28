package com.richardkuiper.cleanarchitecture.stackoverflowusers.data

import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model.GetUsersResponse
import io.reactivex.Single
import retrofit2.http.GET

interface UserApi {

    @GET("2.2/users?pagesize=100&order=desc&sort=reputation&site=stackoverflow")
    fun getTopUsers(): Single<GetUsersResponse>
}