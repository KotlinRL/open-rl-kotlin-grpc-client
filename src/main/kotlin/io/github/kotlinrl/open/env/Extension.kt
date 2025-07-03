package io.github.kotlinrl.open.env

import com.google.protobuf.ByteString
import open.rl.env.EnvOuterClass.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

operator fun StepResponse.component1(): Observation = observation
operator fun StepResponse.component2() = reward
operator fun StepResponse.component3() = terminated
operator fun StepResponse.component4() = truncated
operator fun StepResponse.component5(): Info = info

operator fun ResetResponse.component1(): Observation = observation
operator fun ResetResponse.component2(): Info = info


fun ByteString.toFloatArray(): FloatArray {
    val bb = ByteBuffer.wrap(this.toByteArray()).order(ByteOrder.LITTLE_ENDIAN)
    val arr = FloatArray(this.size() / 4)
    for (i in arr.indices) {
        arr[i] = bb.float
    }
    return arr
}

fun ByteString.toDoubleArray(): DoubleArray {
    val bb = ByteBuffer.wrap(this.toByteArray()).order(ByteOrder.LITTLE_ENDIAN)
    val arr = DoubleArray(this.size() / 8)
    for (i in arr.indices) {
        arr[i] = bb.double
    }
    return arr
}

fun ByteString.toIntArray(): IntArray {
    val bb = ByteBuffer.wrap(this.toByteArray()).order(ByteOrder.LITTLE_ENDIAN)
    val arr = IntArray(this.size() / 4)
    for (i in arr.indices) {
        arr[i] = bb.int
    }
    return arr
}

fun ByteString.toLongArray(): LongArray {
    val bb = ByteBuffer.wrap(this.toByteArray()).order(ByteOrder.LITTLE_ENDIAN)
    val arr = LongArray(this.size() / 8)
    for (i in arr.indices) {
        arr[i] = bb.long
    }
    return arr
}