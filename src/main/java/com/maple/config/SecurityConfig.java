package com.maple.config;

import com.maple.exception.jwt.CustomAuthenticationEntryPoint;
import com.maple.jwt.filter.JwtAuthenticationProcessingFilter;
import com.maple.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;

    private final CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter;
    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico",
                        "/ts/**","/ttf/**").permitAll()
                .requestMatchers("/api/auth/signup/self","/api/auth/login/self").permitAll()
                .requestMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated())

                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint);


        http.addFilter(corsFilter);
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter, LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter,
                CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.getOrBuild();
    }


}
