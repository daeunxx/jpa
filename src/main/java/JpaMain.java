import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello-test");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            Member member = new Member();
//            member.setId(3L);
//            member.setName("daeun");
//
//            em.persist(member);

            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

//            em.remove(findMember);

            findMember.setName("haha");


            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
