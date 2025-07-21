package myapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import myapp.entity.Order;
import myapp.repository.CartRepository;
import myapp.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
	
    private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	
	public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    //개별 주문 저장
    @Transactional
    public boolean processOrder(List<Order> orderRequests, String userId) {
    	String groupId = UUID.randomUUID().toString();
    	try {
            // 주문 저장
            for (Order order : orderRequests) {
            	order.setOrderGroupId(groupId);
                order.setUserId(userId);
                order.setSituation("주문완료");
                order.setOrderDateTime(LocalDate.now(), LocalTime.now());//현재 날짜와 현재 시간 따로 저장
                orderRepository.save(order);
                orderRepository.flush();
            }

            // 주문이 완료되면 장바구니 삭제
            cartRepository.deleteByUserId(userId);

            return true;  // 주문 및 장바구니 삭제 성공
        } catch (Exception e) {
            return false; // 주문 실패
        }
    }

    // 주문 상태가 "주문완료"인 주문들만 가져옴
    public List<Order> getCompletedOrders(String situation) {
        return orderRepository.findBySituation(situation);
    }
}
