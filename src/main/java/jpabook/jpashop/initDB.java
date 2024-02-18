package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "마포구", "11111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 5000, 50);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 10000, 100);
            em.persist(book2);

            Delivery delivery = createDelivery(member);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 5000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 10000, 2);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "경기", "시흥시", "22222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 15000, 200);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

            Delivery delivery = createDelivery(member);

            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 4);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 15000, 3);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}
