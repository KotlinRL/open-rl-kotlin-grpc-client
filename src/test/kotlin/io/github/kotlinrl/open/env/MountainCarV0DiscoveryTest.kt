package io.github.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.DType.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class MountainCarV0DiscoveryTest : StringSpec({
    "MountainCar-v0 should have action space of BoxSpace" {
        val env = RemoteEnv("MountainCar-v0")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe DISCRETE
        val discrete = actionSpace.discrete
        discrete.n shouldBe 3
        discrete.start shouldBe 0.0f

        env.close()
    }

    "MountainCar-v0 should have observation space of BoxSpace" {
        val env = RemoteEnv("MountainCar-v0")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(2)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-1.2f, -0.07f)

        box.high.shapeList shouldBe listOf(2)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(0.6f, 0.07f)

        box.shapeList shouldBe listOf(2)
        box.dtype shouldBe float32

        env.close()
    }

    "MountainCar-v0 should have an expected initial observation" {
        val env = RemoteEnv("MountainCar-v0")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(2)
        observation.array.dtype shouldBe float32
        observation.array.data.toFloatArray() shouldBe floatArrayOf(-0.46352962f, 0.0f)

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()
        env.close()
    }

    "MountainCar-v0 should have metadata" {
        val env = RemoteEnv("MountainCar-v0")
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
