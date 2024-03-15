import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;
import jpql.Address;
import jpql.Member;
import jpql.MemberDto;
import jpql.Team;

public class JpaMain {

  public static void main(String[] args) {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Member member = new Member();
      member.setUsername("daeun");
      member.setAge(10);
      em.persist(member);

      em.flush();
      em.clear();

      List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
      Member findMember = members.get(0);
      findMember.setAge(20);

      List<Team> teams = em.createQuery("select t from Member m join m.team t", Team.class)
          .getResultList();

      em.createQuery("select o.address from Order o", Address.class).getResultList();

      List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")
          .getResultList();

      Object[] result = resultList.get(0);
      System.out.println("result[0] = " + result[0]);
      System.out.println("result[1] = " + result[1]);

      List<MemberDto> memberDtoList = em.createQuery(
              "select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
          .getResultList();

      MemberDto memberDto = memberDtoList.get(0);
      System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
      System.out.println("memberDto.getAge() = " + memberDto.getAge());

      tx.commit();

    } catch (Exception e) {
      e.printStackTrace();
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}
