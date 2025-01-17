package com.example.spring_api;

import com.example.spring_api.model.Member;
import com.example.spring_api.model.MemberRepository;
import com.example.spring_api.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("홍길동");
        member.setEmail("hong@test.com");
        member.setPassword("1234");

        // when
        Long savedId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(savedId);
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("홍길동");
        member1.setEmail("hong@test.com");
        member1.setPassword("1234");

        Member member2 = new Member();
        member2.setName("홍길동");
        member2.setEmail("hong@test.com");
        member2.setPassword("1234");

        // when
        memberService.join(member1);

        // then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }
}