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

    @Query("SELECT b FROM Book b WHERE b.category.id = ?1")
    Page<Book> findByCategoryId(Long cateid, PageRequest of);

    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId AND b.id <> :bookId ORDER BY function('RAND')")
    Page<Book> findRelatedBooks(@Param("categoryId") Long categoryId, @Param("bookId") Long bookId, Pageable pageable);

    Page<Book> findAllByOrderByPublishDateDesc(PageRequest of);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(String keyword1, String keyword2,
                                                                               Pageable pageable);

    boolean existsByPublisher_Id(Long id);

    @Query("SELECT b FROM Book b WHERE (:keyword IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:category IS NULL OR b.category.id = :category) AND(:minPrice IS NULL OR b.price >= :minPrice) AND (:maxPrice IS NULL OR b.price <= :maxPrice)")
    Page<Book> searchBooksWithFilter(@Param("keyword") String keyword,  // Thêm @Param ở đây
                                     @Param("category") Integer category,
                                     @Param("minPrice") Double minPrice,
                                     @Param("maxPrice") Double maxPrice,
                                     Pageable pageable);
}
