package com.smp.service;

import com.smp.entity.MemberEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(String userid, String password) {

        MemberEntity member = new MemberEntity();

        member.setUserid(userid);

        // 🔥 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(password));

        member.setRole("USER");

        memberRepository.save(member);
    }

    // 로그인용 조회
    public MemberEntity findByUserid(String userid) {
        return memberRepository.findByUserid(userid)
                .orElse(null);
    }
    
    public List<MemberEntity> findAllUser()  {
    	return memberRepository.findAll();
    }

	public MemberEntity findById(Long id) {
		 return memberRepository.findById(id)
	                .orElse(null);
		
	}
}