package myapp.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders") // 테이블 이름
@Data
@Getter
@Setter
public class Order {
	
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // String 타입의 UUID 사용
    private String id;
    
    private String userId;    // 주문한 사용자 ID
    
    private String menuName;  // 주문한 메뉴 이름
    private int quantity;     // 수량
    private int price;        // 가격
    private String situation; // 현재상태
    
    private LocalDate orderDate; // 날짜만 저장
    private LocalTime orderTime; // 시간만 저장
    
    private String temperature; // 온도 설정
    private String beanType; // 원두 설정
    private String cupType; // 컵 설정
    private String syrup; //시럽 종류 설정
    private String orderGroupId; // 그룹 아이디 설정
    // 기본 생성자
    public Order() {}

    // 생성자
    public Order(String userId, String menuName, int quantity, int price, 
            String temperature, String beanType, String cupType, String syrup, 
            String orderGroupId, LocalDate orderDate ,LocalTime orderTime) {
	   this.userId = userId;
	   this.menuName = menuName;
	   this.quantity = quantity;
	   this.price = price;
	   this.temperature = temperature;
	   this.beanType = beanType;
	   this.cupType = cupType;
	   this.syrup = syrup;
	   this.orderGroupId = orderGroupId;
	   this.orderDate = orderDate;
	   this.orderTime = orderTime;
	}
    
    public void setOrderDateTime(LocalDate orderDate, LocalTime orderTime) {
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
    }
}

