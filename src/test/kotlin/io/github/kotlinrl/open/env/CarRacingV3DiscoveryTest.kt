package io.github.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.DType.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class CarRacingV3DiscoveryTest : StringSpec({
    "CarRacing-v3 should have action space of BoxSpace" {
        val env = RemoteEnv("CarRacing-v3")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe BOX
        val box = actionSpace.box
        box.low.shapeList shouldBe listOf(3)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-1.0f, 0.0f, 0.0f)

        box.high.shapeList shouldBe listOf(3)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(1.0f, 1.0f, 1.0f)

        box.shapeList shouldBe listOf(3)
        box.dtype shouldBe float32

        env.close()
    }

    "CarRacing-v3 should have observation space of BoxSpace" {
        val env = RemoteEnv("CarRacing-v3")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(96, 96, 3)
        box.low.dtype shouldBe uint8

        box.high.shapeList shouldBe listOf(96, 96, 3)
        box.high.dtype shouldBe uint8

        val lowVals = box.low.data.toByteArray()
        lowVals.size shouldBe 96 * 96 * 3
        lowVals.take(10).all { it == 0.toByte() } shouldBe true

        val highVals = box.high.data.toByteArray()
        highVals.take(10).all { it == 255.toByte() } shouldBe true

        box.shapeList shouldBe listOf(96, 96, 3)
        box.dtype shouldBe uint8

        env.close()
    }

    "CarRacing-v3 should have an expected initial observation" {
        val env = RemoteEnv("CarRacing-v3")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(96, 96, 3)
        observation.array.dtype shouldBe uint8
        val values = observation.array.data.toByteArray()
        values.size shouldBe 96 * 96 * 3
        values.all { (it.toInt() and 0xFF) in 0..255 } shouldBe true

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()
        env.close()
    }

    "CarRacing-v3 should have metadata" {
        val env = RemoteEnv("CarRacing-v3")
        env.metadata shouldNotBe null
        val renderFps = env.metadata.fieldsMap["render_fps"]?.numberValue
        renderFps shouldBe 50.0
        val renderModes = env.metadata.fieldsMap["render_modes"]
            ?.listValue
            ?.valuesList
            ?.mapNotNull { it.stringValue }
            ?: emptyList()
        renderModes shouldBe listOf("human", "rgb_array", "state_pixels")
        env.close()
    }
})
