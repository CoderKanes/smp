package com.smp.model;

import java.time.format.DateTimeFormatter;

import com.smp.entity.MemberEntity;

import lombok.Data;

@Data
public class MemberDTO {
    private Long id; // 회원 ID (PK)

    private String userid; // 	
    
    private String password; // 암호화된 비밀번호

    private String role; // 권한 (USER / ADMIN)
    
    public MemberDTO(MemberEntity entity) {
        this.id = entity.getId();
        this.userid = entity.getUserid();
        this.password = entity.getPassword();
        this.role = entity.getRole();
    }
}
