package com.example.nyt.model

sealed class NewsActionState {
    object LoadingState : NewsActionState()
    object UpdateDb: NewsActionState()
    data class DataState(val newsResponse: NewsResponseModel?) : NewsActionState()
    data class ErrorState(val throwable: Throwable) : NewsActionState()
    object FinishState : NewsActionState()

}