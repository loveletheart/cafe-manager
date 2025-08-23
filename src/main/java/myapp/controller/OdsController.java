package myapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	 * ODS화면 출력 (뷰 반환)
	 */
	@GetMapping("/ODS")
	public String showODS() {
		return "admin/ODS";
	}
	
	/**
	 * '주문완료' 상태 데이터만 JSON으로 반환
	 */
	@GetMapping("/order")
	@ResponseBody // JSON 데이터를 반환함을 명시
	public ResponseEntity<List<Order>> getOrders() {
		List<Order> cartItems = oederrepository.findBySituationAndOrderDate("주문완료", LocalDate.now());
		return ResponseEntity.ok(cartItems);
	}
	
	/**
	 * 준비가 완료된 주문을 '준비완료'로 상태 변경
	 */
	@PostMapping("/complete/{orderGroupId}")
	@ResponseBody // JSON 데이터를 반환함을 명시
	public ResponseEntity<String> completeOrdersInGroup(@PathVariable String orderGroupId) {
		try {
			List<Order> orderList = oederrepository.findByOrderGroupId(orderGroupId);
			if (!orderList.isEmpty()) {
				for (Order order : orderList) order.setSituation("준비완료");
				oederrepository.saveAll(orderList);
				return new ResponseEntity<>("Order group " + orderGroupId + " marked as READY", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Order group with ID " + orderGroupId + " not found.", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error updating order group status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * '준비완료' 주문 리스트 페이지를 제공
	 */
	@GetMapping("/completed")
	public String showCompletedPage() {
		return "admin/completed";
	}
	
	/**
	 * '준비완료' 주문 리스트의 JSON 데이터 제공
	 */
	@GetMapping("/complete")
	@ResponseBody // JSON 데이터를 반환함을 명시
	public ResponseEntity<List<Order>> getCompletes() {
		List<Order> cartItems = oederrepository.findBySituationAndOrderDate("준비완료", LocalDate.now());
		return ResponseEntity.ok(cartItems);
	}
	
	/**
	 * 준비완료 주문을 '주문완료'로 상태 변경
	 */
	@PostMapping("/revert/{orderGroupId}")
	@ResponseBody
	public ResponseEntity<String> revertOrdersInGroup(@PathVariable String orderGroupId) {
		try {
			List<Order> orderList = oederrepository.findByOrderGroupId(orderGroupId);
			if (!orderList.isEmpty()) {
				for (Order order : orderList) order.setSituation("주문완료");
				oederrepository.saveAll(orderList);
				return new ResponseEntity<>("Order group " + orderGroupId + " marked as REVERTED", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Order group with ID " + orderGroupId + " not found.", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error updating order group status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
