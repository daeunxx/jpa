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
      Movie movie = new Movie();
      movie.setDirector("a");
      movie.setActor("bbb");
      movie.setName("dune");
      movie.setPrice(1000);

      em.persist(movie);

      em.flush();
      em.clear();

      Item finditem = em.find(Item.class, movie.getId());
      System.out.println("finditem = " + finditem);

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
