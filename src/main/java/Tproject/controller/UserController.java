package Tproject.controller;

import Tproject.dto.UserDto;
import Tproject.mapper.UserMapper;
import Tproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/role")
    public ResponseEntity<UserDto> setRole(@RequestParam("username") String username,
                                           @RequestParam("role") String role,
                                           HttpServletRequest request){
        return ResponseEntity.ok(userMapper.toDto(userService.setRole(username,role,request)));
    }
}
