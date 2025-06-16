package myapp.service;

import java.util.List;
import java.util.Optional;

import myapp.entity.Menu;
import myapp.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
}