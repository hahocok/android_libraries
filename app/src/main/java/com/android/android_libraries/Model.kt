package com.android.android_libraries

import java.util.*

class Model(private val count: Int) {
    private val counters = ArrayList<Int>(count)

    init {
        for(i in 0 until count) {
            counters.add(0)
        }
    }

    fun setAt(position: Int, value: Int) {
        counters[position] = value
    }

    fun getAt(position: Int): Int {
        return counters[position]
    }
}