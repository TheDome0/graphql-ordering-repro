package com.example.errors

sealed interface DomainError {
    val message: String?
}

data object NotFound : DomainError {
    override val message: String? = "Not found"
}
