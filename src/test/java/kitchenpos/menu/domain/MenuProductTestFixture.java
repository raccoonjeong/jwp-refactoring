package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

public class MenuProductTestFixture {

    public static MenuProduct generateMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        return MenuProduct.of(seq, menu, product, quantity);
    }

    public static MenuProduct generateMenuProduct(Product product, long quantity) {
        return MenuProduct.of(product, quantity);
    }

    public static MenuProductRequest generateMenuProductRequest(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
