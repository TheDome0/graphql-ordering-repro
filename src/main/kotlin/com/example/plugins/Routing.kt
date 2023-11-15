package com.example.plugins

import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        graphiQLRoute("/playground", "/127.0.0.1:8080/graphql")
        graphQLSDLRoute()
        graphQLPostRoute(streamingResponse = true)
    }
}
