package com.banking.springboot_bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Indicates that this class is a configuration class, meaning it contains bean definitions and configuration settings
// that will be managed by the Spring container.
@Configuration
//Enables Spring Security’s web security support and provides the Spring MVC integration.
// It’s necessary to customize the security configuration for web applications
@EnableWebSecurity
//Enables method-level security, allowing you to use annotations like @PreAuthorize, @Secured, or
// @RolesAllowed on your service layer methods to enforce security checks.
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }



    //This method defines a bean for PasswordEncoder, which is used to encrypt passwords.
    //BCryptPasswordEncoder: A specific implementation of PasswordEncoder that uses the bcrypt hashing function,
    // which is a strong one-way hashing algorithm commonly used for password hashing. This ensures that passwords are securely stored.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//This method configures the security filter chain, which determines how different HTTP requests are secured.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(userDetailsService);
                authenticationProvider.setPasswordEncoder(passwordEncoder());
                return authenticationProvider;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Disables Cross-Site Request Forgery (CSRF) protection. CSRF is a security measure that helps protect
        // against certain types of attacks by ensuring that state-changing requests are accompanied by a secret token.
        // It is usually disabled in stateless REST APIs where clients are expected to be trusted.
        //requestMatchers(HttpMethod.POST, "/api/user").permitAll(): Allows anyone (including unauthenticated users)
        // to make POST requests to /api/user endpoint. This is typically used for endpoints like user registration
        // or login, where authentication is not required to access the endpoint.
        //anyRequest().authenticated(): Requires all other requests to be authenticated.
        // This means that any other endpoint in your application will require the user to be logged in to access it.
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.POST, "/api/user").
                                permitAll().requestMatchers(HttpMethod.POST, "/api/user/login").
                                permitAll().anyRequest().authenticated());
        //Configures the session management policy.
        //SessionCreationPolicy.STATELESS: Indicates that your application does not use sessions to
        // store user information. Instead, it is stateless, meaning each request should contain all
        // the information necessary for the server to process it. This is typical in RESTful services,
        // where authentication is often handled via tokens (e.g., JWT) rather than session cookies.
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//Builds and returns the SecurityFilterChain object, which is the core of Spring Security’s filter chain that
// secures HTTP requests according to the configuration.
        return httpSecurity.build();
    }
}