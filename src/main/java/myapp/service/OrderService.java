package myapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger; // Logger import 추가
import org.slf4j.LoggerFactory; // LoggerFactory import 추가

import myapp.entity.Order;
import myapp.repository.CartRepository;
import myapp.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // List를 Stream으로 처리하기 위해 추가

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class); // 로거 인스턴스 생성

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    //개별 주문들을 묶어 하나의 트랜잭션으로 처리하고 저장합니다.
    //장바구니에 담긴 항목들을 주문으로 변환하고, 해당 사용자의 장바구니를 비웁니다.
    @Transactional
    public boolean processOrder(List<Order> orderRequests, String userId) {
        // 모든 개별 주문에 적용될 단일 주문 그룹 ID 생성
        String orderGroupId = UUID.randomUUID().toString();
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        try {
            // 주문 요청이 비어있는 경우 처리
            if (orderRequests == null || orderRequests.isEmpty()) {
                logger.warn("주문 요청이 비어 있습니다. 사용자 ID: {}", userId);
                return false;
            }

            // 각 주문 요청 항목에 그룹 ID, 사용자 ID, 상태, 날짜/시간 설정
            List<Order> ordersToSave = orderRequests.stream()
                .peek(order -> { // peek을 사용하여 스트림 요소를 변경
                    order.setOrderGroupId(orderGroupId);
                    order.setUserId(userId);
                    order.setSituation("주문완료");
                    order.setOrderDateTime(currentDate, currentTime);
                })
                .collect(Collectors.toList()); // 변경된 Order 객체들을 새 리스트로 수집

            // 모든 주문 항목을 한 번의 배치 작업으로 저장
            orderRepository.saveAll(ordersToSave);

            // 주문이 성공적으로 저장되면 해당 사용자의 장바구니를 비웁니다.
            cartRepository.deleteByUserId(userId);

            logger.info("사용자 '{}'의 주문이 성공적으로 처리되었습니다. 주문 그룹 ID: {}", userId, orderGroupId);
            return true; // 주문 및 장바구니 삭제 성공
        } catch (Exception e) {
            // 오류 발생 시 로그 기록 및 트랜잭션 롤백
            logger.error("사용자 '{}'의 주문 처리 중 오류 발생: {}", userId, e.getMessage(), e);
            // 트랜잭션은 @Transactional에 의해 자동으로 롤백됩니다.
            return false; // 주문 실패
        }
    }
    
    //특정 주문 상태를 가진 모든 주문 목록을 가져옵니다.
    public List<Order> getCompletedOrders(String situation) {
        return orderRepository.findBySituation(situation);
    }
}