package com.example.graphql

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.errors.DomainError
import com.example.errors.NotFound
import com.example.models.Book
import com.expediagroup.graphql.server.operations.Query

class BookQueryService : Query {
    fun getBook(): NotFound = NotFound
}