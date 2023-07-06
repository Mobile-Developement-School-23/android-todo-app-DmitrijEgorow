package com.viable.tasklist.domain

import com.viable.tasklist.core.BaseDataToDomainMapper
import com.viable.tasklist.core.ErrorType
import com.viable.tasklist.data.cloud.ReceivedData

interface TasksReceivedDataMapper<T> : AbstractDataMapper.DataToDomain<ReceivedData, T> {

    class Success() : TasksReceivedDataMapper<ReceivedData> {
        override fun map(data: ReceivedData): ReceivedData {
            return data
        }

        override fun map(e: Exception): ReceivedData {
            return ReceivedData.Empty()
        }
    }

    class Error : BaseDataToDomainMapper<ReceivedData, ErrorType>(), TasksReceivedDataMapper<ErrorType> {

        override fun map(data: ReceivedData) = ErrorType.GENERIC_ERROR
        override fun map(e: Exception) = errorType(e)
    }
}
