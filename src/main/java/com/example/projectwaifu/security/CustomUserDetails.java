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

    public Integer getId() {return this.user.getId();}

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


}
