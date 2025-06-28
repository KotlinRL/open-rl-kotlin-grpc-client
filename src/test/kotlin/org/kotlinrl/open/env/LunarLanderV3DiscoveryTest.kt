package org.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.DType.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class LunarLanderV3DiscoveryTest : StringSpec({
    "LunarLander-v3 should have action space of DiscreteSpace" {
        val env = RemoteEnv("LunarLander-v3")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe DISCRETE
        val discrete = actionSpace.discrete
        discrete.n shouldBe 4
        discrete.start shouldBe 0.0f
        env.close()
    }

    "LunarLander-v3 should have observation space of BoxSpace" {
        val env = RemoteEnv("LunarLander-v3")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(8)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-2.5f, -2.5f, -10.0f, -10.0f, -6.2831855f, -10.0f, -0.0f, -0.0f)

        box.high.shapeList shouldBe listOf(8)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(2.5f, 2.5f, 10.0f, 10.0f, 6.2831855f, 10.0f, 1.0f, 1.0f)

        box.shapeList shouldBe listOf(8)
        box.dtype shouldBe float32
        env.close()
    }

    "LunarLander-v3 should have an expected initial observation" {
        val env = RemoteEnv("LunarLander-v3")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(8)
        observation.array.dtype shouldBe float32
        println(observation.array.data.toFloatArray().map { "${it}f" })
        observation.array.data.toFloatArray().shouldBeWithinTolerance( floatArrayOf(0.0051769256f, 1.4033939f, 0.52433753f,
            -0.33451712f, -0.0059918435f, -0.11877034f, 0.0f, 0.0f), 1e-6f)

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()
        env.close()
    }

    "LunarLander-v3 should have metadata" {
        val env = RemoteEnv("LunarLander-v3")
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
