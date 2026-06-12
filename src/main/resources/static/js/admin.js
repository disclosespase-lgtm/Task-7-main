document.addEventListener("DOMContentLoaded", () => {
    loadUsers();
    initRoles();

    document.getElementById('logoutBtn').addEventListener('click', () => {
        fetch('/api/logout', { method: 'POST' }).then(() => window.location.href = '/login');
    });

    document.getElementById('userForm').addEventListener('submit', handleFormSubmit);
});

let allRoles = [];

// 0. Загрузка всех существующих ролей
function initRoles() {
    fetch('/api/admin/roles')
        .then(res => res.json())
        .then(roles => {
            allRoles = roles;
        })
        .catch(err => console.error("Не удалось получить список ролей:", err));
}

// Построение радиокнопок внутри модального окна
function renderRolesCheckboxes(userRoles = []) {
    const container = document.getElementById('form-roles-container');
    container.innerHTML = '';

    allRoles.forEach(role => {
        const isChecked = userRoles.some(userRole => userRole.id === role.id);
        const label = document.createElement('label');
        label.className = 'role-item';
        label.innerHTML = `
            <input type="radio" name="roles" value="${role.id}" ${isChecked ? 'checked' : ''}>
            <span>${role.role || role.name}</span>
        `;
        container.appendChild(label);
    });
}

// 1. Загрузка таблицы пользователей
function loadUsers() {
    fetch('/api/admin/users')
        .then(res => res.json())
        .then(users => {
            const tbody = document.querySelector('#usersTable tbody');
            tbody.innerHTML = '';

            if (users.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: #aaaaaa;">Нет пользователей</td></tr>`;
                return;
            }

            users.forEach(user => {
                const rolesStr = user.roles ? user.roles.map(r => r.role || r.name).join(' ') : '';
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.firstName || ''}</td>
                    <td>${user.lastName || ''}</td>
                    <td>${user.age || ''}</td>
                    <td>${user.username}</td>
                    <td style="color: #aaaaaa; font-size: 13px;">${rolesStr}</td>
                    <td class="actions-cell">
                        <button class="btn btn-edit">Изменить</button>
                        <button class="btn btn-delete">Удалить</button>
                    </td>
                `;

                tr.querySelector('.btn-edit').addEventListener('click', () => openEditModal(user.id));
                tr.querySelector('.btn-delete').addEventListener('click', () => deleteUser(user.id));
                tbody.appendChild(tr);
            });
        })
        .catch(err => console.error("Ошибка загрузки пользователей:", err));
}

// 2. Открытие окна создания
function openAddModal() {
    document.getElementById('userForm').reset();
    document.getElementById('form-id').value = '';
    document.getElementById('modalTitle').textContent = 'Добавить пользователя';
    document.getElementById('form-password').placeholder = 'Введите пароль';
    document.getElementById('form-password').required = true;

    renderRolesCheckboxes();
    document.getElementById('userModal').style.display = 'flex';
}

// 3. Открытие окна редактирования
function openEditModal(id) {
    fetch(`/api/admin/users/${id}`)
        .then(res => res.json())
        .then(user => {
            document.getElementById('form-id').value = user.id;
            document.getElementById('form-username').value = user.username;
            document.getElementById('form-firstName').value = user.firstName || '';
            document.getElementById('form-lastName').value = user.lastName || '';
            document.getElementById('form-age').value = user.age || '';

            document.getElementById('form-password').value = '';
            document.getElementById('form-password').placeholder = 'Введите новый пароль';
            document.getElementById('form-password').required = true;

            document.getElementById('modalTitle').textContent = 'Редактировать пользователя';
            renderRolesCheckboxes(user.roles || []);
            document.getElementById('userModal').style.display = 'flex';
        });
}

function closeModal() {
    document.getElementById('userModal').style.display = 'none';
}

// 4. Отправка формы (POST / PUT)
function handleFormSubmit(e) {
    e.preventDefault();

    const id = document.getElementById('form-id').value;
    const isEdit = id !== '';
    const passwordValue = document.getElementById('form-password').value;

    // Пароль обязателен всегда
    if (!passwordValue) {
        alert('Введите пароль!');
        return;
    }

    // Роль обязательна
    const selectedRadio = document.querySelector('input[name="roles"]:checked');
    if (!selectedRadio) {
        alert('Выберите роль!');
        return;
    }

    const roleId = parseInt(selectedRadio.value);
    const foundRole = allRoles.find(r => r.id === roleId);

    const userRaw = {
        username: document.getElementById('form-username').value,
        firstName: document.getElementById('form-firstName').value,
        lastName: document.getElementById('form-lastName').value,
        age: parseInt(document.getElementById('form-age').value),
        password: passwordValue,
        roles: [foundRole]
    };

    if (isEdit) userRaw.id = parseInt(id);

    fetch('/api/admin/users', {
        method: isEdit ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userRaw)
    }).then(res => {
        if (!res.ok) {
            alert('Ошибка сохранения!');
            return;
        }
        closeModal();
        loadUsers();
    });
}

// 5. Удаление
function deleteUser(id) {
    if (confirm('Вы уверены, что хотите удалить пользователя?')) {
        fetch(`/api/admin/users/${id}`, { method: 'DELETE' })
            .then(res => {
                if (res.ok) loadUsers();
            });
    }
}