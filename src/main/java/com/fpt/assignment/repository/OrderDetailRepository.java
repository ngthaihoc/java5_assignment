package com.fpt.assignment.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpt.assignment.entity.OrderDetails;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
        @Query("SELECT d.book.category.name, SUM(d.price * d.quantity), SUM(d.quantity), " +
                        "MAX(d.price), MIN(d.price), AVG(d.price) " +
                        "FROM OrderDetails d GROUP BY d.book.category.name")
        List<Object[]> reportByCategory();

        // bán chạy nhất/thấp nhất
        @Query("SELECT d.book, SUM(d.quantity) FROM OrderDetails d " +
                        "GROUP BY d.book ORDER BY SUM(d.quantity) DESC")
        List<Object[]> findTopSellingBooks(PageRequest of);

        @Query("SELECT d.book, SUM(d.quantity) FROM OrderDetails d " +
                        "GROUP BY d.book ORDER BY SUM(d.quantity) ASC")
        List<Object[]> findLowestSellingBooks(PageRequest of);

        // 10 khách hàng VIP
        @Query("SELECT d.order.account.fullname, SUM(d.price * d.quantity), " +
                        "MIN(d.order.createDate), MAX(d.order.createDate) " +
                        "FROM OrderDetails d GROUP BY d.order.account.fullname " +
                        "ORDER BY SUM(d.price * d.quantity) DESC")
        List<Object[]> reportVIPCustomers(PageRequest of);

        List<OrderDetails> findByOrder_Id(Long orderId);

        @Query("SELECT SUM(od.quantity) FROM OrderDetails od")
        Long sumTotalBooks();

        @Query("SELECT d.book, SUM(d.quantity) FROM OrderDetails d " +
                        "WHERE d.book.title LIKE %:keyword% " +
                        "GROUP BY d.book ORDER BY SUM(d.quantity) DESC")
        List<Object[]> findSaleStatsByKeyword(
                        @org.springframework.web.bind.annotation.RequestParam("keyword") String keyword);
}
