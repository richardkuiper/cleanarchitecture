package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.di

import androidx.lifecycle.ViewModel
import com.richardkuiper.cleanarchitecture.common.viewmodel.ViewModelFactoryModule
import com.richardkuiper.cleanarchitecture.common.viewmodel.ViewModelKey
import com.richardkuiper.cleanarchitecture.stackoverflowusers.di.UserModule
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.TopUserListFragment
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.TopUserListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap

@Module(includes = [AndroidSupportInjectionModule::class, UserModule::class, ViewModelFactoryModule::class])
abstract class TopUserListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(TopUserListViewModel::class)
    abstract fun bindsTopUserListViewModel(vieWModel: TopUserListViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun providesTopUserListFragment(): TopUserListFragment
}