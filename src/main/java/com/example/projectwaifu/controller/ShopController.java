package com.example.projectwaifu.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/shop")
public class ShopController {
}
