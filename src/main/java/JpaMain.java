import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Member member1 = new Member();
      member1.setUsername("A");

      Member member2 = new Member();
      member2.setUsername("B");

      Member member3 = new Member();
      member3.setUsername("C");

      Member member4 = new Member();
      member3.setUsername("C");

      System.out.println("=============");

      em.persist(member1);
      em.persist(member2);
      em.persist(member3);
      em.persist(member4);

      System.out.println("member1.getId() = " + member1.getId());
      System.out.println("member2.getId() = " + member2.getId());
      System.out.println("member3.getId() = " + member3.getId());

      System.out.println("=============");
      
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
