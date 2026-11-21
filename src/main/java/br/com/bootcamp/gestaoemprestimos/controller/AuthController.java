package br.com.bootcamp.gestaoemprestimos.controller;

import br.com.bootcamp.gestaoemprestimos.dto.LoginRequest;
import br.com.bootcamp.gestaoemprestimos.dto.LoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Tag(name = "Autenticação", description = "Login e geração do token JWT")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public LoginResponse fazerLogin(@RequestBody LoginRequest request) {

        Authentication autenticacao = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        String perfil = autenticacao.getAuthorities().iterator().next().getAuthority();

        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder().subject(autenticacao.getName())
                .issuedAt(agora).expiresAt(agora.plus(1, ChronoUnit.HOURS))
                .claim("roles", perfil).build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new LoginResponse(token);
    }
}
