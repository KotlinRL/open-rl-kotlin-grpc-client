package org.kotlinrl.open.env

import com.google.protobuf.Struct
import io.grpc.*
import io.grpc.ManagedChannelBuilder.*
import kotlinx.coroutines.*
import open.rl.env.EnvGrpcKt.EnvCoroutineStub
import open.rl.env.EnvOuterClass.*
import open.rl.env.EnvOuterClass.Empty
import open.rl.env.EnvOuterClass.SpaceRequest.SpaceType.*
import java.util.concurrent.TimeUnit.*

class RemoteEnv(
    envName: String,
    private val render: Boolean = true,
    options: Map<String, String> = emptyMap(),
    target: String = "localhost:50051"
) {
    private val channel: ManagedChannel = forTarget(target)
        .usePlaintext()
        .build()
    private val stub = EnvCoroutineStub(channel)
    var metadata: Struct
        private set
    var clientId: String
        private set
    var observationSpace: Space
        private set
    var actionSpace: Space
        private set

    init {
        runBlocking {
            val made  = stub.make(
                MakeRequest.newBuilder()
                    .setEnvId(envName)
                    .setRender(render)
                    .putAllOptions(options)
                    .build()
            )
            clientId = made.envHandle
            metadata = made.metadata
            observationSpace = stub.getSpace(
                SpaceRequest.newBuilder()
                    .setEnvHandle(clientId)
                    .setSpaceType(OBSERVATION)
                    .build()
            ).space
            actionSpace = stub.getSpace(
                SpaceRequest.newBuilder()
                    .setEnvHandle(clientId)
                    .setSpaceType(ACTION)
                    .build()
            ).space
        }
    }

    fun reset(seed: Int? = null, options: Map<String, String>? = null): ResetResponse = runBlocking {
        val builder = ResetRequest.newBuilder().setEnvHandle(clientId)
        seed?.let { builder.setSeed(it) }
        options?.let { builder.putAllOptions(it) }
        stub.reset(builder.build())
    }

    fun step(action: Action): StepResponse = runBlocking {
        stub.step(
            StepRequest.newBuilder()
                .setEnvHandle(clientId)
                .setAction(action)
                .build()
        )
    }

    fun render(): RenderResponse = if (render) runBlocking {
        stub.render(
            RenderRequest.newBuilder()
                .setEnvHandle(clientId)
                .build()
        )
    } else RenderResponse.newBuilder()
        .setEmpty(Empty.newBuilder().build())
        .build()

    fun close() = runBlocking {
        stub.close(CloseRequest.newBuilder().setEnvHandle(clientId).build())
        channel.shutdown().awaitTermination(5, SECONDS)
    }
}

