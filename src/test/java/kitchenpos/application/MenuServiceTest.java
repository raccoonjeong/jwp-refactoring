package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuService 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;
    
    @Mock
    private ProductDao productDao;
        
    @InjectMocks
    private MenuService menuService;
    
    private Product 스테이크;
    private Product 파스타;
    private Product 샐러드;
    private MenuGroup 양식;
    private MenuProduct 스테이크상품;
    private MenuProduct 파스타상품;
    private MenuProduct 샐러드상품;
    private Menu 스테이크정식;

    @BeforeEach
    void setUp() {
        스테이크 = new Product(1L, "스테이크", new Price(10_000));
        파스타 = new Product(2L, "파스타", new Price(8_000));
        샐러드 = new Product(3L, "샐러드", new Price(5_000));
        양식 = new MenuGroup(1L, "양식");

        스테이크정식 = new Menu(1L, "스테이크정식", new Price(23_000), 양식.getId(), new ArrayList<>());

        스테이크상품 = new MenuProduct(1L, 스테이크정식.getId(), 스테이크.getId(), 1);
        파스타상품 = new MenuProduct(2L, 스테이크정식.getId(), 파스타.getId(), 1);
        샐러드상품 = new MenuProduct(3L, 스테이크정식.getId(), 샐러드.getId(), 1);

        스테이크정식.setMenuProducts(Arrays.asList(스테이크상품, 파스타상품, 샐러드상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        when(menuGroupDao.existsById(스테이크정식.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(스테이크상품.getProductId())).thenReturn(Optional.of(스테이크));
        when(productDao.findById(파스타상품.getProductId())).thenReturn(Optional.of(파스타));
        when(productDao.findById(샐러드상품.getProductId())).thenReturn(Optional.of(샐러드));
        when(menuDao.save(스테이크정식)).thenReturn(스테이크정식);
        when(menuProductDao.save(스테이크상품)).thenReturn(스테이크상품);
        when(menuProductDao.save(파스타상품)).thenReturn(파스타상품);
        when(menuProductDao.save(샐러드상품)).thenReturn(샐러드상품);

        // when
        Menu result = menuService.create(스테이크정식);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(스테이크정식.getId()),
            () -> assertThat(result.getName()).isEqualTo(스테이크정식.getName())
        );
    }

    @DisplayName("메뉴 가격이 null이면 예외가 발생한다.")
    @Test
    void createNullPriceMenuException() {
        // given
        스테이크정식 = new Menu(1L, "스테이크정식", null, 1L, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(스테이크정식))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -20000})
    void createUnderZeroPriceMenuException(int input) {
        // given
        스테이크정식 = new Menu(1L, "스테이크정식", new Price(input), 1L, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(스테이크정식))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴그룹이 없을 경우 예외가 발생한다.")
    @Test
    void notExistMenuGroupException() {
        // given
        스테이크정식 = new Menu(1L, "스테이크정식", new Price(1_000), 1L, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(스테이크정식))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 상품이 없을 경우 예외가 발생한다.")
    @Test
    void notExistProductException() {
        // given
        스테이크정식 = new Menu(1L, "스테이크정식", new Price(1_000), 1L, new ArrayList<>());
        when(menuGroupDao.existsById(스테이크정식.getMenuGroupId())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> menuService.create(스테이크정식))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴에 포함된 상품 가격의 합보다 작아야 한다.")
    @Test
    void menuPriceException() {
        // given
        스테이크정식.setPrice(new Price(200_000));
        when(menuGroupDao.existsById(스테이크정식.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(스테이크상품.getProductId())).thenReturn(Optional.of(스테이크));
        when(productDao.findById(파스타상품.getProductId())).thenReturn(Optional.of(파스타));
        when(productDao.findById(샐러드상품.getProductId())).thenReturn(Optional.of(샐러드));

        // when & then
        assertThatThrownBy(() -> menuService.create(스테이크정식))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void findAllMenu() {
        // given
        List<Menu> menus = Arrays.asList(스테이크정식);
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(스테이크정식.getId())).thenReturn(Arrays.asList(스테이크상품, 파스타상품, 샐러드상품));

        // when
        List<Menu> results = menuService.list();

        // then
        assertAll(
            () -> assertThat(results).hasSize(1),
            () -> assertThat(results.get(0).getId()).isEqualTo(스테이크정식.getId()),
            () -> assertThat(results.get(0).getName()).isEqualTo(스테이크정식.getName())
        );
    }
}

