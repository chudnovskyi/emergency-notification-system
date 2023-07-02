package com.example.security.dto.response;

import com.example.security.entity.Address;
import com.example.security.model.Role;
import lombok.Builder;

@Builder
public record ClientResponse(
        Long id,
        Role role,
        String email,
        String name,
        String company,
        String phoneNumber,
        Address address
) {
}
