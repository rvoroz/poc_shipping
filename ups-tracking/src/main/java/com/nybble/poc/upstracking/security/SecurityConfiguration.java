package com.nybble.poc.upstracking.security;

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
import com.nybble.poc.upstracking.service.ApiTokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final String API_PATH = "/**";     
    @Autowired 
    private AuthenticationConfiguration authConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("**/health/**").permitAll()
				.antMatchers("!**/health/**").authenticated()
				.and()
                .authenticationProvider(authProvider())
                .addFilterBefore(authFilter(), AnonymousAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN));

                return http.build();
    }


    AuthTokenProvider authProvider(){
        return new AuthTokenProvider();
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