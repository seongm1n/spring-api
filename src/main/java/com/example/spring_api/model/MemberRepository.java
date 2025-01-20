package com.example.spring_api.model;

import com.example.spring_api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    List<Member> findByNameContainingOrEmailContaining(String name, String email);
}