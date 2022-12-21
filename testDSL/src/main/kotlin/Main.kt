package com.example.kotlin.dsl

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Builds an User as [ObjectNode] with the given [init] builder.
 */
fun user(init: UserBuilder.() -> Unit): ObjectNode {
    val user = UserBuilder ()
    user.init()
    return user.content
}

class UserBuilder {
    val content = JsonNodeFactory.instance.ObjectNode()

    fun name(name: String){
        content.put("name", name)
    }

    fun email(email: String){
        content.put("email", email)
    }

    fun phoneNumbers(init: PhoneNumbersBuilder.() -> Unit){
        content.set<ArrayNode>("phoneNumbers", PhoneNumbersBuilder().apply(init).content)
    }

    class PhoneNumbersBuilder{
        val content = JsonNodeFactory.instance.arrayNode()

        operator fun String?.unaryPlus(){
            content.add(this?.let {
                    value -> phoneNumber {
                number{value}
            }
            })
        }

        operator fun ObjectNode.unaryPlus(){
            content.add(this)
        }
    }
}

fun phoneNumber(init: PhoneNumberBuilder.() -> Unit): ObjectNode {
    val phoneNumber = PhoneNumberBuilder()
    phoneNumber.init()
    return phoneNumber.content
}

class PhoneNumberBuilder{
    val content = JsonNodeFactory.instance.objectNode()

    fun number(value: String){
        content.put("number", value)
    }
}

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}