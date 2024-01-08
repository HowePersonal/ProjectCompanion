package com.example.projectwaifu.controller;

import com.example.projectwaifu.other.ShopManager;
import com.example.projectwaifu.repositories.InventoryRepository;
import com.example.projectwaifu.repositories.UserDataRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ShopManager shopManager;

    Map<String, Integer> productPriceMap = new HashMap<>() {{

    }};
    @PostMapping("/buy")
    public ResponseEntity<Object> purchase(@RequestParam String productId) {
        try {
            if (productPriceMap.get(productId) == null) return new ResponseEntity<>(Map.of("message", "Invalid Product ID"), HttpStatus.BAD_REQUEST);
            int purchasePrice = productPriceMap.get(productId);
            if (!shopManager.canPurchase(purchasePrice)) return new ResponseEntity<>(Map.of("message", "Insufficient funds"), HttpStatus.BAD_REQUEST);
            else if (inventoryRepository.existsByProductId(productId)) return new ResponseEntity<>(Map.of("message", "Item already purchased"), HttpStatus.BAD_REQUEST);

            if (!shopManager.purchase(productId, purchasePrice)) throw new Exception("Purchase failed");
            return new ResponseEntity<>(Map.of("message", "Purchase Success"), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Purchase Failed"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/purchased")
    public List<String> purchased(@RequestParam String productName) {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return inventoryRepository.getPurchased(currUser.getId(), productName);
    }

}
