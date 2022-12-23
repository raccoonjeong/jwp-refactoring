package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private static final int ZERO = 0;

    @Column(nullable = false, columnDefinition = "INT(11)")
    private int numberOfGuests;

    protected NumberOfGuests() {}

    private NumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsIsSmallerThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validateNumberOfGuestsIsSmallerThanZero(int numberOfGuests) {
        if(numberOfGuests < ZERO) {
            throw new IllegalArgumentException("방문한 손님 수는 0보다 작을 수 없음");
        }
    }

    public int value() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
