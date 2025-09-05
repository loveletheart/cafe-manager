package myapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

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
	@MessageMapping("/order/check")
    public void handleOrder(@Payload List<Order> orderRequests, Principal principal) {
        String userID = principal.getName();

        if (orderRequests == null || orderRequests.isEmpty()) {
            System.out.println("WebSocket으로 받은 주문 데이터가 비어 있음.");
            return;
        }

        boolean success = orderService.processOrder(orderRequests, userID);

        if (success) {
            // 관리자 구독 채널로 전송
            messagingTemplate.convertAndSend("/sub/orders", orderRequests);
            System.out.println("주문이 완료되었습니다 (WebSocket).");
        } else {
            System.out.println("주문 처리 중 오류 발생 (WebSocket).");
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
