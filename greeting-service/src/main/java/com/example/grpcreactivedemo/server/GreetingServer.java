package com.example.grpcreactivedemo.server;

import com.example.grpcreactivedemo.service.GreetingServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.services.HealthStatusManager;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new GreetingServiceImpl())
                .build();

        // start
        server.start();

        // shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("gRPC server is shutting down!");
            server.shutdown();
        }));

        System.out.println("gRPC server has started!");
        server.awaitTermination();
        System.out.println("gRPC server has started2!");

    }
}
