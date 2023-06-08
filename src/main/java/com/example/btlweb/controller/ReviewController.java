package com.example.btlweb.controller;

import com.example.btlweb.entity.Review;
import com.example.btlweb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {
    @Autowired
    private BookService service;

    @PostMapping("/save/review")
    String saveNewReview(
            @ModelAttribute("review") Review review,
            @ModelAttribute("userId") Integer userId,
            @ModelAttribute("bookId") Integer bookId,
            RedirectAttributes redirectAttributes
    ) {
        service.saveUserReview(review, userId, bookId);
        redirectAttributes.addFlashAttribute("userId", userId);
        return "redirect:/ubook/"+userId+"/"+bookId;
    }

    @DeleteMapping("/ubook/delete/{userId}/{bookId}/{reviewId}")
    String handleReviewDelete(
            @PathVariable Integer reviewId,
            @PathVariable("bookId") Integer bookId,
            @PathVariable("userId") Integer userId
    ) {
        service.deleteReviewById(reviewId);
        return "redirect:/ubook/"+userId+"/"+bookId;
    }
}
