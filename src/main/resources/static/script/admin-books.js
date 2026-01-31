/**
 * HÀM XỬ LÝ UPLOAD VÀ PREVIEW ẢNH
 * @param {string} fileInputId - ID của thẻ input type="file"
 * @param {string} hiddenInputId - ID của thẻ input hidden (lưu tên file vào DB)
 * @param {string} previewId - ID của thẻ img hiển thị xem trước
 */
function setupImageUpload(fileInputId, hiddenInputId, previewId) {
    const fileInput = document.getElementById(fileInputId);
    const hiddenInput = document.getElementById(hiddenInputId);
    const previewImage = document.getElementById(previewId);

    if (!fileInput || !hiddenInput || !previewImage) return;

    fileInput.addEventListener("change", function () {
        const file = this.files[0];
        if (!file) return;

        previewImage.src = URL.createObjectURL(file);
        previewImage.style.display = "block";

        const formData = new FormData();
        formData.append("file", file);

        fetch("/admin/books/upload-image", {
            method: "POST",
            body: formData
        })
            .then(res => {
                if (!res.ok) throw new Error("Server error");
                return res.text();
            })
            .then(fileName => {
                hiddenInput.value = fileName;
                console.log("Upload thành công:", fileName);
            })
            .catch(err => {
                console.error("Lỗi upload:", err);
                alert("Không thể upload ảnh. Vui lòng kiểm tra lại Server!");
            });
    });
}

// Kích hoạt cho Modal Thêm
setupImageUpload("addImageFile", "addImageUrl", "addPreview");

// Kích hoạt cho Modal Sửa
setupImageUpload("editImageFile", "editImageUrl", "editPreview");

// ==========================================
// XỬ LÝ DỮ LIỆU KHI MỞ MODAL EDIT
// ==========================================
document.querySelectorAll(".edit-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        // Lấy dữ liệu từ data-attribute của nút Sửa
        const id = btn.dataset.id;
        const title = btn.dataset.title;
        const author = btn.dataset.author;
        const price = btn.dataset.price;
        const quantity = btn.dataset.quantity;
        const image = btn.dataset.image; // Đây là tên file (ví dụ: abc.jpg)

        // Đổ dữ liệu vào Form Edit
        document.getElementById("editId").value = id;
        document.getElementById("editTitle").value = title;
        document.getElementById("editAuthor").value = author;
        document.getElementById("editPrice").value = price;
        document.getElementById("editQuantity").value = quantity;
        
        // Xử lý ảnh
        document.getElementById("editImageUrl").value = image;
        
        const preview = document.getElementById("editPreview");
        if (image && image !== 'null') {
            // Logic ghép đường dẫn phải khớp với HTML: /images/books/ + tên file
            preview.src = "/images/books/" + image;
            preview.style.display = "block";
        } else {
            preview.src = "/images/books/default-book.png";
        }
    });
});