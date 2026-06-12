document.addEventListener("DOMContentLoaded", () => {
    fetchAuthorizedUser();

    document.getElementById('logoutBtn').addEventListener('click', handleLogout);
});

function fetchAuthorizedUser() {
    fetch('/api/user')
        .then(response => {
            if (!response.ok) throw new Error("Не авторизован");
            return response.json();
        })
        .then(user => {
            document.getElementById('u-firstName').textContent = user.firstName || '';
            document.getElementById('u-lastName').textContent = user.lastName || '';
            document.getElementById('u-age').textContent = user.age || '';
            document.getElementById('u-username').textContent = user.username || '';
        })
        .catch(error => {
            console.error(error);
            window.location.href = '/login';
        });
}

function handleLogout() {
    fetch('/api/logout', { method: 'POST' })
        .then(() => {
            window.location.href = '/login';
        });
}