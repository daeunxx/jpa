package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  TeamRepository teamRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  public void testMember() {

    System.out.println("memberRepository : " + memberRepository.getClass());

    Member member = new Member("tester");
    Member savedMember = memberRepository.save(member);

    Member findMember = memberRepository.findById(savedMember.getId()).get();

    Assertions.assertEquals(findMember.getId(), member.getId());
    Assertions.assertEquals(findMember.getUsername(), member.getUsername());
    Assertions.assertEquals(findMember, member);
  }

  @Test
  public void basicCRUD() {
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    memberRepository.save(member1);
    memberRepository.save(member2);

    // 단건 조회 검증
    Member findMember1 = memberRepository.findById(member1.getId()).get();
    Member findMember2 = memberRepository.findById(member2.getId()).get();
    assertEquals(findMember1, member1);
    assertEquals(findMember2, member2);

    findMember1.setUsername("daeun");

    // 리스트 조회 검증
    List<Member> members = memberRepository.findAll();
    assertEquals(members.size(), 2);

    // 카운트 검증
    long count = memberRepository.count();
    assertEquals(count, 2);

    // 삭제 검증
    memberRepository.delete(member1);
    memberRepository.delete(member2);

    long delteCount = memberRepository.count();
    assertEquals(delteCount, 0);
  }

  @Test
  public void findByUsernameAndAgeGreaterThan() {
    Member member1 = new Member("tester1", 10);
    Member member2 = new Member("tester1", 20);
    Member savedMember1 = memberRepository.save(member1);
    Member savedMember2 = memberRepository.save(member2);

    // 단건 조회 검증
    Member findMember1 = memberRepository.findById(savedMember1.getId()).get();
    Member findMember2 = memberRepository.findById(savedMember2.getId()).get();

    System.out.println("findMember1 = " + findMember1);
    System.out.println("findMember2 = " + findMember2);

    List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("tester1", 15);

    assertEquals(result.get(0).getUsername(), findMember2.getUsername());
    assertEquals(result.get(0).getAge(), findMember2.getAge());
    assertEquals(result.size(), 1);
  }

  @Test
  public void findHelloBy() {
    List<Member> members = memberRepository.findTop3HelloBy();
  }

  @Test
  public void testNamedQuery() {
    Member member1 = new Member("tester1", 10);
    Member member2 = new Member("tester2", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> members = memberRepository.findByUsername(member1.getUsername());
    assertEquals(member1, members.get(0));
  }

  @Test
  public void testQuery() {
    Member member1 = new Member("tester1", 10);
    Member member2 = new Member("tester1", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> members = memberRepository.findUser(member1.getUsername(), member1.getAge());
    assertEquals(member1, members.get(0));
  }

  @Test
  public void testUserNameList() {
    Member member1 = new Member("tester1", 10);
    Member member2 = new Member("tester1", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<String> usernames = memberRepository.findUsernameList();
    assertEquals(member1.getUsername(), usernames.get(0));
  }

  @Test
  public void findMemberDto() {
    Team team = new Team("teamA");
    teamRepository.save(team);

    Member member1 = new Member("member1", 10);
    member1.setTeam(team);
    memberRepository.save(member1);

    List<MemberDto> members = memberRepository.findMemberDto();
    members.forEach(System.out::println);
  }

  @Test
  public void findByNames() {
    Member member1 = new Member("tester1", 10);
    Member member2 = new Member("tester2", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> members = memberRepository.findByNames(
        Arrays.asList(member1.getUsername(), member2.getUsername()));

    members.forEach(System.out::println);
  }

  @Test
  public void returnType() {
    Member member1 = new Member("member1", 10);
    Member member2 = new Member("member1", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    // 컬렉션에 없는 값이 들어오면 빈 리스트가 반환됨(컬렉션은 null이 아님)
    List<Member> findMemberList = memberRepository.findListByUsername("sadfsa");
    System.out.println(findMemberList);

    // 객체는 없는 값이 들어오면 null
    Member findMember = memberRepository.findMemberByUsername("sadfsa");
    System.out.println(findMember);

    // Optional은 없는 값이 들어오면 Optional.empty
    // member1은 2개라 IncorrectResultSizeDataAccessException 노출
    Optional<Member> findOptionalMember = memberRepository.findOptionalByUsername("member1");
    System.out.println(findOptionalMember);
  }

  @Test
  public void pagingWithPage() {
    // given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 20));
    memberRepository.save(new Member("member4", 20));
    memberRepository.save(new Member("member5", 10));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

    // when
    Page<Member> page = memberRepository.findPageByAge(age, pageRequest);

    // then
    List<Member> content = page.getContent();
    long totalElements = page.getTotalElements();

    content.forEach(System.out::println);
    System.out.println("totalElements = " + totalElements);
    System.out.println("page.getTotalElements() = " + page.getTotalElements());
    System.out.println("page.getNumber() = " + page.getNumber());

    // 조회된 데이터 수
    assertEquals(content.size(), 3);

    // 전체 데이터 수
    assertEquals(page.getTotalElements(), 3);

    // 페이지 번호 : Page의 시작은 0부터
    assertEquals(page.getNumber(), 0);

    // 전체 페이지 수
    assertEquals(page.getTotalPages(), 1);
    assertEquals(page.isFirst(), true);
    assertEquals(page.hasNext(), false);
  }

  @Test
  public void pagingWithSlice() {
    // given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 10));
    memberRepository.save(new Member("member5", 10));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Direction.DESC, "username"));

    // when
    Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

    // then
    List<Member> content = page.getContent();

    content.forEach(System.out::println);

    // 현재 페이지에 나올 데이터 수
    System.out.println("page.getNumberOfElements() = " + page.getNumberOfElements());
    System.out.println("page.nextPageable() = " + page.nextPageable());

    assertEquals(content.size(), 2);
    assertEquals(page.getNumber(), 1);
    assertEquals(page.isFirst(), false);
    assertEquals(page.hasNext(), false);
  }

  @Test
  public void pagingWithList() {
    // given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 10));
    memberRepository.save(new Member("member5", 10));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

    // when
    List<Member> page = memberRepository.findByAge(age, pageRequest);

    // then
  }

  @Test
  public void pagingDividedCount() {
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 15));
    memberRepository.save(new Member("member4", 15));
    memberRepository.save(new Member("member5", 15));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

    Page<Member> members = memberRepository.findMembersByAge(age, pageRequest);
    Page<MemberDto> toMap = members.map(m -> new MemberDto(m.getId(), m.getUsername(), 10));

    toMap.forEach(System.out::println);
  }

  @Test
  public void bulkUpdate() {
    // given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 12));
    memberRepository.save(new Member("member3", 14));
    memberRepository.save(new Member("member4", 20));
    memberRepository.save(new Member("member5", 30));

    // when
    int resultCount = memberRepository.bulkAgePlus(20);
//    em.clear();

    // 조회해보면 bulk 연산이 되지 않은 상태
    // @Modifying(clearAutomatically = true)를 사용하면 객체에 반영
    List<Member> result = memberRepository.findByUsername("member5");
    Member member = result.get(0);
    System.out.println("member = " + member);

    // then
    assertEquals(resultCount, 2);
  }

  @Test
  public void findMemberLazy() {
    // given
    // member1 -> teamA
    // member2 -> teamB

    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    teamRepository.save(teamA);
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamA);
    memberRepository.save(member1);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    // when
    // select Member
    List<Member> members = memberRepository.findAll();

    members.forEach(m -> {
      System.out.println("m.getUsername() = " + m.getUsername());
//      System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
      System.out.println("m.getTeam().getName() = " + m.getTeam().getName());

      // members에서 프록시로 가져왔기 때문에 바로 위에서 초기화되어도 프록시 클래스로 조회됨
      System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
    });
  }

  @Test
  public void findMemberFetchJoin() {
    // given
    // member1 -> teamA
    // member2 -> teamB

    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    teamRepository.save(teamA);
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamB);
    memberRepository.save(member1);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    // when
    List<Member> members = memberRepository.findMemberFetchJoin();

    members.forEach(m -> {
      System.out.println("m.getUsername() = " + m.getUsername());
      System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
      System.out.println("m.getTeam().getName() = " + m.getTeam().getName());
    });

    // 지연로딩 여부 확인
    boolean initialized = Hibernate.isInitialized(members.get(0).getTeam());
    System.out.println("initialized = " + initialized);
  }

  @Test
  public void findMemberGraph() {
    // given
    // member1 -> teamA
    // member2 -> teamB

    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    teamRepository.save(teamA);
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamB);
    memberRepository.save(member1);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    // when
    List<Member> members = memberRepository.findEntityGraphByUsername("member1");

    members.forEach(m -> {
      System.out.println("m.getUsername() = " + m.getUsername());
      System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
      System.out.println("m.getTeam().getName() = " + m.getTeam().getName());
    });
  }

  @Test
  public void queryHint() {
    // given
    Member member1 = memberRepository.save(new Member("member1", 10));
    em.flush();
    em.clear();

    // when
    Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
    findMember.setUsername("member2");

    em.flush(); // update query 실행x
    em.clear(); // 객체만 변경 but DB는 변경되지 않음

    Optional<Member> findMemberById = memberRepository.findById(member1.getId());
    System.out.println("findMemberById = " + findMemberById);
    System.out.println("findMember = " + findMember);
  }

  @Test
  public void lock() {
    // given
    Member member1 = memberRepository.save(new Member("member1", 10));
    em.flush();
    em.clear();

    // when
    memberRepository.findLockByUsername(member1.getUsername());
  }

  @Test
  public void callCustom() {
    List<Member> result = memberRepository.findMemberCustom();
  }

  @Test
  public void teamProxy() {
    // given
    Team team = new Team("team1");
    teamRepository.save(team);

    Member member = new Member("member1", 10, team);
    memberRepository.save(member);

    em.flush();
    em.clear();

    // when
    Member findMember = memberRepository.findMemberByUsername(member.getUsername());
    System.out.println("findMember = " + findMember);

    System.out.println("findMember.getTeam().getClass() = " + findMember.getTeam().getClass());

    // 프록시 객체가 먼저 조회된 상태
    Team findTeam = findMember.getTeam();
    Team saveTeam = findMember.getTeam();

    // 기본키는 이미 갖고 있기 때문에 실제 엔티티를 조회하지 않음
    System.out.println("findTeam.getId() = " + findTeam.getId());

    // 출력을 찍으면 team 조회 쿼리가 호출되면서 실제 team 엔티티를 가져옴
    System.out.println("findTeam = " + findTeam);

    // 이미 프록시 객체가 이전에 조회되었기 프록시 객체가 반환됨
    System.out.println("saveTeam.getClass() = " + saveTeam.getClass());
    System.out.println("findTeam == saveTeam " + findTeam.equals(saveTeam));
  }

  @Test
  public void memberProxy() {
    // given
    Member member = new Member("member1", 10);
    memberRepository.save(member);

    em.flush();
    em.clear();

    // when
    // 바로 위의 테스트와 반대의 상황
    // 쿼리에서 불러온 엔티티와 프록시 객체 비교
    Member findMember = em.find(Member.class, member.getId());
    System.out.println("findMember.getClass() = " + findMember.getClass());

    // 프록시 객체이나 실제 엔티티 클래스 반환
    Member refMember = em.getReference(Member.class, member.getId());
    System.out.println("refMember.getClass() = " + refMember.getClass());

  }

  @Test
  public void detachProxy() {
    // given
    Member member = new Member("member1", 10);
    memberRepository.save(member);

    em.flush();
    em.clear();

    // when
    // 프록시 객체 생성
    Member refMember = em.getReference(Member.class, member.getId());
    System.out.println("refMember.getClass() = " + refMember.getClass());

    // 프록시 객체 준영속화 -> 오류 발생
//    em.detach(refMember);

    // 영속성 컨텍스트 clear -> 오류 발생
//    em.clear();

    // 영속성 컨텍스트 종료 -> 정상
    em.close();

    // 프록시 객체 초기화
    String username = refMember.getUsername();
    System.out.println("Proxy username = " + username);
  }

  @Test
  public void queryByExample() {

    // given
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    member1.setTeam(teamA);
    em.persist(member1);
    em.persist(member2);

    em.flush();
    em.clear();

    // when
    Member member = new Member("member1");
    Team team = new Team("teamA");
    member.setTeam(team);

    // 문제점 : inner join만 가능
    ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
    Example<Member> example = Example.of(member, matcher);

    List<Member> result = memberRepository.findAll(example);

    Assertions.assertEquals(result.get(0).getUsername(), member1.getUsername());
  }

  @Test
  public void projections() throws IllegalArgumentException {
    // given
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    member1.setTeam(teamA);
    em.persist(member1);
    em.persist(member2);

    em.flush();
    em.clear();

    // when
    List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("member1", NestedClosedProjections.class);

    result.forEach(r -> {
      System.out.println("r.getUsername() = " + r.getUsername());
      System.out.println("r.getTeam() = " + r.getTeam());
    });
  }
}