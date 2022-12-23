package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price implements Comparable<Price>{

    public static final Price ZERO_PRICE = Price.from(BigDecimal.ZERO);

    private BigDecimal price;

    public Price(BigDecimal price) {
        validatePriceIsNull(price);
        validatePriceIsSmallerThanZero(price);
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }


    private void validatePriceIsNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("가격은 비어있을 수 없음");
        }
    }

    private void validatePriceIsSmallerThanZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 없음");
        }
    }

    //todo fix
    public Price multiply(Quantity quantity) {
        return new Price(price.multiply(quantity.toBigDecimal()));
    }

    public Price add(Price addPrice) {
        return new Price(this.price.add(addPrice.price));
    }

    public BigDecimal value() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }


    @Override
    public int compareTo(Price comparePrice) {
        return this.price.compareTo(comparePrice.price);
    }
}
