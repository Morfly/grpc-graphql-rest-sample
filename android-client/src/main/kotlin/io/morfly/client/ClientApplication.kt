package io.morfly.client

import android.app.Application
import io.morfly.client.graphql.graphQLModule
import io.morfly.client.grpc.gRPCModule
import io.morfly.client.rest.restModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class ClientApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ClientApplication)
            modules(restModule, graphQLModule, gRPCModule)
        }
    }
}