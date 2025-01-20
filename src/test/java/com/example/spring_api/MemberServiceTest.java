package com.example.spring_api;

import com.example.spring_api.dto.MemberUpdateDto;
import com.example.spring_api.model.Member;
import com.example.spring_api.model.MemberRepository;
import com.example.spring_api.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    void 회원정보_수정() {
        // given
        Member member = new Member();
        member.setName("홍길동");
        member.setEmail("hong@test.com");
        member.setPassword("1234");
        Long savedId = memberService.join(member);

        // when
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("홍길동2");
        updateDto.setEmail("hong2@test.com");
        memberService.updateMember(savedId, updateDto);

        // then
        Member updatedMember = memberService.findOne(savedId);
        assertThat(updatedMember.getName()).isEqualTo("홍길동2");
        assertThat(updatedMember.getEmail()).isEqualTo("hong2@test.com");
    }

    @Test
    void 존재하지_않는_회원_수정() {
        // given
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("홍길동2");
        updateDto.setEmail("hong2@test.com");

        // when & then
        assertThatThrownBy(() -> memberService.updateMember(999L, updateDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 회원입니다");
    }

    @Test
    void 회원_검색() {
        // given
        Member member1 = new Member();
        member1.setName("홍길동");
        member1.setEmail("hong@test.com");
        member1.setPassword("1234");

        Member member2 = new Member();
        member2.setName("김길동");
        member2.setEmail("kim@test.com");
        member2.setPassword("1234");

        memberService.join(member1);
        memberService.join(member2);

        // when
        List<Member> result1 = memberService.searchMembers("길동");
        List<Member> result2 = memberService.searchMembers("test.com");
        List<Member> result3 = memberService.searchMembers("없음");

        // then
        assertThat(result1).hasSize(2);
        assertThat(result2).hasSize(2);
        assertThat(result3).isEmpty();
    }

    @Test
    void 검색어_없는_경우() {
        assertThatThrownBy(() -> memberService.searchMembers(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("검색어를 입력해주세요");
    }
}