
# 🚀 Open RL Kotlin gRPC Client

A high-performance, idiomatic **Kotlin client for the Open RL Environment gRPC protocol**—enabling reinforcement learning (RL) agents and tools written in Kotlin or Java to interact with any gRPC-served RL environment, including [OpenAI Gymnasium](https://gymnasium.farama.org/), PettingZoo, and more.

---

## ✨ Features

- **Strongly-typed Kotlin API** for RL environment interaction over gRPC
- Connects to any server implementing the [Open RL Env proto](https://github.com/KotlinRL/open-rl-env/blob/main/Env.proto)
- Supports all core RL operations: create, reset, step, space queries, render, and close
- Designed for use with the [open-rl-gymnasium-grpc-server](https://github.com/KotlinRL/open-rl-gymnasium-grpc-server)
- Multi-environment, multi-session capable
- Compatible with JVM-based agents and frameworks

---

## 🏗️ Project Structure

```
open-rl-kotlin-grpc-client/
├── proto/                      # Protobuf definitions git submodule
├── src/
│   ├── main/
│   │   ├── kotlin/             # Kotlin source code for gRPC client
│   └── test/                   # Example usage/tests
├── build.gradle.kts            # Gradle build for easy integration
└── README.md
```

---

## 🚀 Getting Started

### 1. **Start a compatible RL Environment server**

The easiest way is with the [open-rl-gymnasium-grpc-server Docker image](https://hub.docker.com/r/kotlinrl/open-rl-gymnasium-grpc-server):

```bash
docker run --rm -p 50051:50051 kotlinrl/open-rl-gymnasium-grpc-server:latest
```

---

### 2. **Add the client to your Kotlin project**

Clone this repo, or add as a module/dependency in your JVM project.

---

### 3. **Use the Kotlin client in your RL agent**

#### **Sample Usage**

```kotlin
val env = RemoteEnv("CartPole-v1", host = "localhost", port = 50051)
val actionSpace = env.actionSpace
val observationSpace = env.observationSpace

val (observation, info) = env.reset(seed = 123)
while (true) {
    val action = ... // Your agent's logic
    val (nextObs, reward, terminated, truncated, stepInfo) = env.step(action)
    if (terminated || truncated) break
}
env.close()
```

---

### 4. **Build and Run**

Standard Gradle JVM build:
```bash
./gradlew build
./gradlew test
```

---

## 🌍 Why Use Open RL Kotlin Client?

- Train RL agents in Kotlin/Java with any gRPC-served environment
- Interoperate with Python-based environments (Gymnasium, PettingZoo, Unity, etc)
- Leverage type-safe, idiomatic Kotlin for RL research and engineering
- Power distributed, scalable, cross-language RL workflows

---

## 📚 Resources

- [Open RL Env Proto](https://github.com/KotlinRL/open-rl-env)
- [Gymnasium Documentation](https://gymnasium.farama.org/)
- [KotlinRL Project](https://github.com/KotlinRL)
- [open-rl-gymnasium-grpc-server](https://github.com/KotlinRL/open-rl-gymnasium-grpc-server)

---

## 🤝 Contributing

Contributions, bug reports, and feature suggestions welcome!  
Open an issue or submit a pull request—join the [KotlinRL](https://github.com/KotlinRL) community.

---

## 📄 License

Apache License Version 2.0

---

> **Open RL Kotlin gRPC Client:**  
> The JVM-native way to connect RL agents to any modern RL environment—local, cloud, or distributed.
