package com.example.btlweb.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ten_tk;
    private String mat_khau;
    private String email;
    private String ten_nguoi_dung;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Purchase> purchases;

    public User() {
    }

    public User(Integer id, String ten_tk, String mat_khau, String email, String ten_nguoi_dung) {
        this.id = id;
        this.ten_tk = ten_tk;
        this.mat_khau = mat_khau;
        this.email = email;
        this.ten_nguoi_dung = ten_nguoi_dung;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTen_tk() {
        return ten_tk;
    }

    public void setTen_tk(String ten_tk) {
        this.ten_tk = ten_tk;
    }

    public String getMat_khau() {
        return mat_khau;
    }

    public void setMat_khau(String mat_khau) {
        this.mat_khau = mat_khau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTen_nguoi_dung() {
        return ten_nguoi_dung;
    }

    public void setTen_nguoi_dung(String ten_nguoi_dung) {
        this.ten_nguoi_dung = ten_nguoi_dung;
    }

}
