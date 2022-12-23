package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {}

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsIsEmpty(orderLineItems);
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    private void validateOrderLineItemsIsEmpty(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없음");
        }
    }

    public void setUpOrder(final Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.setUpOrder(order));
    }

    public List<OrderLineItem> unmodifiableOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
