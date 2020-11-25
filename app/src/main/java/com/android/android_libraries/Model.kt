package com.android.android_libraries

import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class Model(private val count: Int) {
    private val counters = ArrayList<Int>(count)
    val observable: PublishSubject<Collection<Int>> = PublishSubject.create()

    init {
        for(i in 0 until count) {
            counters.add(0)
        }
    }

    fun setAt(position: Int, value: Int) {
        counters[position] = value
        observable.onNext(counters)
    }

    fun getAt(position: Int): Int {
        return counters[position]
    }
}