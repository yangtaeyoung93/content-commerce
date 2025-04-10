package com.commerce.content.domain.order;

import com.commerce.content.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    private LocalDateTime orderDt;
    public void setUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }


    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderDt(LocalDateTime orderDt) {
        this.orderDt = orderDt;
    }

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderProduct> orderProduct;

    /**
     * 주문 생성
     */

    public static Order createOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDt(LocalDateTime.now());

        return order;
    }

    /**
     * 주문 취소
     */

    public void cancle(){
        this.setOrderStatus(OrderStatus.CANCLE);

    }

    /**
     * 주문 조회 ( 전체 주문 가격 조회)
     */

    public int getTotalPrice(){
        return orderProduct.stream().mapToInt(price -> getTotalPrice()).sum();
    }
}
