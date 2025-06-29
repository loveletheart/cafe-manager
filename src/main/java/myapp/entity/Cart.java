package myapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@IdClass(CartId.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

	@Id
    @Column(name = "user_id")
    private String userId;

    @Id
    private String menuName;

    @Id
    private String temperature;

    @Id
    private String beanType;

    @Id
    private String cupType;

    private String menuNameen;
    private String syrup;
    private int count;
    private int price;
    
    public Cart(String userId, String menuName, String menuNameen, int count, int price) {
        this.userId = userId;
        this.menuName = menuName;
        this.menuNameen = menuNameen;
        this.count = count;
        this.price = price;
    }
}
