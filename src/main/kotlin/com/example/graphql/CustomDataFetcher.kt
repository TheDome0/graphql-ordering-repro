package com.example.graphql

import arrow.core.Either
import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import graphql.schema.DataFetcher
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.instanceParameter

class CustomPropertyDataFetcher(private val propertyGetter: KProperty.Getter<*>) : DataFetcher<Any?> {
    override fun get(environment: DataFetchingEnvironment): Any? {
        environment.getSource<Any?>().let { source ->
            return propertyGetter.call(getSourceValue(source))
        }
    }
}

class CustomFunctionDataFetcher(private val target: Any?, private val fn: KFunction<*>) :
    FunctionDataFetcher(target, fn) {
    override fun get(environment: DataFetchingEnvironment): Any? {
        val instance: Any? = getSourceValue(target ?: environment.getSource<Any?>())
        val instanceParameter = fn.instanceParameter

        return if (instance != null && instanceParameter != null) {
            val parameterValues = getParameters(fn, environment)
                .plus(instanceParameter to instance)

            if (fn.isSuspend) {
                runSuspendingFunction(environment, parameterValues)
            } else {
                runBlockingFunction(parameterValues)
            }
        } else {
            null
        }
    }
}

class DataFetcherFactoryProvider : SimpleKotlinDataFetcherFactoryProvider() {
    override fun propertyDataFetcherFactory(kClass: KClass<*>, kProperty: KProperty<*>): DataFetcherFactory<Any?> =
        DataFetcherFactory {
            CustomPropertyDataFetcher(kProperty.getter)
        }

    override fun functionDataFetcherFactory(
        target: Any?,
        kClass: KClass<*>,
        kFunction: KFunction<*>
    ): DataFetcherFactory<Any?> =
        DataFetcherFactory {
            CustomFunctionDataFetcher(target, kFunction)
        }
}

private fun getSourceValue(source: Any?): Any? = when (source) {
    is Either.Right<*> -> source.value
    is Either.Left<*> -> source.value
    else -> source
}