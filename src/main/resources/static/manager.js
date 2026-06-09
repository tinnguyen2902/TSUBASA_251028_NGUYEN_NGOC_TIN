
    // Biến trung gian để lưu lại cái form mà người dùng vừa muốn xóa
    let currentDeleteForm = null;

    const modal = document.getElementById('customConfirmModal');
    const btnCancel = document.getElementById('btnModalCancel');
    const btnConfirm = document.getElementById('btnModalConfirm');

    // 1. Khi bấm nút Xóa trên bảng, hiện Modal lên và lưu lại Form
    function openConfirmModal(buttonElement) {
    currentDeleteForm = buttonElement.closest('form'); // Lấy form chứa nút bấm này
    modal.style.display = 'flex'; // Hiện modal ra giữa màn hình
}

    // 2. Khi bấm nút "Hủy bỏ" => Modal ẩn đi
    btnCancel.onclick = function() {
    modal.style.display = 'none';
    currentDeleteForm = null;
}

    // 3. Khi bấm nút Xác nhận trên Modal thì submit cái form đã lưu
    btnConfirm.onclick = function() {
    if (currentDeleteForm) {
    currentDeleteForm.submit(); // Gửi dữ liệu về Java xử lý xóa
}
}

    // 4. Nếu click ra ngoài khung trắng => hộp thoại  đóng
    window.onclick = function(event) {
    if (event.target == modal) {
    modal.style.display = 'none';
    currentDeleteForm = null;
}
}