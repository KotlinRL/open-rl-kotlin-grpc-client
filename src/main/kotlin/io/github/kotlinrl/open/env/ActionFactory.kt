package io.github.kotlinrl.open.env

import com.google.protobuf.ByteString
import open.rl.env.EnvOuterClass.*

fun action(value: Int): Action = Action.newBuilder().setInt32(value).build()
fun action(value: Double): Action = Action.newBuilder().setDouble(value).build()
fun action(value: String): Action = Action.newBuilder().setString(value).build()
fun action(value: NDArray): Action = Action.newBuilder().setArray(value).build()
fun action(dtype: DType, shape: IntArray, data: ByteArray): Action =
    action(NDArray.newBuilder()
        .setDtype(dtype)
        .addAllShape(shape.toList())
        .setData(ByteString.copyFrom(data))
        .build())