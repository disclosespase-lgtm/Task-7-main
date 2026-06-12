package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService {
    public void saveUser(User user);

    public void deleteUser(long id);

    public User getUser(long id);

    public List<User> getAllUser();

    public User findByUsername(String username);

}
