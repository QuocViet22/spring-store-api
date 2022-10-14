package com.spring.store.api.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.store.api.security.services.AccountDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccountDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            String jwtPostMan = parseJwtForPostMan(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt) /*&& jwtPostMan != null && jwtUtils.validateJwtToken(jwtPostMan)*/) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
//                String usernamePostMan = jwtUtils.getUserNameFromJwtToken(jwtPostMan);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                UserDetails userDetailsPostMan = userDetailsService.loadUserByUsername(usernamePostMan);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
//                UsernamePasswordAuthenticationToken authenticationPostMan =
//                        new UsernamePasswordAuthenticationToken(userDetailsPostMan,
//                                null,
//                                userDetailsPostMan.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                authenticationPostMan.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                SecurityContextHolder.getContext().setAuthentication(authenticationPostMan);
            }
            if (jwtPostMan != null && jwtUtils.validateJwtToken(jwtPostMan)) {
                String usernamePostMan = jwtUtils.getUserNameFromJwtToken(jwtPostMan);
                UserDetails userDetailsPostMan = userDetailsService.loadUserByUsername(usernamePostMan);
                UsernamePasswordAuthenticationToken authenticationPostMan =
                        new UsernamePasswordAuthenticationToken(userDetailsPostMan,
                                null,
                                userDetailsPostMan.getAuthorities());
                authenticationPostMan.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationPostMan);
            }

        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwtForPostMan(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
