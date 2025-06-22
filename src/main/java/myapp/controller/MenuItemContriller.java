package myapp.controller;

import myapp.entity.Menu;
import myapp.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    	if (menuItemService.existsByMenuName(menu.getMenu_Name())) {
            model.addAttribute("error", "이미 존재하는 메뉴 이름입니다.");
            model.addAttribute("menu", menu);
            return "admin/menucontroller/menu_form";
        }

        // 이미지 저장 처리
        if (!imageFile.isEmpty()) {
            String fileName = menu.getMenu_Name() + "_" + imageFile.getOriginalFilename();
            Path savePath = Paths.get("src/main/resources/static/images", fileName);
            Files.copy(imageFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
            // 필요시 menu.setImagePath("/images/" + fileName); 등 저장
        }

        menuItemService.addMenu(menu);
        return "redirect:/admin/menucontroller/menu";
    }

    @GetMapping("/edit/{menuName}")
    public String showEditForm(@PathVariable String menuName, Model model) {
        Optional<Menu> menu = menuItemService.getMenuByName(menuName);
        if (menu.isPresent()) {
            model.addAttribute("menu", menu.get());
            model.addAttribute("actionUrl", "/admin/menucontroller/edit/" + menuName);
            return "admin/menucontroller/menu_form";
        } else {
            return "redirect:/admin/menucontroller/menus"; // 잘못된 접근시 목록으로
        }
    }

    @PostMapping("/edit/{menuName}")
    public String updateMenu(@PathVariable String menuName, @ModelAttribute Menu menu) {
    	menuItemService.updateMenu(menuName, menu);
        return "redirect:/admin/menucontroller/menu_list";
    }

    @GetMapping("/delete/{menuName}")
    public String deleteMenu(@PathVariable String menuName) {
    	menuItemService.deleteMenu(menuName);
        return "redirect:/admin/menucontroller/menu_list";
    }
}
