package Tproject.service;

import Tproject.dto.UserDto;
import Tproject.model.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    public List<User> getAll();
    public User setRole(String username, String newRole, HttpServletRequest request);
}
