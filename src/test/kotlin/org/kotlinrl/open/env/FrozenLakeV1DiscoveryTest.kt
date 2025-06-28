package org.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.*
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class FrozenLakeV1DiscoveryTest : StringSpec({
    "FrozenLake-v1 should have action space of DiscreteSpace" {
        val env = RemoteEnv("FrozenLake-v1")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe DISCRETE
        val discrete = actionSpace.discrete
        discrete.n shouldBe 4
        discrete.start shouldBe 0.0f
        env.close()
    }

    "FrozenLake-v1 should have observation space of DiscreteSpace" {
        val env = RemoteEnv("FrozenLake-v1")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe DISCRETE
        val discrete = observationSpace.discrete
        discrete.n shouldBe 16
        discrete.start shouldBe 0.0f
        env.close()
    }

    "FrozenLake-v1 should have an expected initial observation" {
        val env = RemoteEnv("FrozenLake-v1")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe INT32
        observation.int32 shouldBe 0

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()

        env.close()
    }

    "FrozenLake-v1 should have metadata" {
        val env = RemoteEnv("FrozenLake-v1")
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