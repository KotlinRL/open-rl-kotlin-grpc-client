package io.github.kotlinrl.open.env

import com.google.protobuf.Struct
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import io.kotest.matchers.types.instanceOf
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class TaxiV3DiscoveryTest : StringSpec({
    "Taxi-v3 should have action space of DiscreteSpace" {
        val env = RemoteEnv("Taxi-v3")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe DISCRETE
        val discrete = actionSpace.discrete
        discrete.n shouldBe 6
        discrete.start shouldBe 0.0f
        env.close()
    }

    "Taxi-v3 should have observation space of DiscreteSpace" {
        val env = RemoteEnv("Taxi-v3")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe DISCRETE
        val discrete = observationSpace.discrete
        discrete.n shouldBe 500
        discrete.start shouldBe 0.0f
        env.close()
    }

    "Taxi-v3 should have an expected initial observation" {
        val env = RemoteEnv("Taxi-v3")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe INT32
        observation.int32 shouldBe 341

        info shouldNotBe null
        info shouldBe instanceOf(Struct::class)

        env.close()
    }

    "Taxi-v3 should have metadata" {
        val env = RemoteEnv("Taxi-v3")
        env.metadata shouldNotBe null
        val renderFps = env.metadata.fieldsMap["render_fps"]?.numberValue
        renderFps shouldBe 4.0
        val renderModes = env.metadata.fieldsMap["render_modes"]
            ?.listValue
            ?.valuesList
            ?.mapNotNull { it.stringValue }
            ?: emptyList()
        renderModes shouldBe listOf("human", "ansi", "rgb_array")
        env.close()
    }
})