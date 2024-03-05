package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);
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
        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        findMember1.setUsername("daeun");

        // 리스트 조회 검증
        List<Member> members = memberJpaRepository.findAll();
        assertEquals(members.size(), 2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertEquals(count, 2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deleteCount = memberJpaRepository.count();
        assertEquals(deleteCount, 0);
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

        assertEquals(result.get(0).getUsername(), findMember2.getUsername());
        assertEquals(result.get(0).getAge(), findMember2.getAge());
        assertEquals(result.size(), 1);
    }

    @Test
    public void testNamedQuery() {
        Member member1 = new Member("tester1", 10);
        Member member2 = new Member("tester1", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsername(member1.getUsername());
        Member findMember = result.get(0);

        assertEquals(findMember, member1);
    }
}