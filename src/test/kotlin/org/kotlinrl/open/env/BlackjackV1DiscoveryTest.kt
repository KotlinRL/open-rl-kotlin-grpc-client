package org.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import open.rl.env.EnvOuterClass.*
import open.rl.env.EnvOuterClass.Observation.ValueCase.INT32
import open.rl.env.EnvOuterClass.Space.TypeCase.*

class BlackjackV1DiscoveryTest : StringSpec({
    "Blackjack-v1 should have action space of DiscreteSpace" {
        val env = RemoteEnv("Blackjack-v1")
        val actionSpace = env.actionSpace
        actionSpace.typeCase shouldBe DISCRETE
        val discrete = actionSpace.discrete
        discrete.n shouldBe 2
        discrete.start shouldBe 0.0f
        env.close()
    }

    "Blackjack-v1 should have observation space of TupleSpace" {
        val env = RemoteEnv("Blackjack-v1")
        val observationSpace = env.observationSpace
        observationSpace.typeCase shouldBe TUPLE
        val tuple = observationSpace.tuple
        tuple.spacesCount shouldBe 3
        tuple.spacesList.map { it.typeCase } shouldBe listOf(DISCRETE, DISCRETE, DISCRETE)
        tuple.spacesList[0].discrete.n shouldBe 32
        tuple.spacesList[1].discrete.n shouldBe 11
        tuple.spacesList[2].discrete.n shouldBe 2
        env.close()
    }

    "Blackjack-v1 should have an expected initial observation" {
        val env = RemoteEnv("Blackjack-v1")
        val (observation, info) = env.reset(seed = 123)
        observation.valueCase shouldBe Observation.ValueCase.TUPLE
        observation.tuple.itemsCount shouldBe 3
        observation.tuple.itemsList.map { it.valueCase } shouldBe listOf(INT32, INT32, INT32)
        observation.tuple.itemsList[0].int32 shouldBe 19
        observation.tuple.itemsList[1].int32 shouldBe 1
        observation.tuple.itemsList[2].int32 shouldBe 1

        info shouldNotBe null
        info.dataMap shouldBe emptyMap()

        env.close()
    }

    "Blackjack-v1 should have metadata" {
        val env = RemoteEnv("Blackjack-v1")
        env.metadata shouldNotBe null
        val renderFps = env.metadata.fieldsMap["render_fps"]?.numberValue
        renderFps shouldBe 4.0
        val renderModes = env.metadata.fieldsMap["render_modes"]
            ?.listValue
            ?.valuesList
            ?.mapNotNull { it.stringValue }
            ?: emptyList()
        renderModes shouldBe listOf("human", "rgb_array")
        env.close()
    }
})