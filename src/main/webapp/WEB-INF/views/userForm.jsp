<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${empty user.id ? 'Add' : 'Edit'} user</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body{background:linear-gradient(135deg,#eef3ff 0%,#f8fbff 60%,#ffffff 100%)}
        @media (prefers-color-scheme: dark){body{background:#0b0d10}}
        .glass{background:rgba(255,255,255,.7); backdrop-filter:blur(12px); border:1px solid rgba(255,255,255,.4); border-radius:20px}
        @media (prefers-color-scheme: dark){.glass{background:rgba(20,22,26,.6); border-color:rgba(255,255,255,.08)}}
    </style>
</head>
<body>
<div class="container py-5">
    <div class="glass p-4 mx-auto" style="max-width:700px">
        <h3 class="mb-4">${empty user.id ? 'Add user' : 'Edit user'}</h3>
        <form action="${pageContext.request.contextPath}/saveUser" method="post" class="row g-3">
            <input type="hidden" name="id" value="${user.id}"/>
            <div class="col-12">
                <label class="form-label">Name</label>
                <input name="name" class="form-control form-control-lg" value="${user.name}" required>
            </div>
            <div class="col-12">
                <label class="form-label">Email</label>
                <input name="email" type="email" class="form-control form-control-lg" value="${user.email}" required>
            </div>
            <div class="col-sm-6">
                <label class="form-label">Age</label>
                <input name="age" type="number" min="0" class="form-control form-control-lg" value="${user.age}">
            </div>
            <div class="col-12 d-flex gap-2 pt-2">
                <button class="btn btn-primary btn-lg" type="submit"><i class="bi bi-check2-circle me-1"></i>Save</button>
                <a class="btn btn-outline-secondary btn-lg" href="${pageContext.request.contextPath}/users">Cancel</a>
            </div>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</body>
</html>
