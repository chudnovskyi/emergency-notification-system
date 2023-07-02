package com.example.security.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String jwt
) {
}
