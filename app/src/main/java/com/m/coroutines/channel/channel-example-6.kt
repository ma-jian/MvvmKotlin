package com.m.coroutines.channel

import channel.*
import com.m.coroutines.delay.delay

// https://tour.golang.org/concurrency/6

fun main(args: Array<String>) = mainBlocking {
    val tick = Time.tick(100)
    val boom = Time.after(500)
    whileSelect {
        tick.onReceive {
            println("tick.")
            true // continue loop
        }
        boom.onReceive {
            println("BOOM!")
            false // break loop
        }
        onDefault {
            println("    .")
            delay(50)
            true // continue loop
        }
    }
}
