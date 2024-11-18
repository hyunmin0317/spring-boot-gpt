package com.hyunmin.gpt.domain.member.service;

import com.hyunmin.gpt.global.common.entity.Member;
import com.hyunmin.gpt.global.common.repository.MemberRepository;
import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import com.hyunmin.gpt.domain.member.dto.MemberInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Page<MemberInfoResponseDto> findAll(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return MemberInfoResponseDto.from(memberPage);
    }

    public MemberInfoResponseDto findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberInfoResponseDto.from(member);
    }
}
