package ru.itmentor.spring.boot_security.demo.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.service.UserService;

@Tag(name = "Admin Controller", description = "Контроллер для управления пользователями")
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(RoleRepository roleRepository, UserService userService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/admin/users";
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping(value = "/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "users";
    }

    @Operation(summary = "Показать пользователя")
    @GetMapping(value = "/user")
    public String getUser(Model model, @RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("user", userService.getUser(id));
        return "user";
    }

    @Operation(summary = "Сохранить пользователя")
    @GetMapping(value = "/new-user")
    public String addUser(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("newUser", new User());
        return "new-user";
    }

    @Operation(summary = "Сохраняем кнопкой сейв")
    @PostMapping(value = "/new-user")
    public String postAddUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @Operation(summary = "Изменить пользователя")
    @GetMapping(value = "/change-user")
    public String changeUser(Model model, @RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", userService.getUser(id));
        return "change-user";
    }

    @PostMapping(value = "/change-user")
    public String mergeChangeUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
