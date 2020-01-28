package com.richardkuiper.cleanarchitecture.stackoverflowusers.di

import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.TopUsersActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
abstract class TopUsersActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesTopUsersActivity(): TopUsersActivity
}