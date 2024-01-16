package com.example.projectwaifu.security;

import com.example.projectwaifu.models.entities.User;
import com.google.api.client.util.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.Serializable;
import java.util.*;


public class CustomUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = -1090244616449358260L;
    private User user;

    public CustomUserDetails(User user) { this.user = user;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roleAuthorities = new ArrayList<GrantedAuthority>();
        roleAuthorities.add(new SimpleGrantedAuthority("DEFAULT"));
        return roleAuthorities;
    }

    public void setUsername(String username) {
        this.user.setUsername(username);
    }

    public void setPassword(String password) {
        this.user.setPassword(password);
    }

    @Override
    public String getUsername() {return this.user.getUsername();}

    @Override
    public String getPassword() {return this.user.getPassword();}

    public String getEmail() {return this.user.getEmail();}

    public Long getId() {return this.user.getId();}

    public User getUser() {return this.user;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Configuration
    @EnableWebSecurity(debug = false)
    public static class SecurityConfig {
        @Value("${base-domain}")
        private String base_domain;
        private static final String[] ENDPOINTS_WHITELIST = {
                "/index.html",
                "/",
                "/assets/**",
                "/vite.svg",
                "/security/**",
        };

        private static final String[] ENDPOINTS_USERS = {
                "/api/**"
        };



        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http    .csrf(AbstractHttpConfigurer::disable)
                    .securityContext((securityContext) -> securityContext
                            .requireExplicitSave(true)
                            .securityContextRepository(new HttpSessionSecurityContextRepository())
                    )
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                            .requestMatchers(ENDPOINTS_USERS).hasAuthority("DEFAULT")
                            .anyRequest().authenticated()
                    )
                    .oauth2Login(oauth2 -> oauth2
                            .redirectionEndpoint(redirection -> redirection
                                    .baseUri(base_domain+"/security/oauth2/callback")
                            )
                    );
            return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                UserDetailsService userDetailsService,
                PasswordEncoder passwordEncoder) {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder);

            ProviderManager providerManager = new ProviderManager(authenticationProvider);
            providerManager.setEraseCredentialsAfterAuthentication(false);
            return providerManager;
        }

        @Bean
        public AuthenticationManager encodedAuthenticationManager(
                UserDetailsService userDetailsService
        ) {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

            ProviderManager providerManager = new ProviderManager(authenticationProvider);
            providerManager.setEraseCredentialsAfterAuthentication(false);
            return providerManager;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityContextRepository securityContextRepository() {
            return new HttpSessionSecurityContextRepository();
        }

    //    @Bean
    //    public WebSecurityCustomizer webSecurityCustomizer() {
    //        return (web) -> web.ignoring().requestMatchers("/index.html", "/", "/assets/**", "/vite.svg");
    //    }
    }
}
