package kitchenpos.menu.domain;

import static kitchenpos.common.domain.Price.ZERO_PRICE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> menuProducts) {
        validateMenuProductsIsEmpty(menuProducts);
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private void validateMenuProductsIsEmpty(List<MenuProduct> menuProducts) {
        if(menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품은 비어있을 수 없음");
        }
    }

    public Price totalPrice() {
        Price totalPrice = ZERO_PRICE;
        for(MenuProduct menuProduct: menuProducts) {
            totalPrice = totalPrice.add(menuProduct.totalPrice());
        }
        return totalPrice;
    }

    public void setUpMenu(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.setUpMenu(menu));
    }

    public List<MenuProduct> unmodifiableMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

}
