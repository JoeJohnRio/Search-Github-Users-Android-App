package com.example.githubsearch

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("total_count") var totalCount: Int? = null,
    @SerializedName("incomplete_results") var incompleteResults: Boolean? = null,
    @SerializedName("items") var items: MutableList<SearchItem>? = null,
    @SerializedName("message") var message: String? = null
)