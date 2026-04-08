package com.fpt.assignment.service;

import com.fpt.assignment.dto.CustomerDTO;
import com.fpt.assignment.dto.CustomerDetailDTO;
import com.fpt.assignment.dto.OrderHistoryDTO;
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
        public List<CustomerDTO> getCustomers() {
                return accountRepository.findAllCustomers()
                                .stream()
                                .map(this::toDTO)
                                .toList();
        }

        // ================= SEARCH + FILTER =================
        @Override
        public List<CustomerDTO> searchCustomers(String keyword, String rank, String status) {

                List<CustomerDTO> customers = getCustomers();

                // 1️⃣ TÌM KIẾM
                if (keyword != null && !keyword.isBlank()) {
                        String kw = keyword.toLowerCase();
                        customers = customers.stream()
                                        .filter(c -> c.getName().toLowerCase().contains(kw)
                                                        || c.getEmail().toLowerCase().contains(kw)
                                                        || (c.getPhone() != null && c.getPhone().contains(kw)))
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
                                        .filter(c -> c.isStatus() == active)
                                        .toList();
                }

                return customers;
        }

        // DANH SÁCH
        private CustomerDTO toDTO(Account account) {

                CustomerDTO dto = new CustomerDTO();

                dto.setEmail(account.getEmail());
                dto.setName(account.getFullname());
                dto.setPhone(""); // DB chưa có

                List<Order> orders = account.getOrders();

                // số đơn
                dto.setOrders(orders.size());

                // tổng chi
                BigDecimal total = orders.stream()
                                .flatMap(o -> o.getOrderDetails().stream())
                                .map(od -> od.getPrice()
                                                .multiply(BigDecimal.valueOf(od.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setTotal(total);

                // đơn gần nhất
                LocalDateTime lastOrder = orders.stream()
                                .map(Order::getCreateDate)
                                .max(LocalDateTime::compareTo)
                                .orElse(null);
                dto.setLastOrder(lastOrder);

                // rank
                if (total.compareTo(BigDecimal.valueOf(2_000_000)) >= 0)
                        dto.setRank("Vàng");
                else if (total.compareTo(BigDecimal.valueOf(1_000_000)) >= 0)
                        dto.setRank("Bạc");
                else
                        dto.setRank("Thường");

                // status
                dto.setStatus(account.isEnabled());

                return dto;
        }

        // CHI TIẾT KHÁCH HÀNG
        @Override
        public CustomerDetailDTO getCustomerDetail(String email) {

                Account acc = accountRepository.findById(email).orElseThrow();
                List<Order> orders = acc.getOrders();

                CustomerDetailDTO dto = new CustomerDetailDTO();
                dto.setEmail(acc.getEmail());
                dto.setName(acc.getFullname());
                dto.setAvatar(acc.getAvatar());
                dto.setAddress(
                                orders.stream()
                                                .map(Order::getAddress)
                                                .filter(Objects::nonNull)
                                                .findFirst()
                                                .orElse("—"));

                dto.setTotalOrders(orders.size());

                BigDecimal total = orders.stream()
                                .flatMap(o -> o.getOrderDetails().stream())
                                .map(od -> od.getPrice()
                                                .multiply(BigDecimal.valueOf(od.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setTotalAmount(total);

                LocalDateTime lastOrder = orders.stream()
                                .map(Order::getCreateDate)
                                .max(LocalDateTime::compareTo)
                                .orElse(null);
                dto.setLastOrder(lastOrder);

                dto.setRegisterDate(
                                orders.stream()
                                                .map(Order::getCreateDate)
                                                .min(LocalDateTime::compareTo)
                                                .orElse(null));

                if (total.compareTo(BigDecimal.valueOf(2_000_000)) >= 0)
                        dto.setRank("Vàng");
                else if (total.compareTo(BigDecimal.valueOf(1_000_000)) >= 0)
                        dto.setRank("Bạc");
                else
                        dto.setRank("Thường");

                dto.setStatus(!orders.isEmpty());

                // lịch sử đơn
                List<OrderHistoryDTO> histories = orders.stream().map(o -> {
                        OrderHistoryDTO h = new OrderHistoryDTO();
                        h.setId(o.getId());
                        h.setDate(o.getCreateDate());
                        h.setItemCount(o.getOrderDetails().size());

                        BigDecimal orderTotal = o.getOrderDetails().stream()
                                        .map(od -> od.getPrice()
                                                        .multiply(BigDecimal.valueOf(od.getQuantity())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                        h.setTotal(orderTotal);

                        h.setStatus(
                                        o.getStatus() == 2 ? "Đã giao" : o.getStatus() == 1 ? "Đang giao" : "Mới");
                        return h;
                }).toList();

                dto.setOrders(histories);

                return dto;
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
