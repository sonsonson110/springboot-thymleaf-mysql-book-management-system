package com.example.btlweb.repository;

import com.example.btlweb.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE LOWER(b.tieu_de) = LOWER(:tieuDe) AND LOWER(b.tac_gia) = LOWER(:tacGia) AND b.id <> :id")
    Book findByTieuDeAndTacGiaIgnoreCase(Integer id, String tieuDe, String tacGia);

    @Query("SELECT b FROM Book b WHERE LOWER(b.tieu_de) LIKE %:searchText% OR LOWER(b.tac_gia) LIKE %:searchText%")
    List<Book> searchBooks(@Param("searchText") String searchText);
}
