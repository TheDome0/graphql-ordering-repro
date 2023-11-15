package com.example.errors

sealed interface DomainError {
    val message: String?
        get() = null
}

data object NotFound : DomainError
data object Unauthorized : DomainError