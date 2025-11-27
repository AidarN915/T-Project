package Tproject.controller;

import Tproject.dto.UserDto;
import Tproject.mapper.UserMapper;
import Tproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAll(){
        return ResponseEntity.ok(userMapper.toListDto(userService.getAll()));
    }
}
