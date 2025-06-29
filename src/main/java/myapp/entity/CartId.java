package myapp.entity;

import java.io.Serializable;
import java.util.Objects;

public class CartId implements Serializable {

    private String userId;
    private String menuName;
    private String temperature;
    private String beanType;
    private String cupType;

    public CartId() {}

    public CartId(String userId, String menuName, String temperature, String beanType, String cupType) {
        this.userId = userId;
        this.menuName = menuName;
        this.temperature = temperature;
        this.beanType = beanType;
        this.cupType = cupType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartId)) return false;
        CartId cartId = (CartId) o;
        return Objects.equals(userId, cartId.userId) &&
               Objects.equals(menuName, cartId.menuName) &&
               Objects.equals(temperature, cartId.temperature) &&
               Objects.equals(beanType, cartId.beanType) &&
               Objects.equals(cupType, cartId.cupType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, menuName, temperature, beanType, cupType);
    }
}
