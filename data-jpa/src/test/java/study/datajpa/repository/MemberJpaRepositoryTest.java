package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

  @Autowired
  MemberJpaRepository memberJpaRepository;

  @Test
  public void testMember() {
    Member member = new Member("daeun");
    Member savedMember = memberJpaRepository.save(member);

    Member findMember = memberJpaRepository.find(savedMember.getId());

    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    assertThat(findMember).isEqualTo(member); //JPA entity 동일성 보장
  }

  @Test
  public void basicCRUD() {
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);

    // 단건 조회 검증
    Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
    Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);

    findMember1.setUsername("daeun");

    // 리스트 조회 검증
    List<Member> members = memberJpaRepository.findAll();
    assertThat(members.size()).isEqualTo(2);

    // 카운트 검증
    long count = memberJpaRepository.count();
    assertThat(count).isEqualTo(2);

    // 삭제 검증
    memberJpaRepository.delete(member1);
    memberJpaRepository.delete(member2);

    long deleteCount = memberJpaRepository.count();
    assertThat(deleteCount).isEqualTo(0);
  }

  @Test
  public void findByUsernameAndAgeGreaterThan() {
    Member member1 = new Member("tester1", 10);
    Member member2 = new Member("tester1", 20);
    Member savedMember1 = memberJpaRepository.save(member1);
    Member savedMember2 = memberJpaRepository.save(member2);

    // 단건 조회 검증
    Member findMember1 = memberJpaRepository.findById(savedMember1.getId()).get();
    Member findMember2 = memberJpaRepository.findById(savedMember2.getId()).get();

    System.out.println("findMember1 = " + findMember1);
    System.out.println("findMember2 = " + findMember2);

    List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("tester1", 15);

    assertThat(result.get(0).getUsername()).isEqualTo(findMember2.getUsername());
    assertThat(result.get(0).getAge()).isEqualTo(findMember2.getAge());
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  public void testNamedQuery() {
    Member member1 = new Member("tester1", 10);
    Member member2 = new Member("tester1", 20);
    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);

    List<Member> result = memberJpaRepository.findByUsername(member1.getUsername());
    Member findMember = result.get(0);

    assertThat(findMember).isEqualTo(member1);
  }

  @Test
  public void paging() {
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member2", 10));
    memberJpaRepository.save(new Member("member3", 10));
    memberJpaRepository.save(new Member("member4", 10));
    memberJpaRepository.save(new Member("member5", 10));

    int age = 10;
    int offset = 0;
    int limit = 3;

    List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
    long totalCount = memberJpaRepository.totalCount(age);

    assertThat(members.size()).isEqualTo(limit);
    assertThat(totalCount).isEqualTo(5);
  }

  @Test
  public void bulkUpdate() {
    // given
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member2", 12));
    memberJpaRepository.save(new Member("member3", 14));
    memberJpaRepository.save(new Member("member4", 20));
    memberJpaRepository.save(new Member("member5", 30));

    // when
    int resultCount = memberJpaRepository.bulkAgePlus(20);

    // then
    assertThat(resultCount).isEqualTo(2);
  }
}