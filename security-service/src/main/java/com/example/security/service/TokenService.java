package com.example.security.service;

import com.example.security.entity.Client;
import com.example.security.entity.Token;
import com.example.security.exception.client.ClientJwtNotFoundException;
import com.example.security.exception.client.ClientNotFoundException;
import com.example.security.exception.token.InvalidTokenException;
import com.example.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TokenService {

    private final JwtService jwtService;
    private final ClientService clientService;
    private final TokenRepository tokenRepository;
    private final MessageSourceService message;

    public void createToken(Client client, String jwt) {
        tokenRepository.save(Token.builder().client(client).jwt(jwt).expired(false).revoked(false).build());
    }

    public void deletePreviousClientToken(Client client) {
        tokenRepository.findByClient_Id(client.getId()).ifPresent(tokenRepository::delete);
    }

    public boolean isTokenValid(String jwtToken) {
        Client client = takeUserDetailsFromJwt(jwtToken);
        boolean isTokenValid = tokenRepository.findByJwt(jwtToken)
                .map(token -> !token.isExpired() && !token.isRevoked())
                .orElse(false);

        if (isTokenValid && jwtService.isJwtValid(jwtToken, client)) {
            return true;
        } else {
            throw new InvalidTokenException(message.getProperty("jwt.invalid"));
        }
    }

    public Client takeUserDetailsFromJwt(String jwt) {
        String email = jwtService.extractEmail(jwt);
        try {
            return clientService.loadUserByUsername(email);
        } catch (ClientNotFoundException e) {
            throw new ClientJwtNotFoundException(e.getMessage());
        }
    }
}
