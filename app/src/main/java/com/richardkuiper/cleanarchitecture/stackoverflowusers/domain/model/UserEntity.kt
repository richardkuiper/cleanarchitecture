package com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model

data class UserEntity(
        val accountId: Int,
        val age: Int,
        val reputation: Int,
        val creationDate: Int,
        val userType: String,
        val location: String? = null,
        val profileImage: String,
        val displayName: String
)