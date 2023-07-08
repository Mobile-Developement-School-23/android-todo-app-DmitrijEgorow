package com.viable.tasklist.domain.mapper

import com.viable.tasklist.core.ErrorType

/*
interface TasksDataToUiMapper<T>{

    class Id(private val id: Read<Int>) : BooksDataMapper<BookData> {
        override fun map(data: List<BookData>) =
            data.find { it.map(BookDataMapper.Id(id)) } ?: BookData.Empty()

        override fun map(e: Exception) = BookData.Empty()
    }

    class Error : BaseDataToDomainMapper<List<BookData>, ErrorType>(), BooksDataMapper<ErrorType> {
        override fun map(data: List<BookData>) = ErrorType.GENERIC_ERROR
        override fun map(e: Exception) = errorType(e)
    }
}*/
interface AbstractDataMapper {
    interface Data<S, R> : AbstractDataMapper {
        fun map(data: S): R
    }

    interface DataToDomain<S, R> : Data<S, R> {
        fun map(e: Exception): R
    }

    interface DomainToUi<S, T> : Data<S, T> {
        fun map(errorType: ErrorType): T
    }

    class Empty : AbstractDataMapper
}
