package com.mystery.blog.contolller;

import com.mystery.blog.entity.Result;
import com.mystery.blog.entity.User;
import com.mystery.blog.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

@Controller
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Result errorResult = new Result("fail", "没有登录", false);

        if (authentication == null) {
            return errorResult;
        } else {
            String username = authentication.getName();
            User user = userService.getUserByUserName(username);
            if (user == null) {
                return errorResult;
            }
            return new Result("ok", "登录成功", true, user);
        }
    }

    @PostMapping("auth/register")
    @ResponseBody
    public Object register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        User userByUserName = userService.getUserByUserName(username);

        if (userByUserName != null) {
            return new Result("fail", "用户已存在", false);
        } else {
            userService.save(username, password);
            login(usernameAndPassword);
        }

        return new Result("ok", "注册成功", true);
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Object login(
            @RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        Result errorResult =  new Result("fail", "用户不存在，请先注册", false);

        User realUser;

        try {
            realUser = userService.getUserByUserName(username);
            if (realUser == null) {
                return errorResult;
            }
        } catch (UsernameNotFoundException e) {
            return errorResult;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());

        try {
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            User userDetails = userService.getUserByUserName(username);

            return new Result("ok", "登录成功", true, userDetails);
        } catch (BadCredentialsException e) {
            return new Result("fail", "密码不正确", false);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        SecurityContextHolder.clearContext();

        return new Result("ok", "已登出", false);
    }


}
