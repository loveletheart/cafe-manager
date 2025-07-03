package myapp.controller;

import myapp.entity.*;
import myapp.service.MenuService;
import myapp.repository.CartRepository;
import myapp.repository.MenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import java.util.*;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private CartRepository cartRepository;

    /**
     * 기본 메뉴 페이지 (GET 요청)
     * 사용자가 `/menu`로 접근하면 기본적으로 'coffee' 카테고리를 보여줌
     */
    @GetMapping
    public ModelAndView showDefaultMenu(@RequestParam(defaultValue = "0") int page) {
    	 // 리다이렉트 URL에 페이지 파라미터 포함
        return new ModelAndView("redirect:/menu/coffee?page=" + page);
    }

    /**
     * 특정 카테고리 메뉴 페이지 (GET 요청)
     * 사용자가 `/menu/{category}`로 접근하면 해당 카테고리의 메뉴를 조회함
     * 예: `/menu/aid`, `/menu/cookie`
     */
    @GetMapping("/{category}")
    public ModelAndView getMenuByCategory(@PathVariable String category, @RequestParam(defaultValue = "0") int page) {
        return getMenu(category, page);  // 해당 카테고리의 메뉴를 조회
    }

    /**
     * 내부 메서드 - 메뉴 데이터 조회 및 페이지 반환
     * 카테고리에 따라 데이터를 가져와서 ModelAndView를 구성하여 반환
     */
    private ModelAndView getMenu(String category, int page) {
        int pageSize = 12;  // 한 페이지당 12개의 아이템을 표시
        Page<Menu> menuPage = menuService.getMenuByCategory(category, page, pageSize);

        String viewName = "menu/" + category; // 뷰 이름 지정
        ModelAndView modelAndView = new ModelAndView(viewName);

        modelAndView.addObject("menus", menuPage.getContent());  // 메뉴 데이터 추가
        modelAndView.addObject("activeCategory", category);  // 현재 카테고리 지정 (버튼 활성화용)
        modelAndView.addObject("totalPages", menuPage.getTotalPages());  // 전체 페이지 수 추가
        modelAndView.addObject("currentPage", page);  // 현재 페이지 번호 추가

        System.out.println("Menus on current page: " + menuPage.getContent().size()); // 디버깅용 출력

        return modelAndView;
    }
    
    /**
     * 메뉴 옵션 설정
     * 사용자가 '메뉴 선택'버튼 클릭시 호출
     */
    @GetMapping("/order/{menuName}")
    public String showMenuDetail(@PathVariable String menuName, Model model) {
    	Menu menu = menuService.findMenuByName(menuName);
        model.addAttribute("menu", menu);
        return "menu/options :: optionForm"; // HTML 조각만 반환 (Thymeleaf fragment)
    }

    /**
     * 장바구니 추가 (AJAX 요청 처리)
     * 사용자가 '장바구니 추가' 버튼을 클릭하면 호출됨
     */
    @PostMapping("/options/add")
    @ResponseBody
    public ResponseEntity<String> addToCartWithOptions(@ModelAttribute Cart cart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        cart.setUserId(userId);

        menuService.addToCart(cart);

        return ResponseEntity.ok("success"); // JSON 형태가 아니더라도 단순 문자열 응답 가능
    }

    /**
     * 로그인한 사용자의 장바구니 목록 조회
     */
    @GetMapping("/cart")
    public String showCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();  // 로그인된 사용자 ID 가져오기
        
        List<Cart> cartItems = cartRepository.findByUserId(userId);  // userId로 장바구니 아이템을 조회
        model.addAttribute("cartItems", cartItems);
        
        int totalSum = menuService.calculateTotalSum(userId);
        model.addAttribute("totalPrice", totalSum);  // 전체 금액 합산
        
        return "menu/cart";  // 장바구니 페이지로 리턴
    }

    /**
     * 장바구니 상품 수량 업데이트
     */
    @PostMapping("/cart/update")
    @ResponseBody
    public ResponseEntity<?> updateCartItem(@RequestBody Map<String, Object> requestData) {
        String menuName = (String) requestData.get("menuName");
        int count = (int) requestData.get("count");

        // 로그인된 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        
        // 장바구니 업데이트
        boolean result = menuService.updateCartItem(userId, menuName, count);
        
        // 전체 합계 계산
        int totalSum = menuService.calculateTotalSum(userId);
        
        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("totalSum", totalSum);
        
        // 아이템 가격 정보 추가
        if (result) {
            int updatedPrice = count * menuService.getPrice(userId,menuName);
            response.put("updatedPrice", updatedPrice);
        }

        return ResponseEntity.ok(response);
    }
}