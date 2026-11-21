package br.com.bootcamp.gestaoemprestimos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HexFormat;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configurarSeguranca(HttpSecurity http) throws Exception{

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/equipamentos")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/equipamentos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/equipamentos/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/colaboradores/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/equipamentos/**")
                        .hasAnyRole("ADMIN", "COLABORADOR")

                        .requestMatchers("/emprestimos/**")
                        .hasAnyRole("ADMIN", "COLABORADOR")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarioTeste(PasswordEncoder passwordEncoder, @Value("${APP_LOGIN_EMAIL}") String email, @Value("${APP_LOGIN_PASSWORD}") String senha,
                                           @Value("${APP_COLAB_EMAIL}") String emailColaborador, @Value("${APP_COLAB_PASSWORD}") String senhaColaborador) {

        UserDetails admin = User.builder()
                .username(email)
                .password(passwordEncoder.encode(senha))
                .roles("ADMIN")
                .build();

        UserDetails colaborador = User.builder()
                .username(emailColaborador)
                .password(passwordEncoder.encode(senhaColaborador))
                .roles("COLABORADOR")
                .build();

        return new InMemoryUserDetailsManager(admin, colaborador);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public JwtEncoder jwtEncoder(@Value("${jwt.secret}") String segredo) {

        byte[] chaveBytes = HexFormat.of().parseHex(segredo);

        SecretKey chave = new SecretKeySpec(chaveBytes, "HmacSHA256");

        return NimbusJwtEncoder.withSecretKey(chave).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String segredo) {

        byte[] chaveBytes = HexFormat.of().parseHex(segredo);

        SecretKey chave = new SecretKeySpec(chaveBytes, "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(chave).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

        converter.setAuthoritiesClaimName("roles");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtConverter;
    }
}
