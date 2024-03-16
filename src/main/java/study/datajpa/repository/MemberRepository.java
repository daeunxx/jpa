package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

  List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

  List<Member> findTop3HelloBy();

  //    @Query(name = "Member.findByUsername")
//    List<Member> findByUsername(@Param("username") String username);
  List<Member> findByUsername(String username);

  @Query("select m from Member m where m.username = :username and m.age = :age")
  List<Member> findUser(@Param("username") String username, @Param("age") int age);

  @Query("select m.username from Member m")
  List<String> findUsernameList();

  @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
  List<MemberDto> findMemberDto();

  @Query("select m from Member m where m.username in :names")
  List<Member> findByNames(@Param("names") List<String> names);

  List<Member> findListByUsername(String username); // 컬렉션

  Member findMemberByUsername(String username);   // 단건

  Optional<Member> findOptionalByUsername(String usrename);

  Page<Member> findPageByAge(int age, Pageable pageable);

  Slice<Member> findSliceByAge(int age, Pageable pageable);

  List<Member> findByAge(int age, Pageable pageable);

  @Query(value = "select m from Member m left outer join m.team t where m.age = :age", countQuery = "select count(m) from Member m")
  Page<Member> findMembersByAge(@Param("age") int age, Pageable pageable);

  @Modifying(clearAutomatically = true)
//  @Modifying
  @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
  int bulkAgePlus(@Param("age") int age);

  @Query("select m from Member m left join fetch m.team")
  List<Member> findMemberFetchJoin();

  @Override
  @EntityGraph(attributePaths = {"team"})
  List<Member> findAll();

  @EntityGraph(attributePaths = {"team"})
  @Query("select m from Member m")
  List<Member> findMemberEntityGraph();

  @EntityGraph("Member.all")
  List<Member> findEntityGraphByUsername(String username);

  @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
  Member findReadOnlyByUsername(String username);

  @QueryHints(value = {
      @QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
  Page<Member> findByUsername(String username, Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_READ)
  List<Member> findLockByUsername(String username);
}
