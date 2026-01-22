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

    /* LẤY DANH SÁCH GIỎ HÀNG*/
    public List<CartItemDTO> getCartItems(String email) {

        Cart cart = cartRepository.findByAccount_Email(email);
        if (cart == null) {
            throw new RuntimeException("Không tìm thấy giỏ hàng");
        }

        return cartDetailRepository.findByCart_Id(cart.getId())
                .stream()
                .map(d -> {
                    CartItemDTO dto = new CartItemDTO();
                    dto.setCartDetailId(d.getId());
                    dto.setBook(d.getBook());
                    dto.setPrice(d.getBook().getPrice());
                    dto.setQuantity(d.getQuantity());
                    dto.setTotal(
                            d.getBook().getPrice()
                                    .multiply(BigDecimal.valueOf(d.getQuantity()))
                    );
                    return dto;
                })
                .toList();
    }


    /* THÊM SÁCH VÀO GIỎ */
    public void addBook(String email, Long bookId) {

        Cart cart = cartRepository.findByAccount_Email(email);
        if (cart == null) {
            throw new RuntimeException("Không tìm thấy giỏ hàng");
        }

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

    /* CẬP NHẬT SỐ LƯỢNG */
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

    /* XÓA 1 SẢN PHẨM */
    public void remove(Long cartDetailId) {
        cartDetailRepository.deleteById(cartDetailId);
    }

    /* XÓA TOÀN BỘ GIỎ HÀNG*/
    public void clear(String email) {

        Cart cart = cartRepository.findByAccount_Email(email);
        if (cart == null) return;

        cartDetailRepository.deleteAll(
                cartDetailRepository.findByCart_Id(cart.getId())
        );
    }

    /* TÍNH TỔNG TIỀN */
    public BigDecimal getTotal(String email) {

        return getCartItems(email).stream()
                .map(CartItemDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addToCart(String email, Long bookId, int quantity) {
        if (quantity < 1) {
            quantity = 1;
        }

        Cart cart = cartRepository.findByAccount_Email(email);
        if (cart == null) {
            throw new RuntimeException("Không tìm thấy giỏ hàng của người dùng");
        }

        CartDetail detail = cartDetailRepository.findByCart_IdAndBook_Id(cart.getId(), bookId);

        if (detail == null) {
            detail = new CartDetail();
            detail.setCart(cart);
            detail.setBook(bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Sách không tồn tại")));
            detail.setQuantity(quantity);
        } else {
            detail.setQuantity(detail.getQuantity() + quantity);
        }

        cartDetailRepository.save(detail);
    }
}
