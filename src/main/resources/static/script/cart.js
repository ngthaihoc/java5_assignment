function updateQty(id, delta) {
    fetch('/cart/update-ajax', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id,
            delta: delta
        })
    })
        .then(() => {
            location.reload();
        });
}

function removeItem(id) {
    if (!confirm('Xóa sản phẩm này?')) return;

    fetch('/cart/remove-ajax/' + id, {
        method: 'DELETE'
    })
        .then(() => {
            alert('Xóa sản phẩm thành công');
            location.reload();
        });
}

function clearCart() {
    if (!confirm('Xóa toàn bộ giỏ hàng?')) return;

    fetch('/cart/clear-ajax', {
        method: 'DELETE'
    })
        .then(() => {
            alert('Đã xóa toàn bộ giỏ hàng');
            location.reload();
        });
}
