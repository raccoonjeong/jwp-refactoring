package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(nullable = false)
    private String name;

    protected Name() {}

    private Name(String name) {
        validateNameIsNull(name);
        this.name = name;
    }

    public static Name from(String name) {
        return new Name(name);
    }

    private void validateNameIsNull(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없음");
        }
    }

    public String value() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
