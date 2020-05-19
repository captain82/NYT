package com.example.nyt.mvi

import com.example.nyt.model.NewsResponseModel

sealed class NewsActionState {

    object LoadingState:NewsActionState()
    data class DataState(val newsResponse:NewsResponseModel?):NewsActionState()
    data class ErrorState(val throwable: Throwable):NewsActionState()


}