package com.nybble.propify.carriershipping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final String API_PATH = "/api/**";     
    private static final String PUBLIC_PATH = "public/**";
    @Autowired 
    private AuthenticationConfiguration authConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .antMatchers(PUBLIC_PATH).permitAll()
                        .antMatchers(API_PATH).authenticated())
                .addFilterBefore(authFilter(), AnonymousAuthenticationFilter.class)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN)));

                return http.build();
    }

    public AuthenticationManager authenticationManager() throws Exception {
        return this.authConfig.getAuthenticationManager();
    }
         

    AuthTokenFilter authFilter() throws Exception {
		RequestMatcher protectedUrls = new OrRequestMatcher(new AntPathRequestMatcher(API_PATH));
		final AuthTokenFilter filter = new AuthTokenFilter(protectedUrls);
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}
}