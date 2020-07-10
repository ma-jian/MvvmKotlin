package com.m.coroutines.channel

// https://talks.golang.org/2012/concurrency.slide#27



fun main(args: Array<String>) = mainBlocking {
    val c = fanIn(boring("Joe"), boring("Ann"))
    for (i in 0..9) {
        println(c.receive())
    }
    println("Your're both boring; I'm leaving.")
}

suspend fun fanIn(input1: Channel<String>, input2: ReceiveChannel<String>): ReceiveChannel<String> {
    val c = Channel<String>()
    go {
        while(true) {
            val s = select<String> {
                input1.onReceive { it }
                input2.onReceive { it }
            }
            c.send(s)
        }
    }
    return c // return combo channel
}