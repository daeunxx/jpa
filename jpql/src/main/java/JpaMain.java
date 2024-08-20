import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpql.Member;
import jpql.Team;

public class JpaMain {

  public static void main(String[] args) {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Team team1 = new Team();
      team1.setName("team1");
      em.persist(team1);

      Team team2 = new Team();
      team2.setName("team2");
      em.persist(team2);

      Member member1 = new Member();
      member1.setUsername("daeun");
      member1.setAge(10);
      member1.setTeam(team1);
      em.persist(member1);

      Member member2 = new Member();
      member2.setUsername("daeun");
      member2.setAge(10);
      member2.setTeam(team2);
      em.persist(member2);

      Member meber3 = new Member();
      meber3.setUsername("daeun3");
      meber3.setAge(10);
      meber3.setTeam(team2);
      em.persist(meber3);

      //em.flush();
      //em.clear();

      int resultCount = em.createQuery("update Member m set m.age = 20")
          .executeUpdate();

      Member findMember = em.find(Member.class, member1.getId());
      System.out.println("resultCount = " + resultCount);
      System.out.println("findMember.getAge() = " + findMember.getAge());

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
