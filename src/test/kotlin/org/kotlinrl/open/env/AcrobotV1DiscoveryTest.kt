package org.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.DType.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class AcrobotV1DiscoveryTest : StringSpec({
    "Acrobot-v1 should have action space of DiscreteSpace" {
        val env = RemoteEnv("Acrobot-v1")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe DISCRETE
        val discrete = actionSpace.discrete
        discrete.n shouldBe 3
        discrete.start shouldBe 0.0f
        env.close()
    }

    "Acrobot-v1 should have observation space of BoxSpace" {
        val env = RemoteEnv("Acrobot-v1")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(6)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-1.0f, -1.0f, -1.0f, -1.0f, -12.566371f, -28.274334f)

        box.high.shapeList shouldBe listOf(6)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f, 12.566371f, 28.274334f)

        box.shapeList shouldBe listOf(6)
        box.dtype shouldBe float32
        env.close()
    }

    "Acrobot-v1 should have an expected initial observation" {
        val env = RemoteEnv("Acrobot-v1")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(6)
        observation.array.dtype shouldBe float32
        observation.array.data.toFloatArray() shouldBe floatArrayOf(0.99933505f, 0.03646229f, 0.99602115f, -0.089117415f, -0.055928025f, -0.06312564f)

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()
        env.close()
    }

    "Acrobot-v1 should have metadata" {
        val env = RemoteEnv("Acrobot-v1")
        env.metadata shouldNotBe null
        val renderFps = env.metadata.fieldsMap["render_fps"]?.numberValue
        renderFps shouldBe 15.0
        val renderModes = env.metadata.fieldsMap["render_modes"]
            ?.listValue
            ?.valuesList
            ?.mapNotNull { it.stringValue }
            ?: emptyList()
        renderModes shouldBe listOf("human", "rgb_array")
        env.close()
    }
})
