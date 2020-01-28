package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.di

import com.richardkuiper.cleanarchitecture.common.viewmodel.ViewModelFactoryModule
import com.richardkuiper.cleanarchitecture.stackoverflowusers.di.UserModule
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.TopUserDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(
        includes = [
            AndroidSupportInjectionModule::class,
            ViewModelFactoryModule::class,
            UserModule::class
        ]
)
abstract class TopUserDetailFragmentModule {

    @ContributesAndroidInjector
    abstract fun providesTopUserDetailFragment(): TopUserDetailFragment
}