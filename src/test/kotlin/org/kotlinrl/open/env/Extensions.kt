package org.kotlinrl.open.env

fun FloatArray.shouldBeWithinTolerance(expected: FloatArray, tolerance: Float) {
    for (i in indices) {
        val delta = kotlin.math.abs(this[i] - expected[i])
        if (delta > tolerance) {
            throw AssertionError(
                "Element $i: Expected ${expected[i]}, but was ${this[i]} (delta $delta > tolerance $tolerance)"
            )
        }
    }
}