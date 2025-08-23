package myapp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import myapp.entity.Order;
import myapp.repository.OrderRepository;

@Controller
@RequestMapping("/admin")
public class OdsController {
	
	@Autowired
	private OrderRepository oederrepository;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
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
    
    /**
     * 준비가 완료된 주문을 '준비완료'인 상태로 데이터 변환 
     */
    @PostMapping("/complete/{orderGroupId}")
    @ResponseBody
    public ResponseEntity<String> completeOrdersInGroup(@PathVariable String orderGroupId) {
    	try {
    		List<Order> OrderList = oederrepository.findByOrderGroupId(orderGroupId);
    		
    		if(!OrderList.isEmpty()) {
    			for(Order order : OrderList)order.setSituation("준비완료");
    			
    			oederrepository.saveAll(OrderList);
    			return new ResponseEntity<>("Order group " + orderGroupId + " marked as READY", HttpStatus.OK);
    		}else {
    			return new ResponseEntity<>("Order group with ID " + orderGroupId + " not found.", HttpStatus.NOT_FOUND);
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
            return new ResponseEntity<>("Error updating order group status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    /**
     * '준비완료' 주문 리스트를 위한 React 페이지를 제공
     */
    @GetMapping("/completed")
    public String showCompletedPage() {
        return "admin/completed"; // src/main/resources/templates/admin/completed.html을 반환
    }
    
    /**
     * '준비완료' 주문 리스트를 위한 json데이터 제공
     */
    @GetMapping("/complete")
    @ResponseBody
    public ResponseEntity<List<Order>> getcompletes() {
    	
        List<Order> cartItems = oederrepository.findBySituationAndOrderDate("준비완료", LocalDate.now());
        
        return ResponseEntity.ok(cartItems); // JSON 응답 반환
    }
}
