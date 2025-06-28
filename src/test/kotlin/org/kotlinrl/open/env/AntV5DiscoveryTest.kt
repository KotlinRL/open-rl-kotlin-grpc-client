package org.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.*
import open.rl.env.EnvOuterClass.DType.float32
import open.rl.env.EnvOuterClass.DType.float64
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class AntV5DiscoveryTest : StringSpec({
    "Ant-v5 should have action space of BoxSpace" {
        val env = RemoteEnv("Ant-v5")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe BOX
        val box = actionSpace.box
        box.low.shapeList shouldBe listOf(8)
        box.low.dtype shouldBe float32
        box.low.data.toFloatArray() shouldBe floatArrayOf(-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f)

        box.high.shapeList shouldBe listOf(8)
        box.high.dtype shouldBe float32
        box.high.data.toFloatArray() shouldBe floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f)

        box.shapeList shouldBe listOf(8)
        box.dtype shouldBe float32

        env.close()
    }

    "Ant-v5 should have observation space of BoxSpace" {
        val env = RemoteEnv("Ant-v5")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe BOX
        val box = observationSpace.box
        box.low.shapeList shouldBe listOf(105)
        box.low.dtype shouldBe float64
        box.low.data.toDoubleArray().size shouldBe 105
        box.low.data.toDoubleArray() shouldBe (0 until 105).map { Double.NEGATIVE_INFINITY }.toDoubleArray()

        box.high.shapeList shouldBe listOf(105)
        box.high.dtype shouldBe float64
        box.high.data.toDoubleArray().size shouldBe 105
        box.high.data.toDoubleArray() shouldBe (0 until 105).map { Double.POSITIVE_INFINITY }.toDoubleArray()

        box.shapeList shouldBe listOf(105)
        box.dtype shouldBe float64

        env.close()
    }

    "Ant-v5 should have an expected initial observation" {
        val env = RemoteEnv("Ant-v5")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe ARRAY
        observation.array.shapeList shouldBe listOf(105)
        observation.array.dtype shouldBe float64
        observation.array.data.toDoubleArray().size shouldBe 105

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()

        env.close()
    }

    "Ant-v5 should have metadata" {
        val env = RemoteEnv("Ant-v5")
        env.metadata shouldNotBe null
        val renderFps = env.metadata.fieldsMap["render_fps"]?.numberValue
        renderFps shouldBe 20.0
        val renderModes = env.metadata.fieldsMap["render_modes"]
            ?.listValue
            ?.valuesList
            ?.mapNotNull { it.stringValue }
            ?: emptyList()
        renderModes shouldBe listOf("human", "rgb_array", "depth_array", "rgbd_tuple")
        env.close()
    }
})