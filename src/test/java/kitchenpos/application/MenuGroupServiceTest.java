package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.application.MenuGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuGroupService 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void createMenuGroup() {
        // given
        MenuGroup 양식 = new MenuGroup(1L, "양식");
        when(menuGroupDao.save(양식)).thenReturn(양식);

        // when
        MenuGroup result = menuGroupService.create(양식);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(양식.getId()),
            () -> assertThat(result.getName()).isEqualTo(양식.getName())
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    public void findAllMenuGroup() {
        // given
        MenuGroup 양식 = new MenuGroup(1L, "양식");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(양식));

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.get(0).getId()).isEqualTo(양식.getId()),
            () -> assertThat(result.get(0).getName()).isEqualTo(양식.getName())
        );
    }
}
