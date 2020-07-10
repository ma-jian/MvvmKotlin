package com.m.coroutines.channel

import com.m.coroutines.context.CommonPool
import com.m.coroutines.run.runBlocking

fun mainBlocking(block: suspend () -> Unit) = runBlocking(CommonPool, block)

fun go(block: suspend () -> Unit) = CommonPool.runParallel(block)

