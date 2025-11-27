package Tproject.service.Impl;

import Tproject.model.User;
import Tproject.repository.UserRepository;
import Tproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
