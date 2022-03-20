package com.misoweather.misoweatherservice.config;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.member.auth.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
//        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        final UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken(email,
//                "password",
//                Arrays.asList(new SimpleGrantedAuthority(role)));
//
//        securityContext.setAuthentication(authenticationToken);
//        return securityContext;

        Member createdMember = Member.builder().nickname("testMember").build();

        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList();
        grantedAuthorities.add(new SimpleGrantedAuthority(annotation.role()));
        User principal = new User(annotation.email(), "1234", true, true, true, true,
                grantedAuthorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());

        authentication.setDetails(new UserDetailsImpl(createdMember));
        context.setAuthentication(authentication);
        return context;
    }
}