package com.smp.security;

import com.smp.entity.MemberEntity;
import com.smp.service.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // DB에서 UserId로 사용자를 찾는 과정입니다.
        MemberEntity member = memberRepository.findByUserid(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        return User.builder()
                .username(member.getUserid())
                .password(member.getPassword())
                .roles(member.getRole()) // "USER" 또는 "ADMIN"
                .build();
    }
}