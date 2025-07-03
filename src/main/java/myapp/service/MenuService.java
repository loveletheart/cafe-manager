package myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import myapp.entity.*;
import myapp.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private CartRepository cartRepository;

    // 카테고리별 메뉴 페이지 조회
    public Page<Menu> getMenuByCategory(String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return menuRepository.findByPage(category, pageRequest);
    }

    // 장바구니에 메뉴 추가
    public boolean addToCart(Cart cart) {
        String userId = cart.getUserId();
        String menuName = cart.getMenuName();
        
        cart.setDate(LocalDate.now());
        cart.setSit("주문완료");
        
        System.out.println("userId = " + userId);
        System.out.println("menuName = " + menuName);
        System.out.println("temperature = " + cart.getTemperature());
        System.out.println("beanType = " + cart.getBeanType());
        System.out.println("cupType = " + cart.getCupType());
        System.out.println("date = " + cart.getDate());
        System.out.println("sit = " + cart.getSit());

        Optional<Cart> existing = cartRepository.findSameCartItem(
            userId,
            menuName,
            cart.getTemperature(),
            cart.getBeanType(),
            cart.getCupType(),
            cart.getDate(),
            "주문완료"
        );
        
        if (existing.isPresent()) {
            Cart existingCart = existing.get();
            int newCount = existingCart.getCount() + cart.getCount();
            existingCart.setCount(newCount);
            cartRepository.save(existingCart);
        } else {
        	cart.setDate(LocalDate.now());
            cartRepository.save(cart);
        }

        return true;
    }

    
    //사용자의 아이디에 따라서 데이터베이스cart에 있는 데이터를 다르게 가지고 옴
    public List<Cart> getCartItemsByUser(String userId) {
        return cartRepository.findByUserId(userId);  // userId로 장바구니 항목 조회
    }
    
    //장바구니에 있는 개수 조절시 실행되는 api
    public boolean updateCartItem(String userId, String menuName, int count) {
        Optional<Cart> existingCart = cartRepository.findByuserIdAndMenuName(userId, menuName);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setCount(count);
            cartRepository.save(cart);
            return true;
        }

        return false;
    }
    
    // 전체 장바구니 총합 계산 메서드
    public int calculateTotalSum(String userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream()
                        .mapToInt(cart -> cart.getPrice() * cart.getCount())
                        .sum();
    }
    
    // 메뉴 가격 가져오는 메서드
    public int getPrice(String userId,String menuName) {
        Optional<Cart> menuData = cartRepository.findByuserIdAndMenuName(userId, menuName);
        return menuData.map(Cart::getPrice).orElse(0);
    }

    public Menu findMenuByName(String menuName) {
        return menuRepository.findByMenuName(menuName).orElse(null);
    }
}
