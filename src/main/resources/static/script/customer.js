function showCustomerDetail(el) {
    const email = el.dataset.email;

    fetch('/admin/customers/' + email)
        .then(res => res.text())
        .then(html => {
            document.getElementById('customerModalContent').innerHTML = html;
            new bootstrap.Modal(
                document.getElementById('customerModal')
            ).show();
        });
}

function toggleCustomer(el) {
    const email = el.dataset.email;

    if (!confirm('Bạn có chắc muốn thay đổi trạng thái tài khoản?')) return;

    fetch('/admin/customers/' + email + '/toggle', {
        method: 'POST'
    }).then(() => location.reload());
}

function openEditCustomer(el) {
    document.getElementById('editEmail').value = el.dataset.email;
    document.getElementById('editName').value = el.dataset.name;

    new bootstrap.Modal(
        document.getElementById('editCustomerModal')
    ).show();
}


function togglePassword(inputId, btn) {
    const input = document.getElementById(inputId);

    if (input.type === "password") {
        input.type = "text";
        btn.innerText = "🙈";
    } else {
        input.type = "password";
        btn.innerText = "👁️";
    }
}
