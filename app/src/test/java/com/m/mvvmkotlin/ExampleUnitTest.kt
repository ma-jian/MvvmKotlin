package com.m.mvvmkotlin

import com.m.mvvmkotlin.sample.BaseFragment
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun stringTest() {
        val indexOfToArray = "我是一条语句，你好吗，我们一a起去a背Ab景，好bB吗、\n，哈哈哈是不是很好。好不好".indexOfToArray("好吗")
        println(indexOfToArray)
    }

    @Test
    fun test() {
        DialogStackManager.addToStack(1, "aaaaaaa", "", {
            add(MainActivity::class.java)
        }, {
            add(BaseFragment::class.java)
        })

        DialogStackManager.addToStack(2, "bbbbbbbbb", "", {
            add(MainActivity::class.java)
        }, {
            add(BaseFragment::class.java)
        })

        DialogStackManager.addToStack(3, "ccccccccccc", "", {
            add(MainActivity::class.java)
        }, {
            add(BaseFragment::class.java)
        })

        DialogStackManager.addToStack(15, "dddddddddddd", "", {
            add(MainActivity::class.java)
        }, {
            add(BaseFragment::class.java)
        },launchMode = DialogStackManager.LaunchMode.SINGLEINSTANCE)

        DialogStackManager.addToStack(1, "dddddddddddd----1", "", {
            add(MainActivity::class.java)
        }, {
            add(BaseFragment::class.java)
        },launchMode = DialogStackManager.LaunchMode.SINGLEINSTANCE)

        DialogStackManager.addToStack(1, "eeeeeeeeeeee", "", {
            add(MainActivity::class.java)
        }, {
            add(BaseFragment::class.java)
        })

        DialogStackManager.addToStack(1, "fffffffffff", "", {
            add(MainActivity::class.java)
        }, {
            add(BaseFragment::class.java)
        })
    }
}

inline fun CharSequence.indexOfToArray(
    regex: CharSequence,
    startIndex: Int = 0,
    endIndex: Int = length,
    ignoreCase: Boolean = true,
    last: Boolean = false
): List<Int> {
    val indexArray = arrayListOf<Int>()
    val indices = if (!last)
        startIndex.coerceAtLeast(0)..endIndex.coerceAtMost(length)
    else
        lastIndex.coerceAtMost(endIndex) downTo 0.coerceAtLeast(startIndex)
    if (this is String && regex is String) { // smart cast
        for (index in indices) {
            if (regex.regionMatches(0, this, index, regex.length, ignoreCase)) {
                indexArray.add(index)
            }
        }
    } else {
        for (index in indices) {
            if (regex.regionMatchesImpl2(0, this, index, regex.length, ignoreCase)) {
                indexArray.add(index)
            }
        }
    }
    return indexArray
}

inline fun CharSequence.regionMatchesImpl2(
    thisOffset: Int,
    other: CharSequence,
    otherOffset: Int,
    length: Int,
    ignoreCase: Boolean
): Boolean {
    if ((otherOffset < 0) || (thisOffset < 0) || (thisOffset > this.length - length) || (otherOffset > other.length - length)) {
        return false
    }

    for (index in 0 until length) {
        if (!this[thisOffset + index].equals(other[otherOffset + index], ignoreCase))
            return false
    }
    return true
}