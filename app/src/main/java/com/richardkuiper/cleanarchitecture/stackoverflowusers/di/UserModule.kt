package com.richardkuiper.cleanarchitecture.stackoverflowusers.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.richardkuiper.cleanarchitecture.di.AppModule
import com.richardkuiper.cleanarchitecture.di.ApplicationContext
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.IUserLocalDataSource
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.UserApi
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.UserLocalDataSource
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.UserRemoteDataSource
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.UserRepository
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.mapper.UserToUserEntityMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.IUserRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val DEFAULT_SHARED_PREFERENCES = "default_shared_preferences"

@Module(includes = [AppModule::class])
class UserModule {

    @Provides
    fun providesUserRepository(
            userLocalDataSource: IUserLocalDataSource,
            userRemoteDataSource: UserRemoteDataSource,
            userToUserEntityMapper: UserToUserEntityMapper
    ): IUserRepository {
        return UserRepository(
                userLocalDataSource,
                userRemoteDataSource,
                userToUserEntityMapper
        )
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl("https://api.stackexchange.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

    @Singleton
    @Provides
    fun providesUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    fun providesSharedPreferences(
            @ApplicationContext applicationContext: Context
    ): SharedPreferences = applicationContext.getSharedPreferences(
            DEFAULT_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
    )

    @Provides
    fun providesUserLocalDataSource(
            sharedPreferences: SharedPreferences,
            gson: Gson
    ): IUserLocalDataSource = UserLocalDataSource(
            sharedPreferences,
            gson
    )

    @Singleton
    @Provides
    fun providesGson(): Gson = GsonBuilder().create()
}