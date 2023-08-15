package com.maple.config;
import com.maple.exception.jwt.CustomAuthenticationEntryPoint;
import com.maple.jwt.filter.JwtAuthenticationProcessingFilter;
import com.maple.jwt.service.JwtService;
import com.maple.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.maple.login.service.PrincipalDetailsService;
import com.maple.oauth2.handler.OAuth2LoginFailureHandler;
import com.maple.oauth2.handler.OAuth2LoginSuccessHandler;
import com.maple.oauth2.service.CustomOAuthUserService;
import com.maple.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuthUserService customOAuthUserService;
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

                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**").permitAll()
                .antMatchers("/api/auth/signup/self").permitAll()
                .anyRequest().authenticated()

                .and()

                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)

                .and()

                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuthUserService);

        http.addFilter(corsFilter);
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter, LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter, CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
