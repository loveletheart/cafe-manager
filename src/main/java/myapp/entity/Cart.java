package myapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    
    @Id
    @Column(name = "Date")
    private LocalDate date;
    
    @Id
    @Column(name = "Situation")
    private String sit;
    
    private String menuNameen;
    private String syrup;
    private int count;
    private int price;
    
    public Cart(String userId, String menuName,String temperature,String beanType,String cupType,String syrup,String menuNameen, int count, int price) {
        this.userId = userId;
        this.menuName = menuName;
        this.temperature = temperature;
        this.beanType = beanType;
        this.cupType = cupType;
        this.syrup = syrup;
        this.menuNameen = menuNameen;
        this.count = count;
        this.price = price;
    }
}
