package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    private final OrderQueryRepository orderQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5JakartaModule 등록
     * - 컨트롤러 단에서 프록시 객체 초기화하여 LAZY = null 처리
     * - 양방향 문제 발생 => @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        orders.forEach(order -> {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(item -> item.getItem().getName());
        });

        return orders;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 지연 로딩으로 너무 많은 SQL 실행되는 문제
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - 컬렉션 fetch join으로 인해 페이징 불가
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     * V3.1. 엔티티를 조회해서 DTO로 변환
     * - xToOne 관계만 우선 모두 fetch join으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size 로 최적화
     * - 페이징 가능
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     * V4. JPA에서 DTO로 바로 조회 | 컬렉션 N번 조회
     * - query : 루트 1번, 컬렉션 N번
     * - 컬렉션은 별도로 조회
     * - 단건 조회에서 주로 사용하는 방식
     * - 페이징 가능
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    /**
     * V5. JPA에서 DTO 바로 조회 | 컬렉션 1번 조회
     * - query : 루트 1번, 컬렉션 1번
     * - 데이터를 한꺼번에 처리할 때 주로 사용하는 방식
     * - 페이징 가능
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> orderV5() {
        return orderQueryRepository.findAllByDtoOptimization();
    }

    /**
     * V6. JPA에서 DTO 바로 조회
     * - 1 query
     * - 페이징 불가
     * @return
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> orderV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByFlat();

        Map<OrderQueryDto, List<OrderItemQueryDto>> orderItemMap = flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(i -> new OrderItemQueryDto(i.getOrderId(), i.getItemName(), i.getOrderPrice(), i.getCount()), toList())));

        return orderItemMap.entrySet().stream()
                .map(m -> new OrderQueryDto(m.getKey().getOrderId(), m.getKey().getName(), m.getKey().getOrderDate(), m.getKey().getOrderStatus(), m.getKey().getAddress(), m.getValue()))
                .collect(toList());
    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                                                .map(OrderItemDto::new)
                                                .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int price;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            price = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
