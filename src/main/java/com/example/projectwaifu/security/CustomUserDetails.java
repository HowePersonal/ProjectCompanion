package com.example.projectwaifu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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



}
