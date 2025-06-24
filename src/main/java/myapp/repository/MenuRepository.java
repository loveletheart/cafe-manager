package myapp.repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import myapp.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {
	 @Query("SELECT m FROM Menu m WHERE m.type = :menu")//페이지 개수 검색 및 메뉴 조회
	 Page<Menu> findByPage(String menu, Pageable pageable);
	 
	 @Query("SELECT m FROM Menu m WHERE m.menuName = :menuName")
	 Optional<Menu> findByMenuName(String menuName);
}