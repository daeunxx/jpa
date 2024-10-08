package org.example.querydsl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.querydsl.entity.Member;
import org.example.querydsl.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

  private final InitMemberService initMemberService;

  //@PostConstruct와 @Transactional은 라이프 사이클이 서로 다르기 때문에 분리해야함
  @PostConstruct
  public void init() {
    initMemberService.init();
  }

  @Component
  static class InitMemberService {

    @Autowired
    private EntityManager em;

    @Transactional
    public void init() {
      Team teamA = new Team("teamA");
      Team teamB = new Team("teamB");
      em.persist(teamA);
      em.persist(teamB);

      for (int i = 0; i < 100; i++) {
        Team selectedTeam = (i % 2 == 0) ? teamA : teamB;
        em.persist(new Member("member" + i, i, selectedTeam));
      }
    }
  }
}
