package com.richardkuiper.cleanarchitecture.di

import android.content.Context
import com.richardkuiper.cleanarchitecture.App
import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppModule {

    @Singleton
    @Binds
    @ApplicationContext
    abstract fun provideContext(app: App): Context
}