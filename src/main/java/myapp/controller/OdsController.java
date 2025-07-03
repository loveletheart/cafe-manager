package myapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import myapp.entity.Order;
import myapp.repository.OrderRepository;

@Controller
@RequestMapping("/admin")
public class OdsController {
	
	@Autowired
	private OrderRepository oederrepository;
	
	/**
     * ODS화면 출력
     */
    @GetMapping("/ODS")
    public String showODS() {
        return "admin/ODS";
    }
    
    /**
     * 주문 상태가 '주문완료'인 데이터만 JSON으로 반환
     */
    @GetMapping("/order")
    @ResponseBody
    public ResponseEntity<List<Order>> getOrders() {
    	
        List<Order> cartItems = oederrepository.findBySituationAndOrderDate("주문완료", LocalDate.now());
        
        return ResponseEntity.ok(cartItems); // JSON 응답 반환
    }
}
