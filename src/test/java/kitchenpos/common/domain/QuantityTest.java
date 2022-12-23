package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("수량 관련 도메인 테스트")
public class QuantityTest {

    @DisplayName("수량을 생성한다.")
    @Test
    void createQuantity() {
        // given
        long actualQuantity = 10;

        // when
        Quantity quantity = Quantity.from(actualQuantity);

        // then
        assertAll(
                () -> assertThat(quantity).isEqualTo(Quantity.from(actualQuantity)),
                () -> assertThat(quantity.value()).isEqualTo(actualQuantity)
        );
    }

    @ParameterizedTest(name = "수량이 0보다 작으면 에러가 발생한다. (수량: {0})")
    @ValueSource(longs = {-1, -3})
    void createQuantityThrowErrorWhenQuantityIsSmallerThanZero(long quantity) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Quantity.from(quantity))
                .withMessage("수량은 0보다 작을 수 없음");
    }

    @DisplayName("수량의 형식을 BigDecimal로 변경 할 수 있다.")
    @Test
    void convertQuantityToBigDecimal() {
        // given
        long actualQuantity = 10;
        Quantity quantity = Quantity.from(actualQuantity);

        // when
        BigDecimal convertBigDecimal = quantity.toBigDecimal();

        // then
        assertAll(
                () -> assertThat(convertBigDecimal.longValue()).isEqualTo(actualQuantity),
                () -> assertThat(convertBigDecimal).isEqualTo(BigDecimal.valueOf(actualQuantity))
        );
    }
}
