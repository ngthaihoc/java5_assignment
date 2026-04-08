package com.fpt.assignment.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Cart;
import com.fpt.assignment.entity.CartDetail;
import com.fpt.assignment.repository.AccountRepository;
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

    @Autowired
    AccountRepository accountRepository;

    /* LẤY DANH SÁCH GIỎ HÀNG */
    public List<CartItemDTO> getCartItems(String email) {

        Cart cart = getOrCreateCart(email);

        return cartDetailRepository.findByCart_Id(cart.getId())
                .stream()
                .map(d -> {
                    CartItemDTO dto = new CartItemDTO();
                    dto.setCartDetailId(d.getId());
                    dto.setBook(d.getBook());
                    dto.setTitle(d.getBook().getTitle());
                    dto.setImage(d.getBook().getImage());
                    dto.setAuthor(d.getBook().getAuthorName());
                    dto.setBook(d.getBook());
                    dto.setPrice(d.getBook().getPrice());
                    dto.setQuantity(d.getQuantity());
                    dto.setTotal(
                            d.getBook().getPrice()
                                    .multiply(BigDecimal.valueOf(d.getQuantity())));
                    return dto;
                })
                .toList();
    }

    /* THÊM SÁCH VÀO GIỎ */
    public void addBook(String email, Long bookId) {

        Cart cart = getOrCreateCart(email);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));

        CartDetail detail = cartDetailRepository.findByCart_IdAndBook_Id(
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

    /* XÓA 1 SẢN PHẨM */
    public void remove(Long cartDetailId) {
        cartDetailRepository.deleteById(cartDetailId);
    }

    /* XÓA TOÀN BỘ GIỎ HÀNG */
    public void clear(String email) {

        Cart cart = getOrCreateCart(email);

        cartDetailRepository.deleteAll(
                cartDetailRepository.findByCart_Id(cart.getId()));
    }

    /* TÍNH TỔNG TIỀN */
    public BigDecimal getTotal(String email) {

        return getCartItems(email).stream()
                .map(CartItemDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addToCart(String email, Long bookId, int quantity) {
        if (quantity < 1) quantity = 1;

        Cart cart = getOrCreateCart(email);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Sách không tồn tại"));

        CartDetail detail = cartDetailRepository.findByCart_IdAndBook_Id(cart.getId(), bookId);

        int currentInCart = (detail == null) ? 0 : detail.getQuantity();
        int newTotal = currentInCart + quantity;

        // Check tồn kho
        if (newTotal > book.getQuantity()) {
            throw new RuntimeException("Chỉ còn " + book.getQuantity() + " cuốn trong kho!");
        }

        if (detail == null) {
            detail = new CartDetail();
            detail.setCart(cart);
            detail.setBook(book);
            detail.setQuantity(quantity);
        } else {
            detail.setQuantity(newTotal);
        }

        cartDetailRepository.save(detail);
    }

    public void updateQuantity(Long cartDetailId, int quantity) {
        CartDetail detail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new RuntimeException("Item không tồn tại"));

        if (quantity <= 0) {
            cartDetailRepository.delete(detail);
            return;
        }

        // Check tồn kho
        int stock = detail.getBook().getQuantity();
        if (quantity > stock) {
            throw new RuntimeException("Chỉ còn " + stock + " cuốn trong kho!");
        }

        detail.setQuantity(quantity);
        cartDetailRepository.save(detail);
    }

    private Cart getOrCreateCart(String email) {
        Cart cart = cartRepository.findByAccount_Email(email);
        if (cart == null) {
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
            cart = new Cart();
            cart.setAccount(account);
            cartRepository.save(cart);
        }
        return cart;
    }
}
