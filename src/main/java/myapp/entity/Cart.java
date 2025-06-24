package myapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    
    private String menuName; //메뉴 이름
    private String menuNameen; //메뉴 이름
    private String temperature; // HOT or ICE
    private String beanType;    // 원두 이름
    private String cupType;     // 매장/일회용/개인컵
    private String syrup;       // 시럽 종류
    private int count;          // 수량
    private int price;          // 개당 가격
    private String beanDescription; // 원두 설명
    
    public Cart(String userId, String menuName, String menuNameen, int count, int price) {
        this.userId = userId;
        this.menuName = menuName;
        this.menuNameen = menuNameen;
        this.count = count;
        this.price = price;
    }
}
