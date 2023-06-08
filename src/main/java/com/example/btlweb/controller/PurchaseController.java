package com.example.btlweb.controller;

import com.example.btlweb.entity.Purchase;
import com.example.btlweb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PurchaseController {
    @Autowired
    private BookService service;

    @GetMapping("/purchase/{userId}")
    String displayUserPurchases(Model model, @PathVariable Integer userId) {
        model.addAttribute("purchases", service.getUserPurchasedBooks(userId));
        return "purchase/purchase";
    }

    @PostMapping("/purchase/{userId}/{bookId}")
    String addPurchase(
            Purchase purchase,
            @PathVariable Integer userId,
            @PathVariable Integer bookId
    ) {
        service.createAndSavePurchase(purchase, userId, bookId);
        return "redirect:/ubook/"+userId+"/"+bookId;
    }

    @DeleteMapping("/purchase/{userId}/delete/{pId}")
    String deletePurchase(@PathVariable Integer pId, @PathVariable Integer userId) {
        service.deletePurchaseById(pId);
        return "redirect:/purchase/"+userId;
    }
}
