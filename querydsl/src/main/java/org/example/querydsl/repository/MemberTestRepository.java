package org.example.querydsl.repository;

import static org.example.querydsl.entity.QMember.member;
import static org.example.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.example.querydsl.dto.MemberSearchCond;
import org.example.querydsl.entity.Member;
import org.example.querydsl.repository.support.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class MemberTestRepository extends Querydsl4RepositorySupport {

  public MemberTestRepository() {
    super(Member.class);
  }

  public List<Member> basicSelect() {
    return select(member).from(member).fetch();
  }

  public List<Member> basicSelectFrom() {
    return selectFrom(member).fetch();
  }

  public Page<Member> searchPageByApplyPage(MemberSearchCond cond, Pageable pageable) {
    JPQLQuery<Member> query = selectFrom(member)
        .leftJoin(member.team, team)
        .where(usernameEq(cond.getUsername()),
            teamNameEq(cond.getTeamName()),
            ageGoe(cond.getAgeGoe()),
            ageLoe(cond.getAgeLoe()));

    JPQLQuery<Long> countQuery = select(member.count())
        .from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(cond.getUsername()),
            teamNameEq(cond.getTeamName()),
            ageGoe(cond.getAgeGoe()),
            ageLoe(cond.getAgeLoe()));

    List<Member> content = getQuerydsl().applyPagination(pageable, query).fetch();

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  public Page<Member> applyPagination(MemberSearchCond cond, Pageable pageable) {
    return applyPagination(pageable,
        contentQuery -> contentQuery
            .selectFrom(member)
            .leftJoin(member.team, team)
            .where(
                usernameEq(cond.getUsername()),
                teamNameEq(cond.getTeamName()),
                ageGoe(cond.getAgeGoe()),
                ageLoe(cond.getAgeLoe())));
  }

  public Page<Member> applyPagination2(MemberSearchCond cond, Pageable pageable) {
    return applyPagination(pageable,
        contentQuery -> contentQuery
            .selectFrom(member)
            .leftJoin(member.team, team)
            .where(
                usernameEq(cond.getUsername()),
                teamNameEq(cond.getTeamName()),
                ageGoe(cond.getAgeGoe()),
                ageLoe(cond.getAgeLoe())),
        countQuery -> countQuery
            .selectFrom(member).
            leftJoin(member.team, team)
            .where(
                usernameEq(cond.getUsername()),
                teamNameEq(cond.getTeamName()),
                ageGoe(cond.getAgeGoe()),
                ageLoe(cond.getAgeLoe())));
  }

  private BooleanExpression usernameEq(String username) {
    return hasText(username) ? member.username.eq(username) : null;
  }

  private BooleanExpression teamNameEq(String teamName) {
    return hasText(teamName) ? team.name.eq(teamName) : null;
  }

  private BooleanExpression ageGoe(Integer ageGoe) {
    return ageGoe != null ? member.age.goe(ageGoe) : null;
  }

  private BooleanExpression ageLoe(Integer ageLoe) {
    return ageLoe != null ? member.age.loe(ageLoe) : null;
  }
}
