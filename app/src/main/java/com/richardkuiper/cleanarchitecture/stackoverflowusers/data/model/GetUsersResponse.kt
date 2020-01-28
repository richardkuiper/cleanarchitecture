package com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model

import com.google.gson.annotations.SerializedName

data class GetUsersResponse(
        @SerializedName("items") val users: List<User>,
        @SerializedName("has_more") val hasMore: Boolean,
        @SerializedName("quota_max") val quotaMax: Int,
        @SerializedName("quota_remaining") val quotaRemaining: Int
)