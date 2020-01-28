package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.model

data class UserDetailViewData(
        val name: String,
        val reputation: Int,
        val creationDate: Int,
        val userType: String,
        val location: String? = null,
        val profileImage: String
)