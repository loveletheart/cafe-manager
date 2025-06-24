package myapp.controller;

import myapp.entity.Menu;
import myapp.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import java.nio.file.*;
import java.io.IOException;

@Controller
@RequestMapping("/admin/menucontroller")
public class MenuItemContriller {
	
	@Autowired
    private MenuItemService menuItemService;
	
	@GetMapping("/menus")
    public String listMenus(Model model) {
        model.addAttribute("menus", menuItemService.getAllMenus());
        return "admin/menucontroller/menu_list";
    }
    
	@GetMapping("/add")
	public String showAddForm(Model model) {
	    model.addAttribute("menu", new Menu());
	    model.addAttribute("actionUrl", "/admin/menucontroller/add");
	    return "admin/menucontroller/menu_form";
	}

    @PostMapping("/add")
    public String addMenu(@ModelAttribute Menu menu,
            @RequestParam("image") MultipartFile imageFile,
            Model model) throws IOException {
    	if (menuItemService.existsByMenuName(menu.getMenuName())) {
            model.addAttribute("error", "이미 존재하는 메뉴 이름입니다.");
            model.addAttribute("menu", menu);
            model.addAttribute("actionUrl", "/admin/menucontroller/add");
            return "admin/menucontroller/menu_form";
        }
    	
    	String originalFilename = imageFile.getOriginalFilename();
        String contentType = imageFile.getContentType();

        if (originalFilename == null ||
            !originalFilename.toLowerCase().endsWith(".jpg") ||
            !"image/jpeg".equals(contentType)) {

            // 여기서 예외 대신 다시 form으로 돌아감
            model.addAttribute("error", "JPG 파일만 업로드 가능합니다.");
            model.addAttribute("menu", menu);
            model.addAttribute("actionUrl", "/admin/menucontroller/add");
            return "admin/menucontroller/menu_form";
        }
    	
        // 이미지 저장 처리
    	if (!imageFile.isEmpty()) {
    	    // 원본 파일명에서 확장자 추출
    	    String extension = ""; // 확장자 초기화

    	    int dotIndex = originalFilename.lastIndexOf('.');
    	    if (dotIndex > 0) {
    	        extension = originalFilename.substring(dotIndex); // .jpg, .png 등
    	    }

    	    // 영어 메뉴 이름 + 확장자로 저장
    	    String fileName = menu.getMenuName().replaceAll("[^a-zA-Z0-9]", "_") + extension;

    	    Path savePath = Paths.get("src/main/resources/static/images", fileName);
    	    Files.copy(imageFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
    	}

        menuItemService.addMenu(menu);
        return "redirect:/admin/menucontroller/menus";
    }

    @GetMapping("/edit/{menuName}")
    public String showEditForm(@PathVariable String menuName, Model model) {
        Optional<Menu> menu = menuItemService.getMenuByName(menuName);
        if (menu.isPresent()) {
            model.addAttribute("menu", menu.get());
            model.addAttribute("actionUrl", "/admin/menucontroller/edit/" + menuName);
            return "admin/menucontroller/menu_form";
        } else {
        	 model.addAttribute("error", "수정이 불가합니다");
        	 return "admin/menucontroller/menu_form";
        }
    }

    @PostMapping("/edit/{menuName}")
    public String updateMenu(@PathVariable String menuName, @ModelAttribute Menu menu) {
    	menuItemService.updateMenu(menuName, menu);
        return "redirect:/admin/menucontroller/menus";
    }

    @GetMapping("/delete/{menuName}")
    public String deleteMenu(@PathVariable String menuName,RedirectAttributes redirectAttributes) {
    	menuItemService.deleteMenu(menuName);
    	redirectAttributes.addFlashAttribute("complete", "삭제가 완료되었습니다.");
        return "redirect:/admin/menucontroller/menus";
    }
}
