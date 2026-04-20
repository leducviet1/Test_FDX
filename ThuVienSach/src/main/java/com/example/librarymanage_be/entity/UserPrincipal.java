package com.example.librarymanage_be.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

//User hiện tại
public class UserPrincipal implements UserDetails {
    private final int id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    public UserPrincipal(int id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    //Convert từ User entity -> UserPrincipal
    public static UserPrincipal build(Users user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole userRole : user.getUserRoles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getUserRoles()));
        }
        return new UserPrincipal(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Mật khẩu còn hiệu lực
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserPrincipal userPrincipal = (UserPrincipal) obj;
        return Objects.equals(id, userPrincipal.id) && Objects.equals(email, userPrincipal.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
