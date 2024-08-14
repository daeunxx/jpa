package org.example.querydsl.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.querydsl.dto.MemberSearchCond;
import org.example.querydsl.dto.MemberTeamDto;
import org.example.querydsl.repository.MemberJpaRepository;
import org.example.querydsl.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberJpaRepository memberJpaRepository;
  private final MemberRepository memberRepository;

  @GetMapping("/v1/members")
  public List<MemberTeamDto> searchMemberV1(MemberSearchCond cond) {
    return memberJpaRepository.searchByWhereParam(cond);
  }

  @GetMapping("/v2/members")
  public Page<MemberTeamDto> searchMemberV2(MemberSearchCond cond, Pageable pageable) {
    return memberRepository.searchPage(cond, pageable);
  }
}
