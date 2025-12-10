package Tproject.controller;

import Tproject.dto.LoginDto;
import Tproject.dto.UserDto;
import Tproject.dto.UserListDto;
import Tproject.mapper.UserMapper;
import Tproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    @GetMapping("/all")
    public ResponseEntity<List<UserListDto>> getAll(){
        return ResponseEntity.ok(userMapper.toListRoleDto(userService.getAll()));
    }

    @PostMapping("/role")
    public ResponseEntity<UserDto> setRole(@RequestParam("username") String username,
                                           @RequestParam("role") String role,
                                           HttpServletRequest request){
        return ResponseEntity.ok(userMapper.toDto(userService.setRole(username,role,request)));
    }

    @PostMapping("/update-name")
    public ResponseEntity<LoginDto> updateName(@RequestParam("username") String username,
                                               Authentication auth){
        return ResponseEntity.ok(userService.updateName(username,auth));
    }

    @PostMapping(value = "/upload-avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadAvatar(@RequestParam("file" )MultipartFile file,
                                                Authentication auth){
        return ResponseEntity.ok(userMapper.toDto(userService.uploadAvatar(file,auth)));
    }

    @PostMapping("/update-phone-number")
    public ResponseEntity<UserDto> updatePhoneNumber(@RequestParam("phone") String phone,
                                                     Authentication auth){
        return ResponseEntity.ok(userMapper.toDto(userService.updatePhoneNumber(phone,auth)));
    }
}
