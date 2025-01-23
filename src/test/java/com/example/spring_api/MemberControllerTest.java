package com.example.spring_api;

import com.example.spring_api.dto.MemberUpdateDto;
import com.example.spring_api.model.Member;
import com.example.spring_api.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Test
    void 회원정보_수정_성공() throws Exception {
        // given
        Member member = new Member();
        member.setName("홍길동");
        member.setEmail("hong@test.com");
        member.setPassword("1234");
        Long savedId = memberService.join(member);

        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("홍길동2");
        updateDto.setEmail("hong2@test.com");

        // when & then
        mockMvc.perform(put("/api/members/{id}", savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 회원정보_수정_실패_유효성검사() throws Exception {
        // given
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("");  // 이름을 비워서 유효성 검사 실패 유도
        updateDto.setEmail("invalid-email");  // 잘못된 이메일 형식

        // when & then
        mockMvc.perform(put("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 존재하지_않는_회원_수정() throws Exception {
        // given
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("홍길동2");
        updateDto.setEmail("hong2@test.com");

        // when & then
        mockMvc.perform(put("/api/members/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 존재하지_않는_회원_삭제_테스트() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/members/{id}", 999L))
                .andExpect(status().isNotFound())  // 404 상태 코드 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다. ID: 999"))
                .andExpect(jsonPath("$.error").value("Member Not Found"))
                .andDo(print());
    }
}