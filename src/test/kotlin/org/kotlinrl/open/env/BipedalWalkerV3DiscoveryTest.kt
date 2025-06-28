package org.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.DType.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class BipedalWalkerV3DiscoveryTest : StringSpec({
    "BipedalWalker-v3 should have action space of BoxSpace" {
        val env = RemoteEnv("BipedalWalker-v3")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe BOX
        val box = actionSpace.box
        box.low.shapeList shouldBe listOf(4)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-1.0f, -1.0f, -1.0f, -1.0f)

        box.high.shapeList shouldBe listOf(4)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

        box.shapeList shouldBe listOf(4)
        box.dtype shouldBe float32

        env.close()
    }

    "BipedalWalker-v3 should have observation space of BoxSpace" {
        val env = RemoteEnv("BipedalWalker-v3")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(24)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-3.1415927f, -5.0f, -5.0f, -5.0f, -3.1415927f, -5.0f,
            -3.1415927f, -5.0f, -0.0f, -3.1415927f, -5.0f, -3.1415927f, -5.0f, -0.0f, -1.0f, -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f)

        box.high.shapeList shouldBe listOf(24)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(3.1415927f, 5.0f, 5.0f, 5.0f, 3.1415927f, 5.0f,
            3.1415927f, 5.0f, 5.0f, 3.1415927f, 5.0f, 3.1415927f, 5.0f, 5.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f)

        box.shapeList shouldBe listOf(24)
        box.dtype shouldBe float32

        env.close()
    }

    "BipedalWalker-v3 should have an expected initial observation" {
        val env = RemoteEnv("BipedalWalker-v3")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(24)
        observation.array.dtype shouldBe float32
        observation.array.data.toFloatArray().shouldBeWithinTolerance(floatArrayOf(0.002745552f, 1.2205964E-5f, -0.0015916134f,
            -0.01600008f, 0.0925419f, 0.00369484f, 0.8597325f, -0.0015351385f, 1.0f, 0.03284419f, 0.003694687f,
            0.85350716f, -0.0025140182f, 1.0f, 0.4408134f, 0.4458195f, 0.46142212f, 0.4895495f, 0.534102f, 0.6024602f,
            0.7091479f, 0.88593054f, 1.0f, 1.0f), 1e-6f)

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()
        env.close()
    }

    "BipedalWalker-v3 should have metadata" {
        val env = RemoteEnv("BipedalWalker-v3")
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
