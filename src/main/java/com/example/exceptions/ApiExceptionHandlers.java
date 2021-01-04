package com.example.exceptions;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.codec.CodecException;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;
import javax.ws.rs.Produces;

@Produces
@Singleton
@Requires(classes = {CodecException.class, ExceptionHandler.class})
public class ApiExceptionHandlers implements ExceptionHandler<CodecException, HttpResponse> {
    @Override
    public HttpResponse handle(HttpRequest request, CodecException exception) {
        return HttpResponse.badRequest(new ApiErrorMessage(exception.getLocalizedMessage()));
    }
}

@Produces
@Singleton
@Requires(classes = {BadInputException.class, ExceptionHandler.class})
class BadInputExceptionHandler implements ExceptionHandler<BadInputException, HttpResponse> {
    @Override
    public HttpResponse handle(HttpRequest request, BadInputException exception) {
        return HttpResponse.badRequest(new ApiErrorMessage(exception.getLocalizedMessage()));
    }
}
