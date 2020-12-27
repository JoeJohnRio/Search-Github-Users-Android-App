package com.example.githubsearch

import com.google.gson.annotations.SerializedName

data class SearchItem(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("login") var login: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null
)