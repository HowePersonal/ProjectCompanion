package com.example.projectwaifu.shop;

import com.example.projectwaifu.user.UserItem;
import com.example.projectwaifu.user.InventoryRepository;
import com.example.projectwaifu.user.UserDataRepository;
import com.example.projectwaifu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ShopManager {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    private int getCoins() {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int currCoins = userDataRepository.getCoins(currUser.getId());
        return currCoins;
    }

    public boolean canPurchase(int purchasePrice) {
        if (purchasePrice > getCoins()) return false;
        return true;
    }

    public boolean purchase(String productID, int purchasePrice) throws Exception {
        try {
            CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userDataRepository.addCoins(currUser.getId(), -purchasePrice);
            inventoryRepository.save(new UserItem(currUser.getId(), productID));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
