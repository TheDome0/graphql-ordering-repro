package com.example.graphql

import com.example.errors.NotFound
import com.expediagroup.graphql.server.operations.Query

class BookQueryService : Query {
    fun getBook(): NotFound = NotFound
}