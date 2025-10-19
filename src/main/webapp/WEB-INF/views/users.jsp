<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Users</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        :root{--glass-bg:rgba(255,255,255,.7);--glass-brd:rgba(255,255,255,.4)}
        @media (prefers-color-scheme: dark){
            :root{--glass-bg:rgba(20,22,26,.6);--glass-brd:rgba(255,255,255,.08)}
            body{background:#0b0d10}
        }
        body{background:linear-gradient(135deg,#eef3ff 0%,#f8fbff 60%,#ffffff 100%)}
        .glass {
            background: var(--glass-bg);
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            border: 1px solid var(--glass-brd);
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(20,20,20,.08);
        }
        .hero h2{font-weight:700; letter-spacing:.3px}
        .table thead th{font-weight:600; text-transform:uppercase; font-size:.8rem; letter-spacing:.06em}
        .avatar {
            width:36px; height:36px; border-radius:50%;
            display:inline-flex; align-items:center; justify-content:center;
            font-weight:600; background:#e9efff; color:#3651ff;
        }
        @media (prefers-color-scheme: dark){
            .avatar{background:#1b2333; color:#9bb1ff}
        }
        .fab {
            position: fixed; right: 24px; bottom: 24px; z-index: 10;
            box-shadow: 0 10px 20px rgba(0,0,0,.18);
            border-radius: 9999px;
        }
        .empty{padding:48px 16px; color:#848b99}
    </style>
</head>
<body>
<div class="container py-5">
    <div class="d-flex justify-content-between align-items-end hero mb-4">
        <div>
            <h2 class="mb-1">User Directory</h2>
            <div class="text-secondary">CRUD • Spring MVC + JPA</div>
        </div>
        <form class="d-none d-sm-flex gap-2" method="get" action="${pageContext.request.contextPath}/users">
            <input name="q" value="${param.q}" class="form-control form-control-sm" placeholder="Поиск по имени или email">
            <button class="btn btn-sm btn-outline-secondary"><i class="bi bi-search"></i></button>
        </form>
    </div>

    <div class="glass p-0">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                <tr>
                    <th style="width:8%">ID</th>
                    <th style="width:34%">User</th>
                    <th style="width:30%">Email</th>
                    <th style="width:10%">Age</th>
                    <th class="text-end" style="width:18%">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td class="text-muted">#${u.id}</td>
                        <td>
                            <span class="avatar me-2">${fn:toUpperCase(fn:substring(u.name,0,1))}</span>
                            <span class="fw-semibold">${u.name}</span>
                        </td>
                        <td><i class="bi bi-envelope-open text-secondary me-1"></i>${u.email}</td>
                        <td><span class="badge text-bg-light border">${u.age}</span></td>
                        <td class="text-end">
                            <a class="btn btn-sm btn-outline-primary me-1"
                               href="${pageContext.request.contextPath}/userForm?id=${u.id}">
                                <i class="bi bi-pencil-square me-1"></i>Edit
                            </a>
                            <button type="button" class="btn btn-sm btn-outline-danger"
                                    data-bs-toggle="modal" data-bs-target="#confirmDelete"
                                    data-user-id="${u.id}" data-user-name="${u.name}">
                                <i class="bi bi-trash me-1"></i>Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty users}">
                    <tr><td colspan="5" class="text-center empty">
                        <i class="bi bi-people text-secondary fs-3 d-block mb-2"></i>
                        Нет пользователей. Нажмите «Add user».
                    </td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<a class="btn btn-primary fab" href="${pageContext.request.contextPath}/userForm">
    <i class="bi bi-plus-lg me-1"></i> Add user
</a>

<!-- Delete modal -->
<div class="modal fade" id="confirmDelete" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content glass">
            <div class="modal-header border-0">
                <h5 class="modal-title"><i class="bi bi-exclamation-triangle me-2 text-danger"></i>Удалить пользователя</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/deleteUser">
                <div class="modal-body">
                    <input type="hidden" name="id" id="delId">
                    Удалить <span class="fw-semibold" id="delName"></span>?
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>
    const modal = document.getElementById('confirmDelete');
    modal.addEventListener('show.bs.modal', event => {
        const btn = event.relatedTarget;
        document.getElementById('delId').value = btn.getAttribute('data-user-id');
        document.getElementById('delName').textContent = btn.getAttribute('data-user-name');
    });
</script>
</body>
</html>
