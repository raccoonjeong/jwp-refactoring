package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
    private Order order;
    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu"))
    private Menu menu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return new OrderLineItem(null, null, menu, quantity);
    }

    public void setUpOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
