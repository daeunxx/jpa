package jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    @JsonIgnore
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders();

        orders.forEach(order -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(order.getOrderId());
            order.setOrderItems(orderItems);
        });

        return orders;
    }

    public List<OrderQueryDto> findAllByDtoOptimization() {
        List<OrderQueryDto> orders = findOrders();
        List<Long> orderIds = toOrderIds(orders);
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemsMap(orderIds);

        orders.forEach(order -> order.setOrderItems(orderItemMap.get(order.getOrderId())));

        return orders;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemsMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                                                                    .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> orders) {
        List<Long> orderIds = orders.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(Collectors.toList());
        return orderIds;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                    "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderFlatDto> findAllByFlat() {
        return em.createQuery(
                "select new " +
                        " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
