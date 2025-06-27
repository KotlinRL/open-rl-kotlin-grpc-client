package org.kotlinrl.open.env

import com.google.protobuf.ByteString
import open.rl.env.EnvOuterClass.*

fun action(value: Int) = Action.newBuilder().setInt32(value).build()
fun action(value: Float) = Action.newBuilder().setFloat(value).build()
fun action(value: String) = Action.newBuilder().setString(value).build()
fun action(value: NDArray) = Action.newBuilder().setArray(value).build()
fun action(dtype: DType, shape: IntArray, data: ByteArray) =
    action(NDArray.newBuilder()
        .setDtype(dtype)
        .addAllShape(shape.toList())
        .setData(ByteString.copyFrom(data))
        .build())