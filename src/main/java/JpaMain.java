import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

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
      member1.setTeam(team1);
      em.persist(member1);

      Member member2 = new Member();
      member2.setUsername("daeun2");
      member2.setTeam(team2);
      em.persist(member2);

      em.flush();
      em.clear();

      List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
          .getResultList();

      tx.commit();
    } catch (Exception e) {
      e.printStackTrace();
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }

  private static void printMember(Member member) {
    System.out.println("member = " + member.getUsername());
  }

  private static void printMemberAndTeam(Member member) {
    String username = member.getUsername();
    System.out.println("username = " + username);

    Team team = member.getTeam();
    System.out.println("team = " + team);
  }
}
