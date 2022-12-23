package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "ORDERS")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_to_order_table"), nullable = false)
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        orderLineItems.setUpOrder(this);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, OrderLineItems.from(orderLineItems));
    }

    public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(null, orderTable, orderLineItems);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블은 비어있으면 안됨");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatusCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusCompletion() {
        if(Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("이미 완료된 주문");
        }
    }

    public void validateIfNotCompletionOrder() {
        if(!Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("완료되지 않은 주문");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
