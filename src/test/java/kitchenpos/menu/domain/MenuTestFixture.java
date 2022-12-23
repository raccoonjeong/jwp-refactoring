package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuTestFixture {

    public static Menu generateMenu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroup, menuProducts);
    }

    public static Menu generateMenu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroup, MenuProducts.from(menuProducts));
    }

    public static MenuRequest generateMenuRequest(Name name, Price price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name.value(), price.value(), menuGroupId, menuProductRequests);
    }

    public static MenuRequest generateMenuRequest(Name name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name.value(), price, menuGroupId, menuProductRequests);
    }

    public static MenuRequest generateMenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }
}
