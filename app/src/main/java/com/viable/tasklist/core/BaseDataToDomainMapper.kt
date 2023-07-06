package com.viable.tasklist.core

import com.viable.tasklist.domain.AbstractDataMapper
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class BaseDataToDomainMapper<S, R> : AbstractDataMapper.DataToDomain<S, R> {
    protected fun errorType(e: Exception) = when (e) {
        is UnknownHostException -> ErrorType.NO_CONNECTION
        is HttpException -> ErrorType.SERVICE_UNAVAILABLE
        else -> ErrorType.GENERIC_ERROR
    }
}
