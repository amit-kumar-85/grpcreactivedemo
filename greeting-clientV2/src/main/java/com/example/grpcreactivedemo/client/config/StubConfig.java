package com.example.grpcreactivedemo.client.config;

import com.example.grpcreactivedemo.ReactorGreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class StubConfig {

//    @Bean(destroyMethod = "shutdown")
    public ManagedChannel getManagedChannel() {
        System.out.println("managed channel bean created");
        return ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext().build();
    }

//    @Bean
    public ReactorGreetingServiceGrpc.ReactorGreetingServiceStub getGreetingServiceStub() {
        System.out.println("stub created");
        return ReactorGreetingServiceGrpc.newReactorStub(getManagedChannel());
    }
}
