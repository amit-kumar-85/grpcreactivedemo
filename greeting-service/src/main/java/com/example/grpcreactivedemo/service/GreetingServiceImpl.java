package com.example.grpcreactivedemo.service;

import com.example.grpcreactivedemo.GreetingRequest;
import com.example.grpcreactivedemo.GreetingResponse;
import com.example.grpcreactivedemo.ReactorGreetingServiceGrpc;
import com.example.grpcreactivedemo.StreamingGreetingResponse;
import com.google.rpc.Code;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@GrpcService
public class GreetingServiceImpl extends ReactorGreetingServiceGrpc.GreetingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(GreetingServiceImpl.class);

    @Autowired
    private ArsenalClientService arsenalClientService;

    @Override
    public Mono<GreetingResponse> greeting(Mono<GreetingRequest> request) {
        log.info("play health check: {}", arsenalClientService.getArsenalResponse().block());
        return request.map(GreetingRequest::getMessage)
                .map(msg -> GreetingResponse.newBuilder().setMessage(msg).build());
    }

    @Override
    public Flux<StreamingGreetingResponse> greetingSStream(Mono<GreetingRequest> request) {
        return request.filter(req -> req.getMessage() != null)
                .map(req -> req.getMessage())
                .map(msg -> msg.split(","))
                .flatMapIterable(arr -> Arrays.asList(arr))
                .map(str -> getStreamingGreetingResponse(str));

    }

    @Override
    public Mono<GreetingResponse> greetingCStream(Flux<GreetingRequest> request) {
        return request.map(req -> req.getMessage())
                .filter(msg -> msg != null)
                .reduce(new StringBuilder(), (acc, msg) -> acc.append(msg))
                .map(builder -> builder.toString())
                .map(msg -> GreetingResponse.newBuilder().setMessage(msg).build());
    }

    @Override
    public Flux<StreamingGreetingResponse> greetingBiDirectional(Flux<GreetingRequest> request) {
        return request.filter(req -> req.getMessage() != null)
                .map(req -> getStreamingGreetingResponse(req.getMessage()));
    }

    private StreamingGreetingResponse getStreamingGreetingResponse(String msg) {
        if (!msg.equalsIgnoreCase("error")) {
            return StreamingGreetingResponse.newBuilder().setGreetingResponse(GreetingResponse.newBuilder().setMessage(msg).build()).build();
        } else {
            return StreamingGreetingResponse.newBuilder().setStatus(com.google.rpc.Status.newBuilder()
                    .setCode(Code.INTERNAL.getNumber())
                    .setMessage("Error message found").build()).build();
        }
    }
}
