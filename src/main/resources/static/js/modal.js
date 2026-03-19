function openModal() {
    document.getElementById('modal').classList.add('active');
    document.getElementById('modalOverlay').classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('modalOverlay').classList.remove('active');
}