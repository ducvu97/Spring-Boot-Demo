package com.example.demo.controller;

import com.example.demo.model.JwtResponse;
import com.example.demo.model.UserDao;
import com.example.demo.model.UserDto;
import com.example.demo.service.JwtTokenUtilService;
import com.example.demo.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api("User APIs")
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtilService jwtTokenUtilService;

    @Autowired
    private UserService userService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Thành công"),
            @ApiResponse(code = 401, message = "Chưa xác thực"),
            @ApiResponse(code = 403, message = "Truy cập bị cấm"),
            @ApiResponse(code = 404, message = "Không tìm thấy")
    })

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );
        UserDetails userDetails = userService.loadUserByUsername(userDto.getUsername());

        String token = jwtTokenUtilService.generateToken(userDetails);

        JwtResponse response = new JwtResponse(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDao>> getUsers() {
        List<UserDao> userDtoList = userService.getUsers();
        return ResponseEntity.ok(userDtoList);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserDao> saveUser(@ApiParam(value = "New object to creat ", required = true) @Valid @RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.ok(userService.save(userDto));
    }

    private void login(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
