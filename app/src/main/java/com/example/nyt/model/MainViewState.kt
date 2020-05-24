package com.example.nyt.model


data class MainViewState(
    var isPageLoading:Boolean = false,
    var newsObject: NewsResponseModel? = null,
    var error: Boolean = false,
    var updateDb:Boolean = false
) {
}