package com.wishlist.security;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JWTConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger logger = LoggerFactory.getLogger(JWTConverter.class);

	@Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");

        Collection<String> roles = realmAccess.get("roles");

        var grants = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+ role)).toList();
        
        StringBuilder sb = new StringBuilder("rokes:");

        roles.stream().forEach(role -> {
            sb.append(role.toString() + " ");
        });
        
        logger.info("JWT roles:" + roles.toString());

        return new JwtAuthenticationToken(jwt, grants);
    }

}
