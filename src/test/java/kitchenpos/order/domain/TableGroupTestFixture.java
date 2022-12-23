package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.dto.TableGroupRequest;

public class TableGroupTestFixture {

    public static TableGroup generateTableGroup(Long id, List<OrderTable> orderTables) {
        return TableGroup.of(id, orderTables);
    }

    public static TableGroup generateTableGroup(List<OrderTable> orderTables) {
        return TableGroup.of(null, orderTables);
    }

    public static TableGroupRequest generateTableGroupRequest(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }
}
