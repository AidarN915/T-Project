package Tproject.service;

import Tproject.dto.UserDto;
import Tproject.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public List<User> getAll();
    public User setRole(String username, String newRole, HttpServletRequest request);
    public User updateName(String username, Authentication auth);
    public User uploadAvatar(MultipartFile file,Authentication auth);
}
