import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.hibernate.Hibernate;

public class JpaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      Member member1 = new Member();
      member1.setUsername("daeun");
      
      em.persist(member1);

      Member member2 = new Member();
      member2.setUsername("daeun2");

      em.persist(member2);

      em.flush();
      em.clear();

      Member refMember = em.getReference(Member.class, member1.getId());
      System.out.println("refMember.getClass() = " + refMember.getClass());
      System.out.println("isLoded : " + emf.getPersistenceUnitUtil().isLoaded(refMember));
      Hibernate.initialize(refMember);
//      em.close();
//      em.clear();
//      em.detach(refMember);

//      System.out.println("refMember.getUsername() = " + refMember.getUsername());

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
