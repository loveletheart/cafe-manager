package myapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "menu")
@Getter
@Setter
@Data
public class Menu {

    @Id
    @Column(name = "menu_Name")
    private String menuName;//해당 순서에 맞추어서 데이터에서 검색함
    
    @Column(name = "menu_Nameen")
    private String menuNameen;
    
    private String type;
    private int price;
   
}
