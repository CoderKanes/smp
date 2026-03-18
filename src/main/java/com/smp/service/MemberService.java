package com.smp.service;

import com.smp.entity.MemberEntity;
import com.smp.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveMembers(List<MemberEntity> memberList) {
        for (MemberEntity member : memberList) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            memberRepository.save(member);
        }
    }
}
        
    
