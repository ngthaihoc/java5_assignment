package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Order;
import com.fpt.assignment.entity.OrderDetails;
import com.fpt.assignment.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderRestController {

    final OrderRepository orderRepository;

    @GetMapping
    public List<Order> getOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer status) {

        List<Order> list = orderRepository.findAll();

        if (search != null && !search.isBlank())
            list = list.stream().filter(o ->
                    o.getId().toString().contains(search) ||
                    (o.getAccount() != null && o.getAccount().getFullname()
                            .toLowerCase().contains(search.toLowerCase()))
            ).collect(Collectors.toList());

        if (status != null)
            list = list.stream().filter(o -> o.getStatus() == status)
                    .collect(Collectors.toList());

        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();

        List<OrderDetails> details = order.getOrderDetails();
        if (details == null) return ResponseEntity.ok(List.of());

        List<Map<String, Object>> result = details.stream()
                .map((OrderDetails d) -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("productName", d.getBook() != null ? d.getBook().getTitle() : "N/A");
                    m.put("price", d.getPrice().doubleValue());
                    m.put("quantity", d.getQuantity());
                    m.put("total", d.getPrice().doubleValue() * d.getQuantity());
                    return m;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> body) {
        Long id = Long.parseLong(body.get("id").toString());
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();
        order.setStatus(Integer.parseInt(body.get("status").toString()));
        order.setPhone(body.get("phone").toString());
        order.setAddress(body.get("address").toString());
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
