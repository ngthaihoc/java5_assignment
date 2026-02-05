package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    List<Book> findAll();

    @Query("SELECT b FROM Book b WHERE b.category.id = ?1 AND b.available = true")
    List<Book> findByCategoryId(Long cateid, PageRequest of);

    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId AND b.id <> :bookId AND b.available = true ORDER BY function('RAND')")
    Page<Book> findRelatedBooks(@Param("categoryId") Long categoryId, @Param("bookId") Long bookId, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.available = true ORDER BY b.publishDate DESC")
    List<Book> findAllByOrderByPublishDateDesc(PageRequest of);

    @Query("SELECT b FROM Book b WHERE b.available = true AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword1, '%')) OR LOWER(b.authorName) LIKE LOWER(CONCAT('%', :keyword2, '%')))")
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(@Param("keyword1") String keyword1,
            @Param("keyword2") String keyword2,
            Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.available = true AND (:keyword IS NULL OR b.title LIKE %:keyword%) AND (:category IS NULL OR b.category.id = :category) AND (:minPrice IS NULL OR b.price >= :minPrice) AND (:maxPrice IS NULL OR b.price <= :maxPrice)")
    Page<Book> searchBooksWithFilter(String keyword, Long category, Integer minPrice, Integer maxPrice,
            Pageable pageable);

    boolean existsByPublisher_Id(Long id);

}
