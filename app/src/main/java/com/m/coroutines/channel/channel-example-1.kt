package com.m.coroutines.channel

import com.m.coroutines.delay.delay

// https://tour.golang.org/concurrency/1

suspend fun say(s: String) {
    for (i in 0..4) {
        delay(100)
        println(s)
    }
}

fun main(args: Array<String>) = mainBlocking {
    go { say("world") }
    say("hello")
}

