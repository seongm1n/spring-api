package com.example.spring_api.controller;

import com.example.spring_api.model.Member;
import com.example.spring_api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> createMember(@RequestBody Member member) {
        Long id = memberService.join(member);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<List<Member>> getMembers() {
        List<Member> members = memberService.findMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        Member member = memberService.findOne(id);
        return ResponseEntity.ok(member);
    }
}