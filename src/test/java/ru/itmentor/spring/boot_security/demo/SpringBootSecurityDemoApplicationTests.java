package ru.itmentor.spring.boot_security.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.List;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("CRUD Integration Tests")
class SpringBootSecurityDemoApplicationTests {

    @Autowired
    private UserService userService;

    private final String testName = "Ivan";
    private final String testLastName = "Testovich";
    private final Integer testAge = 22;
    private final String testUsername = "ivan_test";
    private final String testPassword = "test123";

    @AfterEach
    void tearDown() {
        log.info("Очистка БД");
        List<User> users = userService.getAllUser();
        for (User user : users) {
            userService.deleteUser(user.getId());
        }
        log.info("Cleanup finished.");
    }

    @DisplayName("Save user test")
    @Test
    void saveUserTest() {
        User newUser = new User(testName, testLastName, testAge, testUsername, testPassword);
        userService.saveUser(newUser);
        List<User> allUsers = userService.getAllUser();
        Assertions.assertEquals(1, allUsers.size());
        Assertions.assertEquals(testName, allUsers.get(0).getFirstName());
    }

    @DisplayName("Delete user test")
    @Test
    void deleteUserTest() {
        userService.saveUser(new User(testName, testLastName, testAge, testUsername, testPassword));
        long id = userService.getAllUser().get(0).getId();
        userService.deleteUser(id);
        Assertions.assertTrue(userService.getAllUser().isEmpty());
    }

    @DisplayName("Get all users test")
    @Test
    void getAllUserTest() {
        userService.saveUser(new User(testName, testLastName, testAge, testUsername, testPassword));
        Assertions.assertEquals(1, userService.getAllUser().size());
    }

    @DisplayName("Update user test")
    @Test
    void updateUserTest() {
        userService.saveUser(new User(testName, testLastName, testAge, testUsername, testPassword));
        long id = userService.getAllUser().get(0).getId();
        User updateUser = new User("Test", "Test", 1, "test_updated", "pass");
        updateUser.setId(id);
        userService.saveUser(updateUser);
        User userDb = userService.getUser(id);
        Assertions.assertEquals("Test", userDb.getFirstName());
        Assertions.assertEquals("Test", userDb.getLastName());
    }
}