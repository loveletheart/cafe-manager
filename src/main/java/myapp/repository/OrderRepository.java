package myapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	List<Order> findBySituation(String situation);
	
	List<Order> findBySituationAndOrderDate(String situation,LocalDate localDate);
	
	List<Order> findByOrderGroupId(String orderGroupId);
}
