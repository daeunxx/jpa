import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;
import jpql.Member;

public class JpaMain {

  public static void main(String[] args) {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      for (int i = 0; i < 100; i++) {
        Member member = new Member();
        member.setUsername("member" + i);
        member.setAge(i);
        em.persist(member);
      }

      em.flush();
      em.clear();

      List<Member> members = em.createQuery("select m from Member m order by m.age desc",
              Member.class)
          .setFirstResult(1)
          .setMaxResults(10)
          .getResultList();

      System.out.println("members.size() = " + members.size());

      for (Member member : members) {
        System.out.println("member = " + member);
      }

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
