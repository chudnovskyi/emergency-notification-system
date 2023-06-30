package com.example.recipient.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String jwt
) {
}
