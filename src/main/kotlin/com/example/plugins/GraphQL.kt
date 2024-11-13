package com.example.plugins

import com.example.graphql.BookQueryService
import com.expediagroup.graphql.server.ktor.GraphQL
import io.ktor.server.application.*

fun Application.configureGraphQL() {
    install(GraphQL) {
        schema {
            packages = listOf("com.example")
            queries = listOf(BookQueryService())
        }
        server {
        }
        engine {
        }
    }
}