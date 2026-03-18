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
    private String userid; // 회원 프라이머리키

    private String password; // 시큐리티 비번 (암호화 필요)

    // 반복 구조를 위한 필드 배열 처리 예시
    public void setFields(String[] data) {
        for (int i = 0; i < data.length; i++) {
            if (i == 0) this.userid = data[i];
            if (i == 1) this.password = data[i];
        }
    }
}