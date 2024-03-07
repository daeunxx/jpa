package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository{

  @PersistenceContext
  EntityManager em;

  @Override
  public List<Member> findMemberCustom() {
    return em.createQuery("select m from Member m")
        .getResultList();
  }
}
