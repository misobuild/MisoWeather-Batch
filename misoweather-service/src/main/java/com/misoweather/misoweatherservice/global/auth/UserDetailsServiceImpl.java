package com.misoweather.misoweatherservice.global.auth;

import com.misoweather.misoweatherservice.domain.member.Member;
import com.misoweather.misoweatherservice.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    //이름은 loadByUsername이지만
    // OAuth2 방식으로 구현할때 유니크 값은 userId이다.
    public UserDetails loadUserByUsername(String userPk){
        Member member = memberRepository.findById(Long.parseLong(userPk))
                .orElseThrow(() -> new UsernameNotFoundException("NOTFOUND"));
        return new com.misoweather.misoweatherservice.global.auth.UserDetailsImpl(member);
    }
}
