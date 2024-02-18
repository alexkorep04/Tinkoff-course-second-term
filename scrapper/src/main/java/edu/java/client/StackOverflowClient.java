package edu.java.client;

import edu.java.entity.StackOverflowResponse;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<StackOverflowResponse> fetchQuestion(int questionId);
}
