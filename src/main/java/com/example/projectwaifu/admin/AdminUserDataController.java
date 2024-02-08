package com.example.projectwaifu.admin;

import com.example.projectwaifu.security.CustomUserDetails;
import com.example.projectwaifu.user.UserDataRepository;
import com.example.projectwaifu.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/user/data")
public class AdminUserDataController {
    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/setcoins")
    public Map<String, Object> setUserCoins(@RequestParam String username, @RequestParam int newAmount) {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Map.of("coins", userDataRepository.getCoins(currUser.getId()));
    }
}
