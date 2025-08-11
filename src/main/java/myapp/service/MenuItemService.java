package myapp.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import myapp.entity.Menu;
import myapp.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<Menu> getAllMenus() {
        return menuItemRepository.findAll();
    }

    public Optional<Menu> getMenuByName(String menuName) {
        return menuItemRepository.findById(menuName);
    }

    public Menu addMenu(Menu menu) {
        return menuItemRepository.save(menu);
    }

    public Menu updateMenu(String menuName, Menu updatedMenu) {
        if (menuItemRepository.existsById(menuName)) {
            return menuItemRepository.save(updatedMenu);
        }
        return null;
    }

    public void deleteMenu(String menuName) {
    	menuItemRepository.deleteById(menuName);
    }
    
    public boolean existsByMenuName(String menuName) {
        return menuItemRepository.existsById(menuName);
    }
    
    public List<Menu> findMenusByFilter(String menuName, String menuNameen, String type, Integer priceMin, Integer priceMax) {
        Specification<Menu> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (menuName != null && !menuName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("menuName"), "%" + menuName + "%"));
            }
            if (menuNameen != null && !menuNameen.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("menuNameen"), "%" + menuNameen + "%"));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }
            if (priceMin != null && priceMax != null) {
                // 최저가와 최고가 모두 입력된 경우
                predicates.add(criteriaBuilder.between(root.get("price"), priceMin, priceMax));
            } else if (priceMin != null) {
                // 최저가만 입력된 경우
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin));
            } else if (priceMax != null) {
                // 최고가만 입력된 경우
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // 검색 조건이 하나도 없는 경우 전체 목록을 반환하도록 처리
        if (menuName == null && menuNameen == null && type == null && priceMin == null && priceMax == null) {
            return menuItemRepository.findAll();
        }

        return menuItemRepository.findAll(spec);
    }
}