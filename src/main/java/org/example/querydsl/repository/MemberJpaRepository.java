package org.example.querydsl.repository;

import static org.example.querydsl.entity.QMember.*;
import static org.example.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.*;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.example.querydsl.dto.MemberSearchCond;
import org.example.querydsl.dto.MemberTeamDto;
import org.example.querydsl.dto.QMemberTeamDto;
import org.example.querydsl.entity.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJpaRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public MemberJpaRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  public void save(Member member) {
    em.persist(member);
  }

  public Optional<Member> findById(Long id) {
    Member findMember = em.find(Member.class, id);
    return Optional.ofNullable(findMember);
  }

  public List<Member> findAll() {
    return em.createQuery("select m from Member m", Member.class)
        .getResultList();
  }

  public List<Member> findAll_querydsl() {
    return queryFactory
        .selectFrom(member)
        .fetch();
  }

  public List<Member> findByUsername(String username) {
    return em.createQuery("select m from Member m where m.username = :username", Member.class)
        .setParameter("username", username)
        .getResultList();
  }

  public List<Member> findByUsername_querydsl(String username) {
    return queryFactory
        .selectFrom(member)
        .where(member.username.eq(username))
        .fetch();
  }

  public List<MemberTeamDto> searchByBuilder(MemberSearchCond cond) {
    BooleanBuilder builder = new BooleanBuilder();

    if (hasText(cond.getUsername())) {
      builder.and(member.username.eq(cond.getUsername()));
    }

    if (hasText(cond.getTeamName())) {
      builder.and(team.name.eq(cond.getTeamName()));
    }

    if (cond.getAgeGoe() != null) {
      builder.and(member.age.goe(cond.getAgeGoe()));
    }

    if (cond.getAgeLoe() != null) {
      builder.and(member.age.loe(cond.getAgeLoe()));
    }

    return queryFactory
        .select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.username,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName"))
        )
        .from(member)
        .leftJoin(member.team, team)
        .where(builder)
        .fetch();
  }

  public List<MemberTeamDto> searchByWhereParam(MemberSearchCond cond) {
    return queryFactory
        .select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.username,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName"))
        )
        .from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(cond.getUsername()),
            teamNameEq(cond.getTeamName()),
            ageGoe(cond.getAgeGoe()),
            ageLoe(cond.getAgeLoe()))
        .fetch();
  }

  public List<Member> searchByWhereParamMember(MemberSearchCond cond) {
    return queryFactory
        .select(member)
        .from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(cond.getUsername()),
            teamNameEq(cond.getTeamName()),
            ageBetween(cond.getAgeLoe(), cond.getAgeGoe()))
        .fetch();
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

  private BooleanExpression ageBetween(int ageLoe, int ageGoe) {
    return ageLoe(ageLoe).and(ageGoe(ageGoe));
  }
}
