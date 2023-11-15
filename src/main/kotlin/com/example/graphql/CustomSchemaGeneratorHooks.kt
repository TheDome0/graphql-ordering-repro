package com.example.graphql

import arrow.core.Either
import com.example.errors.DomainError
import com.example.errors.NotFound
import com.example.errors.Unauthorized
import com.example.models.Book
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.schema.GraphQLType
import graphql.schema.GraphQLTypeReference
import graphql.schema.GraphQLUnionType
import kotlin.reflect.KClass
import kotlin.reflect.KType

class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        Either::class -> {
            val right = type.arguments.last().type?.classifier
            when (right) {
                Book::class -> bookUnion
                else -> null
            }
        }

        else -> null
    }
}

private val bookUnion = GraphQLUnionType.newUnionType()
    .name("BookUnion")
    .possibleType(GraphQLTypeReference("Book"))
    .possibleType(GraphQLTypeReference("NotFound"))
    .possibleType(GraphQLTypeReference("Unauthorized"))
    .typeResolver {
        when (val value = it.getObject<Either<DomainError, Book>>()) {
            is Either.Left -> {
                when (value.value) {
                    NotFound -> it.schema.getObjectType("NotFound")
                    Unauthorized -> it.schema.getObjectType("Unauthorized")
                }
            }

            is Either.Right -> it.schema.getObjectType("Book")
        }
    }
    .build()