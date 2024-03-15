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
      Team team = new Team();
      team.setName("team1");
      em.persist(team);

      Member member = new Member();
      member.setUsername("daeun");
      member.setAge(10);
      member.setTeam(team);
      member.setType(RoleType.ADMIN);
      em.persist(member);

      em.flush();
      em.clear();

      String query = "select nullif(m.username, 'daeun') from Member m";
      List<String> result = em.createQuery(query, String.class).getResultList();
      result.forEach(System.out::println);

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
