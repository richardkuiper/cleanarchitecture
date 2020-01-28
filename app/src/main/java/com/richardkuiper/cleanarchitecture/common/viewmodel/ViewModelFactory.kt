package com.richardkuiper.cleanarchitecture.common.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Reusable
import javax.inject.Inject
import javax.inject.Provider

@Reusable
class ViewModelFactory @Inject constructor(
        private val viewModelProviderMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("MapGetWithNotNullAssertionOperator")
        val viewModelProvider = viewModelProviderMap[modelClass]!!

        @Suppress("UNCHECKED_CAST")
        return viewModelProvider.get() as T
    }
}

inline fun <reified T : ViewModel> Fragment.viewModel(
        factory: ViewModelProvider.Factory, body: T.() -> Unit
) = ViewModelProvider(this, factory)[T::class.java].apply(body)