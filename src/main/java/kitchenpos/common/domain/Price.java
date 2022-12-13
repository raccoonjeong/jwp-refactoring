package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private BigDecimal price;

    public Price(BigDecimal price) {

        if (isGreaterThanZero(price)) {
            throw new IllegalArgumentException();
        }

        this.price = price;
    }

    public Price(int price) {
        BigDecimal castedPrice = BigDecimal.valueOf(price);

        if (isGreaterThanZero(castedPrice)) {
            throw new IllegalArgumentException();
        }

        this.price = castedPrice;
    }

    public static boolean isGreaterThanZero(BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal multiply(BigDecimal target) {
        return this.price.multiply(target);
    }

}
