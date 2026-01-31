document.addEventListener("click", function (e) {
    if (e.target.classList.contains("edit-btn")) {
        document.getElementById("editID").value = e.target.dataset.id;
        document.getElementById("editName").value = e.target.dataset.name;
    }
});
