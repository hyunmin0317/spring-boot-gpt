package com.hyunmin.gpt.domain.member.controller;

import com.hyunmin.gpt.domain.member.dto.ChangePasswordRequestDto;
import com.hyunmin.gpt.domain.member.dto.MemberInfoResponseDto;
import com.hyunmin.gpt.domain.member.service.MemberCommandService;
import com.hyunmin.gpt.domain.member.service.MemberQueryService;
import com.hyunmin.gpt.global.security.annotation.AuthMember;
import com.hyunmin.gpt.global.validation.annotation.PermissionCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping
    public ResponseEntity<Page<MemberInfoResponseDto>> findAll(@ParameterObject Pageable pageable) {
        Page<MemberInfoResponseDto> responseDtoPage = memberQueryService.findAll(pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoResponseDto> findById(@PermissionCheck @PathVariable Long id) {
        MemberInfoResponseDto responseDto = memberQueryService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(@AuthMember Long memberId) {
        MemberInfoResponseDto responseDto = memberQueryService.findById(memberId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@AuthMember Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<MemberInfoResponseDto> changePassword(@AuthMember Long memberId,
                                                                @RequestBody @Valid ChangePasswordRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.changePassword(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
