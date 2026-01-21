package com.fpt.assignment.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Cart;
import com.fpt.assignment.entity.CartDetail;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.CartDetailRepository;
import com.fpt.assignment.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    BookRepository bookRepository;

    // Lấy danh sách giỏ hàng
    public List<CartItemDTO> getCartItems(String email) {

        Cart cart = cartRepository.findByAccount_Email(email);

        return cartDetailRepository.findByCart_Id(cart.getId())
                .stream()
                .map(d -> new CartItemDTO(
                        d.getId(),
                        d.getBook().getId(),
                        d.getBook().getTitle(),
                        d.getBook().getAuthorName(),
                        d.getBook().getCategory().getName(),
                        d.getBook().getImage(),
                        d.getBook().getPrice(),
                        d.getQuantity(),
                        d.getBook().getPrice()
                                .multiply(BigDecimal.valueOf(d.getQuantity()))
                ))
                .toList();
    }


    // Thêm sách vào giỏ
    public void addBook(String email, Long bookId) {

        Cart cart = cartRepository.findByAccount_Email(email);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));

        CartDetail detail =
                cartDetailRepository.findByCart_IdAndBook_Id(
                        cart.getId(), bookId);

        if (detail == null) {
            detail = new CartDetail();
            detail.setCart(cart);
            detail.setBook(book);
            detail.setQuantity(1);
        } else {
            detail.setQuantity(detail.getQuantity() + 1);
        }

        cartDetailRepository.save(detail);
    }

    // Cập nhật số lượng
    public void updateQuantity(Long cartDetailId, int quantity) {

        CartDetail detail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new RuntimeException("Item không tồn tại"));

        if (quantity <= 0) {
            cartDetailRepository.delete(detail);
        } else {
            detail.setQuantity(quantity);
            cartDetailRepository.save(detail);
        }
    }

    // Xóa 1 sản phẩm
    public void remove(Long cartDetailId) {
        cartDetailRepository.deleteById(cartDetailId);
    }

    // Xóa toàn bộ giỏ
    public void clear(String email) {

        Cart cart = cartRepository.findByAccount_Email(email);
        cartDetailRepository.deleteAll(
                cartDetailRepository.findByCart_Id(cart.getId()));
    }

    // Tính tổng tiền
    public BigDecimal getTotal(String email) {

        return getCartItems(email).stream()
                .map(CartItemDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
