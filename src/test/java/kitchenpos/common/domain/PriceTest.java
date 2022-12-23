package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("가격 관련 도메인 테스트")
public class PriceTest {

    @DisplayName("가격을 생성한다.")
    @Test
    void createPrice() {
        // given
        BigDecimal actualPrice = BigDecimal.valueOf(1000);

        // when
        Price price = Price.from(actualPrice);

        // then
        assertAll(
                () -> assertThat(price.value()).isEqualTo(actualPrice),
                () -> assertThat(price).isEqualTo(Price.from(actualPrice))
        );
    }

    @ParameterizedTest(name = "가격이 0보다 작으면 에러가 발생한다. (가격: {0})")
    @ValueSource(longs = {-1000, -230})
    void createPriceThrowErrorWhenPriceIsSmallerThanZero(long price) {
        // given
        BigDecimal actualPrice = BigDecimal.valueOf(price);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Price.from(actualPrice))
                .withMessage("가격은 0보다 작을 수 없음");
    }

    @DisplayName("가격은 비어있으면 에러가 발생한다.")
    @Test
    void createPriceThrowErrorWhenPriceIsNull() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Price.from(null))
                .withMessage("가격은 비어있을 수 없음");
    }

    @DisplayName("가격에 수량을 곱할 수 있다.")
    @Test
    void multiplyPriceByQuantity() {
        // given
        long actualPrice = 5000;
        long actualQuantity = 3;
        Price price = Price.from(BigDecimal.valueOf(actualPrice));
        Quantity quantity = Quantity.from(actualQuantity);

        // when
        Price multiplyPrice = price.multiply(quantity);

        // then
        assertThat(multiplyPrice.value()).isEqualTo(BigDecimal.valueOf(actualPrice * actualQuantity));
    }

    @DisplayName("가격에 가격을 더할 수 있다.")
    @Test
    void addPrice() {
        // given
        BigDecimal actualPrice = BigDecimal.valueOf(5000);
        BigDecimal addPrice = BigDecimal.valueOf(3000);
        Price price = Price.from(actualPrice);

        // when
        Price result = price.add(Price.from(addPrice));

        // then
        assertThat(result.value()).isEqualTo(actualPrice.add(addPrice));
    }

    @DisplayName("가격을 서로 비교할 수 있다.")
    @Test
    void comparePrice() {
        // given
        BigDecimal smallDecimal = BigDecimal.valueOf(3000);
        BigDecimal bigDecimal = BigDecimal.valueOf(4000);
        Price smallPrice = Price.from(smallDecimal);
        Price bigPrice = Price.from(bigDecimal);

        // when
        int compare = smallPrice.compareTo(bigPrice);

        // then
        assertThat(compare).isEqualTo(smallDecimal.compareTo(bigDecimal));
    }
}
