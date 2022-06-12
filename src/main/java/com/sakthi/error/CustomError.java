package com.sakthi.error;

import com.sakthi.api.RestApiResponse;

public record CustomError(
        int status,
        String error,
        String message
) implements RestApiResponse {
}
