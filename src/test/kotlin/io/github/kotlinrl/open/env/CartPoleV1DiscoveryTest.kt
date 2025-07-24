package io.github.kotlinrl.open.env

import com.google.protobuf.Struct
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import io.kotest.matchers.types.instanceOf
import open.rl.env.EnvOuterClass.DType.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*
import kotlin.Float.Companion.NEGATIVE_INFINITY
import kotlin.Float.Companion.POSITIVE_INFINITY

class CartPoleV1DiscoveryTest : StringSpec({
    "CartPole-v1 should have action space of DiscreteSpace" {
        val env = RemoteEnv("CartPole-v1")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe DISCRETE
        val discrete = actionSpace.discrete
        discrete.n shouldBe 2
        discrete.start shouldBe 0.0f
        env.close()
    }

    "CartPole-v1 should have observation space of BoxSpace" {
        val env = RemoteEnv("CartPole-v1")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(4)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-4.8f, NEGATIVE_INFINITY, -0.41887903f, NEGATIVE_INFINITY)

        box.high.shapeList shouldBe listOf(4)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(4.8f, POSITIVE_INFINITY, 0.41887903f, POSITIVE_INFINITY)

        box.shapeList shouldBe listOf(4)
        box.dtype shouldBe float32

        env.close()
    }

    "CartPole-v1 should have an expected initial observation" {
        val env = RemoteEnv("CartPole-v1")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(4)
        observation.array.dtype shouldBe float32
        observation.array.data.toFloatArray() shouldBe floatArrayOf(0.018235186f, -0.0446179f, -0.027964013f, -0.03156282f)

        info shouldNotBe null
        info shouldBe instanceOf(Struct::class)
        env.close()
    }

    "CartPole-v1 should have metadata" {
        val env = RemoteEnv("CartPole-v1")
        env.metadata shouldNotBe null
        val renderFps = env.metadata.fieldsMap["render_fps"]?.numberValue
        renderFps shouldBe 50.0
        val renderModes = env.metadata.fieldsMap["render_modes"]
            ?.listValue
            ?.valuesList
            ?.mapNotNull { it.stringValue }
            ?: emptyList()
        renderModes shouldBe listOf("human", "rgb_array")
        env.close()
    }
})
