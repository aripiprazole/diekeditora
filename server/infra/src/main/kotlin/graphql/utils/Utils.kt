package com.diekeditora.infra.graphql.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.readValue
import graphql.schema.Coercing
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
internal inline fun <reified I, reified O> ObjectMapper.coercing(noinline fn: (String) -> O) =
    object : Coercing<I, O> {
        override fun serialize(result: Any?): O {
            return fn(valueToTree<ValueNode>(result).asText())
        }

        override fun parseValue(input: Any?): I {
            if (typeOf<O>().isSubtypeOf(typeOf<String>())) {
                return readValue("\"$input\"")
            }

            return readValue(input.toString())
        }

        override fun parseLiteral(input: Any?): I {
            if (typeOf<O>().isSubtypeOf(typeOf<String>())) {
                return readValue("\"$input\"")
            }

            return readValue(input.toString())
        }
    }
