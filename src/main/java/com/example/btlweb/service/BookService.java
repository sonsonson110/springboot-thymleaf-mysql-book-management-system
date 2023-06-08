package com.example.btlweb.service;

import com.example.btlweb.entity.Book;
import com.example.btlweb.entity.Purchase;
import com.example.btlweb.entity.Review;
import com.example.btlweb.entity.User;
import com.example.btlweb.repository.BookRepository;
import com.example.btlweb.repository.PurchaseRepository;
import com.example.btlweb.repository.ReviewRepository;
import com.example.btlweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId).get();
    }

    public Boolean checkForDupBook(Integer id, String tieuDe, String tacGia) {
        return bookRepository.findByTieuDeAndTacGiaIgnoreCase(id, tieuDe, tacGia) != null;
    }

    public List<Book> searchBook(String searchText) {
        return bookRepository.searchBooks(searchText.toLowerCase());
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBookById(Integer bookId) throws IOException {
        Book book = getBookById(bookId);
//        Path fileToDeletePath = Paths.get("D:\\ptit\\2022-2023\\Nam 2023 - Hoc ki 2\\Lap trinh Web\\btlweb\\src\\main\\resources\\bia\\"+book.getBia_sach());
//        Files.delete(fileToDeletePath);
        bookRepository.delete(book);
    }

    public String saveImageAndGetName(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(
                    "D:\\ptit\\2022-2023\\Nam 2023 - Hoc ki 2\\Lap trinh Web\\btlweb\\src\\main\\resources\\bia",
                    file.getOriginalFilename()
            );
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            return fileNames.toString();
        }
        return null;
    }

    public Review getUserReviewOrNew(Integer userId, Integer bookId) {
        Review review = reviewRepository.findByUser_idAndBook_id(userId, bookId);
        if (review == null)
            return new Review();
        return review;
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId).get();
    }

    public void saveUserReview(Review review, Integer userId, Integer bookId) {
        Book book = getBookById(bookId);
        review.setBook(book);

        User currentUser = getUserById(userId);
        review.setUser(currentUser);

        reviewRepository.save(review);
    }

    public void deleteReviewById(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<Purchase> getUserPurchasedBooks(Integer userId) {
        User user = userRepository.findById(userId).get();
        List<Purchase> purchases = user.getPurchases();
        Collections.reverse(purchases);
        return purchases;
    }

    public void createAndSavePurchase(Purchase purchase, Integer userId, Integer bookId) {
        Book book = getBookById(bookId);
        book.setDa_ban(book.getDa_ban()+purchase.getSo_luong());
        bookRepository.save(book);

        purchase.setUser(getUserById(userId));
        purchase.setBook(book);
        purchase.setNgay_mua(new Timestamp(System.currentTimeMillis()));
        purchaseRepository.save(purchase);
    }

    public void deletePurchaseById(Integer purchaseId) {
        purchaseRepository.deleteById(purchaseId);
    }
}
