package myapp.repository;

import myapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import myapp.entity.CartId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {

    // 사용자 아이디와 메뉴 이름을 기준으로 장바구니 항목을 찾는 메서드
	List<Cart> findByUserId(String userId);
	
    // 사용자 아이디와 메뉴 이름을 기준으로 장바구니 항목을 하나만 찾는 메서드
	Optional<Cart> findByuserIdAndMenuName(String userId, String menuName);
	
	// 사용자 아이디에 맞는 저장된 데이터 삭제
	void deleteByUserId(String userId);
	
	//장바구니에 항목 비교 검색용으로 쓰는 메서드
	@Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.menuName = :menuName " +
		       "AND c.temperature = :temperature AND c.beanType = :beanType " +
		       "AND c.cupType = :cupType AND c.date = :date AND c.sit = :sit")
		Optional<Cart> findSameCartItem(
		    @Param("userId") String userId,
		    @Param("menuName") String menuName,
		    @Param("temperature") String temperature,
		    @Param("beanType") String beanType,
		    @Param("cupType") String cupType,
		    @Param("date") LocalDate date,
		    @Param("sit") String sit
		);

}
