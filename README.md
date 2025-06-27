
# Open RL Env (KotlinRL): Standard RL Environment gRPC Protocol

**Open RL Env** defines a universal [gRPC](https://grpc.io/) Protocol Buffers specification for reinforcement learning (RL) environments, making it easy to serve environments (like OpenAI Gymnasium) and connect agents from any language.

---

## 🚩 Purpose

- Provide a **canonical RL environment API** via Protocol Buffers.
- Enable **gRPC-based serving** of environments (e.g., Gymnasium, PettingZoo, Unity, custom).
- Allow **RL clients and agents in any language** (Kotlin, Python, Java, Go, etc.) to interact with environments for RL training and evaluation.

---

## ✨ Features

- **Standard gRPC API**: reset, step, action/observation space, render, frame streaming, and close.
- **Supports discrete & continuous action spaces**.
- **Multi-backend ready**: Gymnasium and more.
- **Multi-language client/server stubs** via codegen.

---

## 🗂️ Project Structure

```
open-rl-env/
├── proto/
│   └── Env.proto                           # The standard RL environment protobuf spec
├── servers/
│   └── open-rl-gymnasium-grpc-server/      # OpenAI Gymnasium 1.0.0 gRPC server implementation
├── clients/
│   ├── open-rl-kotlin-grpc-client/         # Kotlin client (done)
│   └── open-rl-python-grpc-client/         # Python client (todo)
└── README.md
```

---
## 🚀 Getting Started

### 1. **Run the Gymnasium gRPC Server with Docker** (other env servers may follow)

You can instantly serve OpenAI Gymnasium environments using the official Docker image:

```bash
docker pull kotlinrl/open-rl-gymnasium-grpc-server:latest
docker run --rm -p 50051:50051 kotlinrl/open-rl-gymnasium-grpc-server:latest
```

The gRPC server will listen on port `50051`.

---

### 2. **Connect With RL Agents**

Select your language of choice (Kotlin, Java, Python to follow, etc.) from the pre-build clients to interact with the server and perform RL training.

---

### 3. **Serve Additional Backends**

You can implement other environment servers (e.g., PettingZoo, Unity) using the same protocol and client code—just swap the backend container!

---
## 🌍 Why Use Open RL Env?

- **Decouple agent and environment codebases**
- **Train RL agents from any language**
- **Deploy environments remotely (cloud, cluster, desktop)**
- **Standardizes RL research and production pipelines**

---

## 📚 Resources

- [Gymnasium Documentation](https://gymnasium.farama.org/)
- [gRPC Documentation](https://grpc.io/docs/)
- [KotlinRL Project](https://github.com/KotlinRL)

---

## 🤝 Contributing

PRs, issues, and discussion welcome!  
Extend to new backends, support new environments, or improve the proto—join the [KotlinRL](https://github.com/KotlinRL) community.

---

## 📄 License

Apache License Version 2.0

---

> **Open RL Env:** Standardizing RL environment interfaces for a multi-language, multi-platform world.
