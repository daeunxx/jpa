package org.example.querydsl.repository;

import java.util.List;
import org.example.querydsl.dto.MemberSearchCond;
import org.example.querydsl.dto.MemberTeamDto;

public interface MemberRepositoryCustom {

  List<MemberTeamDto> search(MemberSearchCond cond);
}
