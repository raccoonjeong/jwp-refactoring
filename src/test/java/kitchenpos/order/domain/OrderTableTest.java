package kitchenpos.order.domain;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItem;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.order.domain.OrderTestFixture.generateOrder;
import static kitchenpos.order.domain.TableGroupTestFixture.generateTableGroup;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 도메인 테스트")
public class OrderTableTest {

    private Product 치킨버거;
    private MenuProduct 치킨버거상품;
    private MenuGroup 햄버거단품;
    private Menu 치킨버거단품;
    private OrderLineItem 치킨버거단품_주문_항목;

    @BeforeEach
    void setUp() {
        치킨버거 = generateProduct(2L, "치킨버거", BigDecimal.valueOf(4000L));
        치킨버거상품 = generateMenuProduct(치킨버거, 1L);
        햄버거단품 = generateMenuGroup(1L, "햄버거단품");
        치킨버거단품 = generateMenu(2L, "치킨버거단품", BigDecimal.valueOf(4000L), 햄버거단품, singletonList(치킨버거상품));
        치킨버거단품_주문_항목 = generateOrderLineItem(치킨버거단품, 2);
    }

    @DisplayName("주문 테이블의 그룹 상태를 해제한다.")
    @Test
    void ungroupOrderTable() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);
        OrderTable 주문테이블B = generateOrderTable(5, true);
        generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));

        // when
        주문테이블B.ungroup();

        // then
        assertAll(
                () -> assertThat(주문테이블B.getTableGroup()).isNull(),
                () -> assertThat(주문테이블A.isNotNullTableGroup()).isTrue(),
                () -> assertThat(주문테이블B.isNotNullTableGroup()).isFalse()
        );
    }

    @DisplayName("주문 테이블이 비어있으면 참이다.")
    @Test
    void orderTableIsEmpty() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);

        // when
        boolean isEmpty = 주문테이블A.isEmpty();

        // then
        assertThat(isEmpty).isTrue();
    }

    @DisplayName("주문 테이블에 단체를 지정하면, 비어있지 않다.")
    @Test
    void updateTableGroupMakeOrderTableNotEmpty() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);
        OrderTable 주문테이블B = generateOrderTable(5, true);

        // when
        generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));

        // then
        assertAll(
                () -> assertThat(주문테이블A.isEmpty()).isFalse(),
                () -> assertThat(주문테이블B.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블에 단체가 지정되어 있지 않으면, 해당 테이블에서 단체를 조회하면 null이 반환된다.")
    @Test
    void findTableGroupIdByNoTableGroupOrderTable() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);

        // when
        Long tableGroupId = 주문테이블A.findTableGroupId();

        // then
        assertThat(tableGroupId).isNull();
    }

    @DisplayName("완료되지 않은 주문이 없을 경우 주문 테이블의 상태를 바꿀 수 있다.")
    @Test
    void changeEmptyOrderTable() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);

        // when
        주문테이블A.changeEmpty(false, emptyList());

        // then
        assertThat(주문테이블A.isEmpty()).isFalse();
    }

    @DisplayName("완료되지 않은 주문이 있으면 주문 테이블의 상태를 바꿀 수 없다.")
    @Test
    void changeEmptyOrderTableThrowErrorWhenOrderIsNotCompletion() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, false);
        Order notCompletionOrder = generateOrder(주문테이블A,  OrderLineItems.from(singletonList(치킨버거단품_주문_항목)));

        // when & then
        assertThatThrownBy(() -> 주문테이블A.changeEmpty(true, singletonList(notCompletionOrder)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료되지 않은 주문");
    }
}
