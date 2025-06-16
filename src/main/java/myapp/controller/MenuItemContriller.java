package myapp.controller;

import myapp.entity.Menu;
import myapp.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/admin/menucontroller")
public class MenuItemContriller {
	
	@Autowired
    private MenuItemService menuItemService;
	
    @GetMapping
    public String listMenus(Model model) {
        model.addAttribute("menus", menuItemService.getAllMenus());
        return "admin/menu_list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("menu", new Menu());
        return "admin/menu_form";
    }

    @PostMapping("/add")
    public String addMenu(@ModelAttribute Menu menu, Model model) {
        if (menuItemService.existsByMenuName(menu.getMenu_Name())) {
            model.addAttribute("menu", menu);
            model.addAttribute("error", "이미 존재하는 메뉴 이름입니다.");
            return "admin/menu_form";
        }

        menuItemService.addMenu(menu);
        return "redirect:/admin/menu_list";
    }

    @GetMapping("/edit/{menuName}")
    public String showEditForm(@PathVariable String menuName, Model model) {
        Optional<Menu> menu = menuItemService.getMenuByName(menuName);
        menu.ifPresent(value -> model.addAttribute("menu", value));
        return "admin/menu_form";
    }

    @PostMapping("/edit/{menuName}")
    public String updateMenu(@PathVariable String menuName, @ModelAttribute Menu menu) {
    	menuItemService.updateMenu(menuName, menu);
        return "redirect:/admin/menu_list";
    }

    @GetMapping("/delete/{menuName}")
    public String deleteMenu(@PathVariable String menuName) {
    	menuItemService.deleteMenu(menuName);
        return "redirect:/admin/menu_list";
    }
}
