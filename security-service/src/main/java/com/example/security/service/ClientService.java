package com.example.security.service;

import com.example.security.dto.request.SecurityRequest;
import com.example.security.dto.response.TokenResponse;
import com.example.security.entity.Client;
import com.example.security.exception.client.ClientBadCredentialsException;
import com.example.security.exception.client.ClientEmailAlreadyExistsException;
import com.example.security.exception.client.ClientNotFoundException;
import com.example.security.mapper.ClientMapper;
import com.example.security.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final MessageSourceService message;
    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ClientMapper mapper;

    public Boolean register(SecurityRequest request) {
        return Optional.of(clientRepository.findByEmail(request.email()))
                .map(client -> {
                    if (client.isPresent()) {
                        throw new ClientEmailAlreadyExistsException(message.getProperty("client.email.already_exists"));
                    } else {
                        return request;
                    }
                })
                .map(req -> mapper.mapToEntity(req, passwordEncoder))
                .map(clientRepository::saveAndFlush)
                .isPresent();
    }

    public TokenResponse authenticate(SecurityRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (InternalAuthenticationServiceException e) {
            throw new ClientNotFoundException(message.getProperty("client.not_found", request.email()));
        } catch (BadCredentialsException e) {
            throw new ClientBadCredentialsException(message.getProperty("client.bad_cred"));
        }

        Client client = loadUserByUsername(request.email());

        tokenService.deletePreviousClientToken(client);
        String jwt = jwtService.generateJwt(client);
        tokenService.createToken(client, jwt);

        return new TokenResponse(jwt);
    }

    @Override
    public Client loadUserByUsername(String username) {
        return clientRepository.findByEmail(username)
                .orElseThrow(() -> new ClientNotFoundException(
                        message.getProperty("client.not_found", username)
                ));
    }
}
