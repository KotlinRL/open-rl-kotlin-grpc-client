package io.github.kotlinrl.open.env

fun FloatArray.shouldBeWithinTolerance(expected: FloatArray, tolerance: Float = 1e-6f) {
    for (i in indices) {
        val delta = kotlin.math.abs(this[i] - expected[i])
        if (delta > tolerance) {
            throw AssertionError(
                "Element $i: Expected ${expected[i]}, but was ${this[i]} (delta $delta > tolerance $tolerance)"
            )
        }
    }
}