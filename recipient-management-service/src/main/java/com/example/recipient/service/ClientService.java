package com.example.recipient.service;

import com.example.recipient.dto.request.AuthenticationRequest;
import com.example.recipient.dto.request.RegistrationRequest;
import com.example.recipient.dto.response.AuthenticationResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.exception.ClientBadCredentialsException;
import com.example.recipient.exception.ClientEmailAlreadyExists;
import com.example.recipient.exception.ClientNotFoundException;
import com.example.recipient.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.recipient.model.Role.USER;

@Service
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MessageSourceService message;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public Boolean register(RegistrationRequest request) {
        if (isClientWithGivenEmailExists(request.email())) {
            throw new ClientEmailAlreadyExists(message.getProperty("client.email.already_exists"));
        }

        Client client = Client.builder()
                .role(USER)
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return clientRepository.save(client)
                .isEnabled();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
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

        return new AuthenticationResponse(jwt);
    }

    @Override
    public Client loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientRepository.findByEmail(username)
                .orElseThrow(() -> new ClientNotFoundException(
                        message.getProperty("client.not_found", username)
                ));
    }

    private boolean isClientWithGivenEmailExists(String email) {
        return clientRepository.findByEmail(email)
                .isPresent();
    }
}
