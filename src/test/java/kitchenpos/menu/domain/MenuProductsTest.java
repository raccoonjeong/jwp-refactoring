package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 집합 관련 도메인 테스트")
public class MenuProductsTest {

    private Product 감자튀김;
    private Product 불고기버거;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000));
        불고기버거 = generateProduct(2L, "불고기버거", BigDecimal.valueOf(4000));
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김, 1);
        불고기버거상품 = generateMenuProduct(2L, null, 불고기버거, 2);
    }

    @DisplayName("메뉴 상품 집합을 생성한다.")
    @Test
    void createMenuProducts() {
        // when
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(감자튀김상품, 불고기버거상품));

        // then
        assertAll(
                () -> assertThat(menuProducts.unmodifiableMenuProducts()).hasSize(2),
                () -> assertThat(menuProducts.unmodifiableMenuProducts()).containsExactly(감자튀김상품, 불고기버거상품)
        );
    }

    @DisplayName("메뉴 상품 집합 내 메뉴 상품이 없으면 에러가 발생한다.")
    @Test
    void createMenuProductsThrowErrorWhenMenuProductIsEmpty() {
        // when & then
        assertThatThrownBy(() -> MenuProducts.from(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 상품은 비어있을 수 없음");
    }

    @DisplayName("메뉴 상품 집합 내 메뉴 상품들의 가격의 합을 구할 수 있다.")
    @Test
    void findTotalPrice() {
        // given
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(감자튀김상품, 불고기버거상품));
        Price 감자튀김가격 = 감자튀김상품.getProduct().getPrice();
        Price 불고기버거가격 = 불고기버거상품.getProduct().getPrice();

        // when
        Price totalPrice = menuProducts.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(감자튀김가격.multiply(감자튀김상품.getQuantity()).add(불고기버거가격.multiply(불고기버거상품.getQuantity())));
    }
}
