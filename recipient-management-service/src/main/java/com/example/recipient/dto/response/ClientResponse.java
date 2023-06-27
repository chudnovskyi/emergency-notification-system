package com.example.recipient.dto.response;

import com.example.recipient.entity.Address;
import com.example.recipient.model.Role;

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
