package com.example.grpcreactivedemo.client.service;

import com.example.grpcreactivedemo.GreetingRequest;
import com.example.grpcreactivedemo.GreetingResponse;
import com.example.grpcreactivedemo.ReactorGreetingServiceGrpc;
import com.example.grpcreactivedemo.StreamingGreetingResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Stream;

@Service
public class GreetingAdapter {

    @GrpcClient("reactive-greeting")
    private ReactorGreetingServiceGrpc.ReactorGreetingServiceStub serviceStub;

    public String greeting(String message) {
        Mono<GreetingResponse> mono = serviceStub.greeting(GreetingRequest.newBuilder().setMessage(message).build());
        GreetingResponse response = mono.block();
        if (response == null) {
            return "";
        }
        return response.getMessage();
    }


    public String greetingBiStream(String message) {
        if(message == null){
            return "";
        }
        Stream<GreetingRequest> requests = Arrays.stream(message.split(","))
                .map(msg -> GreetingRequest.newBuilder().setMessage(msg).build());
        Flux<GreetingRequest> fRequests = Flux.fromStream(requests);
        Flux<StreamingGreetingResponse> responseFlux = serviceStub.greetingBiDirectional(fRequests);
        return responseFlux.filter(StreamingGreetingResponse::hasGreetingResponse)
                .map(res -> res.getGreetingResponse().getMessage())
                .reduce(new StringBuilder(), (builder, msg) -> builder.append("-").append(msg))
                .map(StringBuilder::toString)
                .block();
    }
}
