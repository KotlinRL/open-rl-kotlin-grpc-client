package io.github.kotlinrl.open.env

import com.google.protobuf.Struct
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import io.kotest.matchers.types.instanceOf
import open.rl.env.EnvOuterClass.DType.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class PendulumV1DiscoveryTest : StringSpec({
    "Pendulum-v1 should have action space of BoxSpace" {
        val env = RemoteEnv("Pendulum-v1")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe BOX
        val box = actionSpace.box
        box.low.shapeList shouldBe listOf(1)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-2.0f)

        box.high.shapeList shouldBe listOf(1)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(2.0f)

        box.shapeList shouldBe listOf(1)
        box.dtype shouldBe float32

        env.close()
    }

    "Pendulum-v1 should have observation space of BoxSpace" {
        val env = RemoteEnv("Pendulum-v1")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(3)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-1.0f, -1.0f, -8.0f)

        box.high.shapeList shouldBe listOf(3)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(1.0f, 1.0f, 8.0f)

        box.shapeList shouldBe listOf(3)
        box.dtype shouldBe float32

        env.close()
    }

    "Pendulum-v1 should have an expected initial observation" {
        val env = RemoteEnv("Pendulum-v1")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(3)
        observation.array.dtype shouldBe float32
        observation.array.data.toFloatArray() shouldBe floatArrayOf(0.4123625f, 0.91101986f, -0.89235795f)

        info shouldNotBe null
        info shouldBe instanceOf(Struct::class)
        env.close()
    }

    "Pendulum-v1 should have metadata" {
        val env = RemoteEnv("Pendulum-v1")
        env.metadata shouldNotBe null
        val renderFps = env.metadata.fieldsMap["render_fps"]?.numberValue
        renderFps shouldBe 30.0
        val renderModes = env.metadata.fieldsMap["render_modes"]
            ?.listValue
            ?.valuesList
            ?.mapNotNull { it.stringValue }
            ?: emptyList()
        renderModes shouldBe listOf("human", "rgb_array")
        env.close()
    }
})
