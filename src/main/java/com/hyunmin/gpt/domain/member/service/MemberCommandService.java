package com.hyunmin.gpt.domain.member.service;

import com.hyunmin.gpt.global.common.entity.Member;
import com.hyunmin.gpt.global.common.repository.MemberRepository;
import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import com.hyunmin.gpt.domain.member.dto.ChangePasswordRequestDto;
import com.hyunmin.gpt.domain.member.dto.MemberInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberInfoResponseDto changePassword(Long id, ChangePasswordRequestDto requestDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        String newEncodedPw = passwordEncoder.encode(requestDto.password());
        member.changePassword(newEncodedPw);
        return MemberInfoResponseDto.from(member);
    }
}
