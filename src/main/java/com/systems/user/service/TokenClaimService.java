package com.systems.user.service;

import com.systems.user.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

@Component
public class TokenClaimService {
    public void populateUserClaims(JwtClaimsSet.Builder claims, UserPrincipal user) {
        claims.claim("id", user.getId().toString());
        claims.claim("username", user.getUsername());
        claims.claim("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        claims.claim("token_type", "user");
    }
}
