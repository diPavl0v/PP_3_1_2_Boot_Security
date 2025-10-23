function readCookie(name) {
    return document.cookie.split('; ').reduce((acc, c) => {
        const [k, v] = c.split('=');
        return k === name ? decodeURIComponent(v) : acc;
    }, null);
}
const csrfHeader = 'X-XSRF-TOKEN'; // CookieCsrfTokenRepository header
function fx(url, opt = {}) {
    opt.headers = opt.headers || {};
    opt.headers[csrfHeader] = readCookie('XSRF-TOKEN') || '';
    if (!(opt.body instanceof FormData) && opt.body && !opt.headers['Content-Type']) {
        opt.headers['Content-Type'] = 'application/json';
    }
    return fetch(url, opt).then(async r => {
        if (!r.ok) {
            const text = await r.text();
            throw new Error(text || r.statusText);
        }
        const ct = r.headers.get('content-type') || '';
        return ct.includes('application/json') ? r.json() : r.text();
    });
}

const tbody = document.getElementById('usersTbody');
const btnNew = document.getElementById('btnNew');
const modalEl = document.getElementById('userModal');
const modal = new bootstrap.Modal(modalEl);
const form = document.getElementById('userForm');
const alertBox = document.getElementById('alertBox');

const f = {
    id: document.getElementById('uId'),
    username: document.getElementById('uUsername'),
    password: document.getElementById('uPassword'),
    first: document.getElementById('uFirst'),
    last: document.getElementById('uLast'),
    age: document.getElementById('uAge'),
    rolesBox: document.getElementById('rolesBox'),
    error: document.getElementById('formError'),
    title: document.getElementById('modalTitle'),
    btnSave: document.getElementById('btnSave')
};

let allRoles = []; // [{id, name}]
let editingId = null;

async function loadRoles() {
    allRoles = await fx('/api/roles');
    f.rolesBox.innerHTML = '';
    allRoles.forEach(r => {
        const id = `role_${r.id}`;
        const block = document.createElement('div');
        block.className = 'form-check form-check-inline';
        block.innerHTML = `
      <input class="form-check-input" type="checkbox" value="${r.name}" id="${id}">
      <label class="form-check-label" for="${id}">${r.name}</label>`;
        f.rolesBox.appendChild(block);
    });
}

function getSelectedRoles() {
    return [...f.rolesBox.querySelectorAll('input[type=checkbox]:checked')]
        .map(i => ({ name: i.value }));
}

function setSelectedRoles(roleNames) {
    const set = new Set(roleNames);
    f.rolesBox.querySelectorAll('input[type=checkbox]').forEach(i => {
        i.checked = set.has(i.value);
    });
}

function renderUsers(users) {
    tbody.innerHTML = '';
    users.forEach(u => {
        const tr = document.createElement('tr');
        const roles = (u.roles || []).map(r => r.name).join(', ');
        tr.innerHTML = `
      <td>${u.id ?? ''}</td>
      <td>${u.username ?? ''}</td>
      <td>${u.firstName ?? ''}</td>
      <td>${u.lastName ?? ''}</td>
      <td>${u.age ?? ''}</td>
      <td>${roles}</td>
      <td class="text-end">
        <button class="btn btn-sm btn-outline-primary me-2" data-act="edit" data-id="${u.id}">Edit</button>
        <button class="btn btn-sm btn-outline-danger" data-act="del" data-id="${u.id}">Delete</button>
      </td>`;
        tbody.appendChild(tr);
    });
}

async function loadUsers() {
    const list = await fx('/api/users');
    renderUsers(list);
}

tbody.addEventListener('click', async e => {
    const b = e.target.closest('button[data-act]');
    if (!b) return;
    const id = b.getAttribute('data-id');
    if (b.dataset.act === 'edit') {
        const u = await fx(`/api/users/${id}`);
        openEdit(u);
    } else if (b.dataset.act === 'del') {
        if (confirm('Delete user?')) {
            await fx(`/api/users/${id}`, { method: 'DELETE' });
            await loadUsers();
            flash('Deleted', 'warning');
        }
    }
});

function clearForm() {
    editingId = null;
    f.id.value = '';
    f.username.value = '';
    f.password.value = '';
    f.first.value = '';
    f.last.value = '';
    f.age.value = '';
    setSelectedRoles([]);
    f.error.classList.add('d-none');
    f.error.textContent = '';
}

function openCreate() {
    clearForm();
    f.title.textContent = 'New user';
    f.btnSave.textContent = 'Create';
    modal.show();
}

function openEdit(u) {
    clearForm();
    editingId = u.id;
    f.id.value = u.id;
    f.username.value = u.username || '';
    f.password.value = '';
    f.first.value = u.firstName || '';
    f.last.value = u.lastName || '';
    f.age.value = u.age ?? '';
    setSelectedRoles((u.roles || []).map(r => r.name));
    f.title.textContent = `Edit #${u.id}`;
    f.btnSave.textContent = 'Update';
    modal.show();
}

form.addEventListener('submit', async e => {
    e.preventDefault();
    try {
        const payload = {
            username: f.username.value.trim(),
            firstName: f.first.value.trim(),
            lastName: f.last.value.trim(),
            age: f.age.value ? Number(f.age.value) : null,
            roles: getSelectedRoles()
        };
        const pass = f.password.value;
        if (pass && pass.trim()) payload.password = pass.trim();

        if (editingId == null) {
            if (!payload.password) throw new Error('Password required');
            await fx('/api/users', { method: 'POST', body: JSON.stringify(payload) });
            flash('Created', 'success');
        } else {
            await fx(`/api/users/${editingId}`, { method: 'PUT', body: JSON.stringify(payload) });
            flash('Updated', 'success');
        }
        await loadUsers();
        modal.hide();
    } catch (err) {
        f.error.textContent = err.message || 'Error';
        f.error.classList.remove('d-none');
    }
});

btnNew.addEventListener('click', openCreate);

function flash(message, type = 'info') {
    alertBox.className = `alert alert-${type}`;
    alertBox.textContent = message;
    alertBox.classList.remove('d-none');
    setTimeout(() => alertBox.classList.add('d-none'), 2500);
}

(async function init() {
    await loadRoles();
    await loadUsers();
})();
