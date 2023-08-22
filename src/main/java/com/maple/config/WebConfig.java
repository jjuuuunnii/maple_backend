package com.maple.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.exception.jwt.CustomAuthenticationEntryPoint;
import com.maple.jwt.filter.JwtAuthenticationProcessingFilter;
import com.maple.jwt.service.JwtService;
import com.maple.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.maple.login.handler.LoginFailedHandler;
import com.maple.login.handler.LoginSuccessHandler;
import com.maple.login.service.PrincipalDetailsService;
import com.maple.oauth.converter.OauthServerTypeConverter;
import com.maple.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(new PrincipalDetailsService(userRepository));
        return new ProviderManager(provider);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter(){
        return new JwtAuthenticationProcessingFilter(jwtService, userRepository);
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        AuthenticationManager authManager = authenticationManager();
        LoginSuccessHandler loginSuccessHandler = loginSuccessHandler();
        LoginFailedHandler loginFailedHandler = loginFailedHandler();
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper(), jwtService, authManager);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authManager);
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailedHandler);
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(jwtService);
    }

    @Bean
    public LoginFailedHandler loginFailedHandler(){
        return new LoginFailedHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OauthServerTypeConverter());
    }
}
