
// UPLOAD ẢNH (ADD)
document.getElementById("addImageFile")?.addEventListener("change", function () {
    uploadImage(this.files[0], "addImageUrl", "addPreview");
});

// UPLOAD ẢNH (EDIT)
document.getElementById("editImageFile")?.addEventListener("change", function () {
    uploadImage(this.files[0], "editImageUrl", "editPreview");
});

function uploadImage(file, inputId, previewId) {
    const formData = new FormData();
    formData.append("file", file);

    fetch("/admin/upload-image", {
        method: "POST",
        body: formData
    })
        .then(res => res.text())
        .then(url => {
            document.getElementById(inputId).value = url;
            document.getElementById(previewId).src = url;
        });
}

//MODAL EDIT
document.querySelectorAll(".edit-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        document.getElementById("editId").value = btn.dataset.id;
        document.getElementById("editTitle").value = btn.dataset.title;
        document.getElementById("editAuthor").value = btn.dataset.author;
        document.getElementById("editPrice").value = btn.dataset.price;
        document.getElementById("editQuantity").value = btn.dataset.quantity;
        document.getElementById("editImageUrl").value = btn.dataset.image;
        document.getElementById("editPreview").src = btn.dataset.image;
    });
});

// =======================
// UPLOAD + PREVIEW ẢNH
// =======================
function handleImageUpload(fileInputId, hiddenInputId, previewId) {
    const fileInput = document.getElementById(fileInputId);

    fileInput?.addEventListener("change", function () {
        const file = this.files[0];
        if (!file) return;

        /* ✅ PREVIEW ẢNH LOCAL (QUAN TRỌNG) */
        const preview = document.getElementById(previewId);
        preview.src = URL.createObjectURL(file);
        preview.style.display = "block";

        /* ✅ UPLOAD LÊN SERVER */
        const formData = new FormData();
        formData.append("file", file);

        fetch("/admin/upload-image", {
            method: "POST",
            body: formData
        })
            .then(res => res.text())
            .then(url => {
                document.getElementById(hiddenInputId).value = url;
            })
            .catch(err => {
                console.error("Upload lỗi", err);
            });
    });
}

// ADD
handleImageUpload("addImageFile", "addImageUrl", "addPreview");

// EDIT
handleImageUpload("editImageFile", "editImageUrl", "editPreview");

