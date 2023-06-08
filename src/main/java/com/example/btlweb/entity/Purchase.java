package com.example.btlweb.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="purchases")
public class Purchase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer so_luong;
    private Timestamp ngay_mua;

    public Integer getId() {
        return id;
    }

    public Timestamp getNgay_mua() {
        return ngay_mua;
    }

    public void setNgay_mua(Timestamp ngay_mua) {
        this.ngay_mua = ngay_mua;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(Integer so_luong) {
        this.so_luong = so_luong;
    }

    public Purchase(Integer id, User user, Book book, Integer so_luong, Timestamp ngay_mua) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.so_luong = so_luong;
        this.ngay_mua = ngay_mua;
    }

    public Purchase() {
    }
}
