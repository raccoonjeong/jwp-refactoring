package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OrderService 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    public MenuDao menuDao;
    @Mock
    public OrderDao orderDao;
    @Mock
    public OrderLineItemDao orderLineItemDao;
    @Mock
    public OrderTableDao orderTableDao;

    @InjectMocks
    public OrderService orderService;

    private Product 스테이크;
    private Product 파스타;
    private Product 샐러드;
    private MenuGroup 양식;
    private MenuProduct 스테이크상품;
    private MenuProduct 파스타상품;
    private MenuProduct 샐러드상품;
    private Menu 스테이크정식;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderLineItem 스테이크정식주문;

    @BeforeEach
    void setUp() {
        스테이크 = new Product(1L, "스테이크", BigDecimal.valueOf(10_000));
        파스타 = new Product(2L, "파스타", BigDecimal.valueOf(8_000));
        샐러드 = new Product(3L, "샐러드", BigDecimal.valueOf(5_000));
        양식 = new MenuGroup(1L, "양식");

        스테이크정식 = new Menu(1L, "스테이크정식", BigDecimal.valueOf(23_000), 양식.getId(), new ArrayList<>());

        스테이크상품 = new MenuProduct(1L, 스테이크정식.getId(), 스테이크.getId(), 1);
        파스타상품 = new MenuProduct(2L, 스테이크정식.getId(), 파스타.getId(), 1);
        샐러드상품 = new MenuProduct(3L, 스테이크정식.getId(), 샐러드.getId(), 1);

        스테이크정식.setMenuProducts(Arrays.asList(스테이크상품, 파스타상품, 샐러드상품));

        주문테이블 = new OrderTable(1L, null, 0, false);
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        스테이크정식주문 = new OrderLineItem(1L, 주문.getId(), 스테이크정식.getId(), 1);
        주문.setOrderLineItems(Arrays.asList(스테이크정식주문));
    }
    

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
            .stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(주문테이블));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(스테이크정식주문)).thenReturn(스테이크정식주문);

        // when
        Order result = orderService.create(주문);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(주문.getId()),
            () -> assertThat(result.getOrderStatus()).isEqualTo(주문.getOrderStatus())
        );
    }

    @DisplayName("주문메뉴가 비어있으면 예외가 발생한다.")
    @Test
    void emptyOrderLineItemsException() {
        // given
        주문 = new Order(1L, 주문테이블.getId(), null, LocalDateTime.now(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문메뉴가 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistOrderLineItemsException() {
        // given
        when(menuDao.countByIdIn(anyList())).thenReturn(10L);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistOrderTableException() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
            .stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void emptyOrderTableException() {
        // given
        주문테이블.setEmpty(true);
        List<Long> menuIds = 주문.getOrderLineItems()
            .stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(주문테이블));

        // when & then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 조회할 수 있다.")
    @Test
    void findAllOrder() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(주문));
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        // when
        List<Order> results = orderService.list();

        // then
        assertAll(
            () -> assertThat(results).hasSize(1),
            () -> assertThat(results.get(0).getId()).isEqualTo(주문.getId()),
            () -> assertThat(results.get(0).getOrderStatus()).isEqualTo(주문.getOrderStatus())
        );
    }

    @DisplayName("주문 상태를 수정할 수 있다.")
    @Test
    void updateOrderStatus() {
        // given
        String expectedStatus = OrderStatus.MEAL.name();
        Order updatedOrder = new Order(
            주문.getId(),
            주문.getOrderTableId(),
            expectedStatus,
            주문.getOrderedTime(),
            주문.getOrderLineItems()
        );
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());

        // when
        Order result = orderService.changeOrderStatus(주문.getId(), updatedOrder);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(주문.getId()),
            () -> assertThat(result.getOrderStatus()).isEqualTo(expectedStatus)
        );
    }

    @DisplayName("등록되지 않은 주문 상태를 수정하면 예외가 발생한다.")
    @Test
    void notExistOrderUpdateStatusException() {
        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(3L, 주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료된 주문 상태를 수정하면 예외가 발생한다.")
    @Test
    void updateCompletionOrderStatusException() {
        // given
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order updatedOrder = new Order(
            주문.getId(),
            주문.getOrderTableId(),
            OrderStatus.MEAL.name(),
            주문.getOrderedTime(),
            주문.getOrderLineItems()
        );

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), updatedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

