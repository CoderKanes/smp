package com.smp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
public class MemberEntity {

    @Id
    private String userid; // 회원 ID (PK)

    private String password; // 암호화된 비밀번호

    private String role; // 권한 (USER / ADMIN)

}