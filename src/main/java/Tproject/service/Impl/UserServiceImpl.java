package Tproject.service.Impl;

import Tproject.dto.UserDto;
import Tproject.model.User;
import Tproject.repository.UserRepository;
import Tproject.service.UserService;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserUtil userUtil;
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
    public User setRole(String username, String newRole, HttpServletRequest request){
        User user = userUtil.getUserByRequest(request);
        if(!user.getRole().equals("SUPERADMIN") || username.equals(user.getUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        User newUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        newUser.setRole(newRole);
        userRepository.save(newUser);
        return user;
    }
}
