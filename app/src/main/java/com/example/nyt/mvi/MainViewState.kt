package com.example.nyt.mvi

import com.example.nyt.model.NewsResponseModel

data class MainViewState(
    var isPageLoading: Boolean? = false,
    var isPullToRefresh: Boolean? = false,
    var newsObject: NewsResponseModel? = null,
    var error: Throwable? = null,
    var finished: Boolean? = false

) {
}