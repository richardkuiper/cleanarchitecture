package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.TopUserDetailInteractor
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.mapper.UserEntityToUserDetailViewDataMapper
import javax.inject.Inject

class TopUserDetailViewModelFactoryProvider @Inject constructor(
        private val topUserDetailInteractor: TopUserDetailInteractor,
        private val userEntityToUserDetailViewDataMapper: UserEntityToUserDetailViewDataMapper
) {

    fun get(accountId: Int): ViewModelProvider.Factory =
            TopUserDetailViewModelFactory(accountId)

    private inner class TopUserDetailViewModelFactory(
            private val accountId: Int
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TopUserDetailViewModel(
                    accountId,
                    topUserDetailInteractor,
                    userEntityToUserDetailViewDataMapper
            ) as T
        }
    }
}