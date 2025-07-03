package myapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import myapp.service.OrderService;
import myapp.entity.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
    private OrderService orderService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

    /**
     * 장바구니에서 넘어온 주문 데이터를 받아서 저장하는 엔드포인트
     */
    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<String> checkout(@RequestBody List<Order> orderRequests) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userID = authentication.getName();  // 기본적으로 username 반환
    	
    	if (orderRequests == null || orderRequests.isEmpty()) {//orderRequests안에 데이터가 없는 경우
    	    System.out.println("orderRequests가 비어 있음.");
    	    return ResponseEntity.badRequest().body("주문 데이터가 없습니다.");
    	}
    	
    	boolean success = orderService.processOrder(orderRequests, userID);
    	
    	if (success) {
            messagingTemplate.convertAndSend("/sub/orders", orderRequests);
            return ResponseEntity.ok("주문이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 처리 중 오류가 발생했습니다.");
        }
    }


    /**
     * 주문 완료 페이지로 이동(다시 메인 페이지로 이동)
     */
    @GetMapping("/complete")
    public String orderComplete() {
        return "/menu/coffee";
    }

    /**
     * 전체 주문 내역을 ods.html 템플릿에 전달하여 출력
     */
    @GetMapping("/list")
    public List<Order> getCompletedOrders() {
        return orderService.getCompletedOrders("주문완료");
    }
}
