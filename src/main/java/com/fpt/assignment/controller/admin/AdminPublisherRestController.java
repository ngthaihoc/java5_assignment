package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Publisher;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/publishers")
@RequiredArgsConstructor
public class AdminPublisherRestController {

    final PublisherRepository publisherRepository;
    final BookRepository bookRepository;

    @GetMapping
    public List<Publisher> getPublishers() {
        return publisherRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createPublisher(@RequestBody Publisher pub) {
        return ResponseEntity.ok(publisherRepository.save(pub));
    }

    @PutMapping
    public ResponseEntity<?> updatePublisher(@RequestBody Publisher pub) {
        return ResponseEntity.ok(publisherRepository.save(pub));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable Long id) {
        if (bookRepository.existsByPublisher_Id(id))
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Không thể xóa NXB vì còn sách liên kết!"));
        publisherRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
