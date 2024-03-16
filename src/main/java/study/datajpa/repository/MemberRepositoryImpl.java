package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

  private final EntityManager em;

  // MemberRepository가 MemberCustomRepository를 상속받았기 때문에
  // MemberResposityImpl 이름을 지킨 클래스에 구현해야함
  @Override
  public List<Member> findMemberCustom() {
    return em.createQuery("select m from Member m")
        .getResultList();
  }
}
