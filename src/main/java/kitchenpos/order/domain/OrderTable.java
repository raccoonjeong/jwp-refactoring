package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Empty;
import kitchenpos.common.domain.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    public OrderTable() {
    }

    private OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.valueOf(empty);
    }

    public static OrderTable of(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean isNotNullTableGroup() {
        return tableGroup != null;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = Empty.IS_NOT_EMPTY;
    }

    public void changeEmpty(boolean isEmpty, List<Order> orders) {
        validateHasTableGroup();
        orders.forEach(Order::validateIfNotCompletionOrder);
        this.empty = Empty.valueOf(isEmpty);
    }

    private void validateHasTableGroup() {
        if(isNotNullTableGroup()) {
            throw new IllegalArgumentException("단체 그룹 지정되어 있음");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateOrderTableIsNotEmpty();
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    private void validateOrderTableIsNotEmpty() {
        if(empty.isEmpty()) {
            throw new IllegalArgumentException("비어있는 주문 테이블");
        }
    }

    public Long findTableGroupId() {
        if(tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}