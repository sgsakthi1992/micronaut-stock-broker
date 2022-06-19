package com.sakthi.error;


import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
@Produces
@Requires(classes = {FiatCurrencyNotSupportedException.class, ExceptionHandler.class})
public class FiatCurrencyNotSupportedExceptionHandler implements ExceptionHandler<FiatCurrencyNotSupportedException, HttpResponse<CustomError>> {
    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, FiatCurrencyNotSupportedException exception) {
        return HttpResponse.badRequest(
                new CustomError(
                        HttpStatus.BAD_REQUEST.getCode(),
                        "UNSUPPORTED FIAT CURRENCY",
                        exception.getMessage()
                )
        );
    }
}
