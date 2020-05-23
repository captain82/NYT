package com.example.nyt.mvi

import com.example.nyt.model.NewsResponseModel

data class MainViewState(
    var isPageLoading:Boolean = false,
    var newsObject: NewsResponseModel? = null,
    var error: Boolean = false
) {
}