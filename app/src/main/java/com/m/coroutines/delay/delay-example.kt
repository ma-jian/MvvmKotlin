package com.m.coroutines.delay

import com.m.coroutines.context.Swing
import com.m.coroutines.future.future
import com.m.coroutines.util.log
import util.*

fun main(args: Array<String>) {
    future(Swing) {
        log("Let's naively sleep for 1 second")
        delay(1000L)
        log("We're still in Swing EDT!")
    }
}