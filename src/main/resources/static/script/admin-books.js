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
        const id = btn.dataset.id;

        // Reset placeholders/values trước khi fetch
        document.querySelectorAll("#editBookModal input, #editBookModal textarea").forEach(el => {
            el.placeholder = "";
        });

        fetch(`/admin/books/api/${id}`)
            .then(response => {
                if (!response.ok) throw new Error("Not found");
                return response.json();
            })
            .then(data => {
                const fill = (id, val, label, type = "text") => {
                    const el = document.getElementById(id);
                    if (!el) return;
                    const isEmpty = (val === null || val === undefined || (typeof val === 'string' && val.trim() === ""));
                    if (isEmpty) {
                        const msg = `không có dữ liệu ${label}`;
                        if (type === "text" || type === "textarea") {
                            el.value = msg;
                        } else {
                            el.value = "";
                            el.placeholder = msg;
                        }
                    } else {
                        el.value = val;
                        el.placeholder = "";
                    }
                };

                const fillSelect = (id, val, label) => {
                    const el = document.getElementById(id);
                    if (!el) return;
                    const temp = el.querySelector('option[data-temp="true"]');
                    if (temp) temp.remove();

                    if (val === null || val === undefined || String(val).trim() === "") {
                        const opt = document.createElement("option");
                        opt.value = "";
                        opt.text = `không có dữ liệu ${label}`;
                        opt.setAttribute("data-temp", "true");
                        el.prepend(opt);
                        el.value = "";
                    } else {
                        el.value = val;
                    }
                };

                document.getElementById("editId").value = data.id || "";

                fill("editTitle", data.title, "tên sách");
                fill("editAuthor", data.authorName, "tác giả");
                fill("editPrice", data.price, "giá bán", "number");
                fill("editQuantity", data.quantity, "tồn kho", "number");

                fill("editPublishDate", data.publishDate, "ngày xuất bản", "date");
                fill("editPageCount", data.pageCount, "số trang", "number");
                fill("editDimensions", data.dimensions, "kích thước");
                fill("editTranslator", data.translator, "dịch giả");

                fillSelect("editCoverType", data.coverType, "loại bìa");
                document.getElementById("editAvailable").value = (data.available !== null && data.available !== undefined) ? data.available.toString() : "true";

                fillSelect("editCategoryId", data.category ? data.category.id : null, "danh mục");
                fillSelect("editPublisherId", data.publisher ? data.publisher.id : null, "nhà xuất bản");

                fill("editDescription", data.description, "mô tả", "textarea");

                document.getElementById("editImageUrl").value = data.image || "";
                const preview = document.getElementById("editPreview");

                if (data.image && data.image.trim() !== "") {
                    preview.src = "/images/books/" + data.image;
                } else {
                    preview.src = "/images/books/default-book.png";
                }
                preview.style.display = "block";
            })
            .catch(error => {
                console.error("Error fetching book details:", error);
                alert("Không thể tải thông tin sách!");
            });
    });
});