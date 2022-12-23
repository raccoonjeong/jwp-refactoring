package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    private static final int MIN_SIZE = 2;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    private OrderTables(List<OrderTable> orderTables) {
        validateOrderTableIsEmpty(orderTables);
        validateOrderTableSize(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    private void validateOrderTableIsEmpty(List<OrderTable> orderTables) {
        if(CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블 집합은 비어있을 수 없음");
        }
    }

    private void validateOrderTableSize(List<OrderTable> orderTables) {
        if(orderTables.isEmpty() || orderTables.size() < MIN_SIZE) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상여야함");
        }
    }

    public boolean anyHasGroupId() {
        return orderTables.stream()
            .anyMatch(OrderTable::isNotNullTableGroup);
    }

    public void updateTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup));
    }

    public void ungroupOrderTables() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> unmodifiableOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}
