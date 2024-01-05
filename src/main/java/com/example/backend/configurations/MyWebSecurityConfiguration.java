package com.example.backend.configurations;

import com.example.backend.services.AuthTokenFilter;
import com.example.backend.services.CustomOAuth2UserService;
import com.example.backend.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.server.WebFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class MyWebSecurityConfiguration {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration, AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authorize) -> {
                    authorize
                            .requestMatchers("/**").permitAll()
                            .anyRequest().permitAll();
                });
//                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//                .requestMatchers("/api/events/**", "/api/notes/**").hasRole("ADMIN")
//                .requestMatchers("/api/auth/**").permitAll()
//                .requestMatchers("/api-docs**", "/swagger-ui/**").permitAll()
//                .requestMatchers("/api/helpers/**").permitAll()


//            http.authorizeRequests()
//                    .antMatchers("/", "/login", "/oauth/**").permitAll()
//                    .anyRequest().authenticated()
//                    .and()
//                    .formLogin().permitAll()
//                    .and()
//                    .oauth2Login()
//                    .userInfoEndpoint().userService(oauth2UserService)
//                    .and().successHandler((request, response, authentication) -> {
//                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//                        response.sendRedirect("/api/test/all");
//                    });
        return http.build();
    }
}
