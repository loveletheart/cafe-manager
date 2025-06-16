package myapp.entity;

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
    private String menu_Name;//해당 순서에 맞추어서 데이터에서 검색함
    private String menu_Nameen;
    private String type;
    private int price;
   
}
