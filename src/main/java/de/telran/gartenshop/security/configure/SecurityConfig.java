package de.telran.gartenshop.security.configure;

import de.telran.gartenshop.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                // Своего рода отключение CORS (разрешение запросов со всех доменов)
//                .cors(cors -> cors.configurationSource(request -> {
//                    var corsConfiguration = new CorsConfiguration();
//                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
//                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                    corsConfiguration.setAllowedHeaders(List.of("*"));
//                    corsConfiguration.setAllowCredentials(true);
//                    return corsConfiguration;
//                }))
                // Настройка доступа к конечным точкам
                .authorizeHttpRequests(
                        authz -> authz
//                                .requestMatchers(
//                                        "/**"
//                                )
                                .requestMatchers(
                                        "/products","/products/*",
                                        "/favorites","/favorites/*",
                                        "/auth/login", "/auth/token",
                                        "/users/register",
                                        "/users/registerAdmin",
                                        "/manage/**",
////                                        "/users/**",
                                        "/swagger-ui.html", "/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**"
//                                        /*",/**"*/
                                )
                                .permitAll()
                                .requestMatchers(PathRequest.toH2Console()).permitAll() //Allows access to /h2-console
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))  // Allows access to /h2-console frameset/frame pages
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
