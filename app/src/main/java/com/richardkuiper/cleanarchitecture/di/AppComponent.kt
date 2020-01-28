package com.richardkuiper.cleanarchitecture.di

import com.richardkuiper.cleanarchitecture.App
import com.richardkuiper.cleanarchitecture.stackoverflowusers.di.TopUsersActivityModule
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.di.TopUserDetailFragmentModule
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.di.TopUserListFragmentModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            AndroidInjectionModule::class,
            AndroidSupportInjectionModule::class,
            TopUsersActivityModule::class,
            TopUserListFragmentModule::class,
            TopUserDetailFragmentModule::class
        ]
)

interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(app: App): Builder
    }
}