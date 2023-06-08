package com.example.btlweb.controller;

import com.example.btlweb.entity.Book;
import com.example.btlweb.entity.Review;
import com.example.btlweb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookService service;

    //admin
    @GetMapping("/books")
    String displayBooks(Model model, @RequestParam(required = false) String searchText) {
        model.addAttribute("searchText", searchText);
        if (searchText == null || searchText.isEmpty())
            model.addAttribute("books", service.getAllBooks());
        else
            model.addAttribute("books", service.searchBook(searchText));
        return "modern/admin/books";
    }

    @PostMapping("/search")
    String bookSearch(
            @RequestParam String searchText,
            @RequestParam(required = false) Boolean admin,
            @RequestParam(required = false) String userId,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addAttribute("searchText", searchText);
        if (admin != null)
            redirectAttributes.addFlashAttribute("admin","1");

        return Boolean.TRUE.equals(admin) ? "redirect:/books" : "redirect:/ubooks/"+userId;
    }

    @GetMapping("/book/{bookId}")
    String displayBook(
            @PathVariable Integer bookId, Model model,
            @RequestParam(required = false) String error
    ) {
        model.addAttribute("error", error);
        model.addAttribute("bookId", bookId);
        if (bookId == -1)
            model.addAttribute("book", new Book());
        else {
            model.addAttribute("book", service.getBookById(bookId));
        }
        return "modern/admin/book";
    }

    @PostMapping("/book/save")
    String saveBook(
            Book book,
            @RequestParam("image") MultipartFile file,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        System.out.println("post");
        //kiểm tra db đã có sách trùng td và tg
        if (service.checkForDupBook(-1, book.getTieu_de(), book.getTac_gia())) {
            String encodedError = URLEncoder.encode(String.format("Đã tồn tại sách có tiêu đề %s của tác giả %s", book.getTieu_de(), book.getTac_gia()), StandardCharsets.UTF_8);
            return "redirect:/book/-1?error=" + encodedError;
        }
        //save and redirect
        String fileName = service.saveImageAndGetName(file);
        book.setDa_ban(0);
        book.setBia_sach(fileName);
        service.saveBook(book);
        redirectAttributes.addFlashAttribute("admin", "1");
        return "redirect:/books";
    }

    @PutMapping("/book/save")
    String updateBook(
            Book book,
            @RequestParam("image") MultipartFile file,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        System.out.println("put");

        //kiểm tra db đã có sách trùng td và tg
        if (service.checkForDupBook(book.getId(), book.getTieu_de(), book.getTac_gia())) {
            String encodedError = URLEncoder.encode(String.format("Đã tồn tại sách có tiêu đề %s của tác giả %s", book.getTieu_de(), book.getTac_gia()), StandardCharsets.UTF_8);
            return "redirect:/book/" + book.getId().toString() + "?error=" + encodedError;
        }

        String fileName = service.saveImageAndGetName(file);
        if (fileName != null)
            book.setBia_sach(fileName);
        service.saveBook(book);

        redirectAttributes.addFlashAttribute("admin", "1");
        return "redirect:/books";
    }

    @DeleteMapping("/book/delete/{bookId}")
    String deleteBook(@PathVariable("bookId") Integer bookId, RedirectAttributes redirectAttributes) throws IOException {
        System.out.println("delete");
        service.deleteBookById(bookId);

        redirectAttributes.addFlashAttribute("admin", "1");
        return "redirect:/books";
    }

    //user
    @GetMapping("/ubooks/{userId}")
    String displayUserBooks(Model model, @PathVariable Integer userId, @RequestParam(required = false) String searchText) {
        model.addAttribute("searchText", searchText);
        if (searchText == null || searchText.isEmpty())
            model.addAttribute("books", service.getAllBooks());
        else
            model.addAttribute("books", service.searchBook(searchText));
        model.addAttribute("userId", userId);
        return "modern/user/books";
    }

    @GetMapping("/ubook/{userId}/{bookId}")
    String displayUserBookDetail(
            Model model,
            @PathVariable("bookId") Integer bookId,
            @PathVariable("userId") Integer userId
    ) {
        Book book = service.getBookById(bookId);
        List<Review> allBookReviews = book.getReviews();
        Review review = service.getUserReviewOrNew(userId, bookId);

        model.addAttribute("book", book);
        model.addAttribute("bookReviews", allBookReviews);
        model.addAttribute("review", review);

        return "modern/user/book";
    }

    @PostMapping("/ubook/{userId}/{bookId}")
    String handleUserBooksPost(
            @PathVariable("bookId") String bookId,
            @PathVariable("userId") String userId
    ) {
        return "redirect:/ubook/" + userId + "/" + bookId;
    }
}
