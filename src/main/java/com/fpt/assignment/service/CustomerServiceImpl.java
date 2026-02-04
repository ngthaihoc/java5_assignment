package com.fpt.assignment.service;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

        @Autowired
        AccountRepository accountRepository;

        // ================= DANH SÁCH =================
        @Override
        public List<Account> getCustomers() {
                return accountRepository.findAllCustomers()
                                .stream()
                                .peek(this::enrichAccountData)
                                .toList();
        }

        // ================= SEARCH + FILTER =================
        @Override
        public List<Account> searchCustomers(String keyword, String rank, String status) {

                List<Account> customers = getCustomers();

                // 1️⃣ TÌM KIẾM
                if (keyword != null && !keyword.isBlank()) {
                        String kw = keyword.toLowerCase();
                        customers = customers.stream()
                                        .filter(c -> c.getFullname().toLowerCase().contains(kw)
                                                        || c.getEmail().toLowerCase().contains(kw))
                                        .toList();
                }

                // LỌC HẠNG
                if (rank != null && !rank.isBlank()) {
                        customers = customers.stream()
                                        .filter(c -> rank.equals(c.getRank()))
                                        .toList();
                }

                // LỌC TRẠNG THÁI
                if (status != null && !status.isBlank()) {
                        boolean active = status.equals("active");
                        customers = customers.stream()
                                        .filter(c -> c.isEnabled() == active)
                                        .toList();
                }

                return customers;
        }

        // POPULATE TRANSIENT FIELDS
        private void enrichAccountData(Account account) {
                List<Order> orders = account.getOrders();

                // số đơn
                account.setOrderCount(orders.size());

                // tổng chi
                BigDecimal total = orders.stream()
                                .flatMap(o -> o.getOrderDetails().stream())
                                .map(od -> od.getPrice()
                                                .multiply(BigDecimal.valueOf(od.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                account.setTotalSpent(total);

                // đơn gần nhất
                LocalDateTime lastOrder = orders.stream()
                                .map(Order::getCreateDate)
                                .max(LocalDateTime::compareTo)
                                .orElse(null);
                account.setLatestOrder(lastOrder);

                // rank
                if (total.compareTo(BigDecimal.valueOf(2_000_000)) >= 0)
                        account.setRank("Vàng");
                else if (total.compareTo(BigDecimal.valueOf(1_000_000)) >= 0)
                        account.setRank("Bạc");
                else
                        account.setRank("Thường");
        }

        // CHI TIẾT KHÁCH HÀNG
        @Override
        public Account getCustomerDetail(String email) {
                Account acc = accountRepository.findById(email).orElseThrow();
                enrichAccountData(acc);

                // Address logic
                acc.setAddress(acc.getOrders().stream()
                                .map(Order::getAddress)
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse("—"));

                return acc;
        }

        @Override
        public void toggleStatus(String email) {
                Account acc = accountRepository.findById(email)
                                .orElseThrow(() -> new RuntimeException("Account not found"));

                acc.setEnabled(!acc.isEnabled());
                accountRepository.save(acc);
        }

        @Override
        public void updateCustomer(String email, String fullname, String password) {

                Account acc = accountRepository.findById(email)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

                acc.setFullname(fullname);
                if (password != null && !password.isBlank()) {
                        acc.setPassword(password);
                }

                accountRepository.save(acc);
        }

        @Override
        public void createCustomer(String email, String fullname, String password) {

                if (accountRepository.existsById(email)) {
                        throw new RuntimeException("Email đã tồn tại");
                }

                Account acc = new Account();
                acc.setEmail(email);
                acc.setFullname(fullname);
                acc.setPassword(password);
                acc.setAdmin(false);
                acc.setEnabled(true);

                accountRepository.save(acc);
        }

}
