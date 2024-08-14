package org.example.querydsl.repository;

import java.util.List;
import org.example.querydsl.dto.MemberSearchCond;
import org.example.querydsl.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

  List<MemberTeamDto> search(MemberSearchCond cond);
  Page<MemberTeamDto> searchPage(MemberSearchCond cond, Pageable pageable);
}
