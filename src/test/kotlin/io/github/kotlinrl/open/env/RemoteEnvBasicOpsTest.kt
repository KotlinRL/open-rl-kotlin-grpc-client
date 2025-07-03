package io.github.kotlinrl.open.env

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import io.kotest.matchers.nulls.*

class RemoteEnvBasicOpsTest : StringSpec({

    "make and close should work" {
        val env = RemoteEnv("CartPole-v1")
        env.shouldNotBeNull()
        env.close()
    }

    "make should return a unique clientId" {
        val env1 = RemoteEnv("CartPole-v1")
        val env2 = RemoteEnv("CartPole-v1")
        env1.clientId.shouldNotBeNull() shouldNotBe env2.clientId.shouldNotBeNull()
        env1.close()
        env2.close()
    }

    "reset without seed should return a valid observation" {
        val env = RemoteEnv("CartPole-v1")
        val observation = env.reset()
        observation.shouldNotBeNull()
        env.close()
    }

    "reset with seed should return a valid observation" {
        val env = RemoteEnv("CartPole-v1")
        val observation = env.reset(seed = 1)
        observation.shouldNotBeNull()
        env.close()
    }

    "env should contain an observationSpace" {
        val env = RemoteEnv("CartPole-v1")
        env.observationSpace.shouldNotBeNull()
        env.close()
    }

    "env should contain an actionSpace" {
        val env = RemoteEnv("CartPole-v1")
        env.actionSpace.shouldNotBeNull()
        env.close()
    }

    "when render is false, render should return an empty frame" {
        val env = RemoteEnv("CartPole-v1", render = false)
        env.reset()
        val frame = env.render()
        frame.empty.shouldNotBeNull()
        env.close()
    }

    "when render is true, render should return a valid frame" {
        val env = RemoteEnv("CartPole-v1", render = true)
        env.reset()
        val frame = env.render()
        frame.rgbArray.shouldNotBeNull()
        env.close()
    }

    "step should return a valid response" {
        val env = RemoteEnv("CartPole-v1")
        env.reset()
        val (observation, reward, terminated, truncated, info) = env.step(action(1))
        observation.shouldNotBeNull()
        reward shouldNotBe null
        terminated shouldNotBe null
        truncated shouldNotBe null
        info.shouldNotBeNull()
        env.close()
    }
})