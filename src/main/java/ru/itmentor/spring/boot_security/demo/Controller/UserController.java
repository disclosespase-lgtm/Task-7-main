package ru.itmentor.spring.boot_security.demo.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmentor.spring.boot_security.demo.model.User;

@Tag(name = "User Controller", description = "Контроллер для получения текущего пользователя")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Operation(summary = "Метод авторизации пользователя")
    @GetMapping
    public ResponseEntity<User> getAuthorizedUser(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }
}