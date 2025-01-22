package com.example.spring_api.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long id) {
        super("회원을 찾을 수 없습니다. ID: " + id);
    }
}