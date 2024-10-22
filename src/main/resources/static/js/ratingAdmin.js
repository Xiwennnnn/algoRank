
// 页面加载完成后，初始化页面
window.onload = function() {
    updatePage(1);
}

// 跳转到第一页
function firstPage() {
    updatePage(1);
}

// 跳转到上一页
function previousPage() {
    let currentPage = parseInt(document.getElementById("currentPage").value);
    const totalPage = parseInt(document.getElementById("totalPage").textContent);
    if (currentPage > 1) {
        updatePage(currentPage - 1);
    }
}

// 跳转到下一页
function nextPage() {
    let currentPage = parseInt(document.getElementById("currentPage").value);
    const totalPage = parseInt(document.getElementById("totalPage").textContent);
    if (currentPage < totalPage) {
        updatePage(currentPage + 1);
    }
}

// 跳转到最后一页
function lastPage() {
    const totalPage = parseInt(document.getElementById("totalPage").textContent);
    updatePage(totalPage);
}

// 跳转到指定页
function gotoPage() {  // 修改为不接收参数
    const currentPageInput = document.getElementById("gotoInput"); // 假设你有一个输入框用于跳转到指定页
    const totalPage = parseInt(document.getElementById("totalPage").textContent);

    // 调用验证函数，确保输入合法
    validateInput(currentPageInput);

    let newPage = parseInt(currentPageInput.value);

    if (!isNaN(newPage) && newPage >= 1 && newPage <= totalPage) {
        updatePage(newPage);
    } else {
        currentPageInput.value = 1; // 如果输入无效，恢复到当前页
    }
}

function updatePage(currentPage) {
    const name = document.getElementById("nameInput").value;
    const major = document.getElementById("majorSelect").value;
    const grade = document.getElementById("gradeSelect").value;
    let url = '/admin/api/rating/get?';
    if (name) {
        url += `name=${encodeURIComponent(name)}&`;  // 添加 encodeURIComponent 确保URL正确
    }
    if (major && major !== '全部') {
        url += `major=${encodeURIComponent(major)}&`;
    }
    if (grade && grade !== '全部') {
        url += `grade=${encodeURIComponent(grade)}&`;
    }

    // 发起分页请求并获取当前页的数据
    fetch(url + `current=${currentPage}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("currentPage").value = currentPage;
            document.getElementById("totalPage").textContent = Math.ceil(data.total / data.size);
            renderTable(data);
        })
        .catch(error => {
            console.error("获取分页数据失败:", error);
        });
}

// 渲染表格数据
function renderTable(data) {
    const records = data.records;
    const tbody = document.querySelector('table tbody');
    tbody.innerHTML = '';  // 清空当前表格内容

    records.forEach((user, index) => {
        const currentPage = parseInt(document.getElementById("currentPage").value);
        const tr = document.createElement('tr');
        tr.setAttribute('id', `user-${user.realName}`)
        tr.innerHTML = `
            <td class="Num">${index + 1 + (currentPage - 1) * data.size}</td>
            <td class="RealName">${user.realName}</td>
            <td class="Major"><input type="text" value="${user.major}" /></td>
            <td class="Grade"><input type="text" value="${user.grade}" /></td>
            <td class="LcUsername"><input type="text" value="${user.lcUsername}" /></td>
            <td class="CfUsername"><input type="text" value="${user.cfUsername}" /></td>
            <td class="ChangeBtn"><button onclick="changeUser('${user.realName}')">修改</button></td>
            <td class="DeleteBtn"><button onclick="deleteUser('${user.realName}')">删除</button></td>
        `;
        tbody.appendChild(tr);
    });
}

// 输入验证函数
function validateInput(input) {
    input.value = input.value.replace(/[^0-9]/g, ''); // 只允许输入数字
    input.value = input.value.replace(/^0+/, ''); // 去除前导零

    let pageValue = parseInt(input.value);
    const totalPage = parseInt(document.getElementById("totalPage").textContent);

    if (pageValue > totalPage) {
        input.value = totalPage;
    }

    if (pageValue < 1 || isNaN(pageValue)) {
        input.value = 1;
    }
}

function searchByChoice() {
    const name = document.getElementById("nameInput").value;
    const major = document.getElementById("majorSelect").value;
    const grade = document.getElementById("gradeSelect").value;
    let url = '/admin/api/rating/get?';
    if (name) {
        url += `name=${encodeURIComponent(name)}&`;
    }
    if (major && major !== '全部') {
        url += `major=${encodeURIComponent(major)}&`;
    }
    if (grade && grade !== '全部') {
        url += `grade=${encodeURIComponent(grade)}&`;
    }
    fetch(url)
        .then(response => response.json())
        .then(data => {
            document.getElementById("currentPage").value = 1;
            document.getElementById("totalPage").textContent = Math.ceil(data.total / data.size);
            renderTable(data);
        })
        .catch(error => {
            console.error("获取分页数据失败:", error);
        });
}

function changeUser(realName) {
    if (!confirm('确认修改该用户信息吗？')) {
        return;
    }

    // 确保在选择器前加上#以选择id
    const row = document.querySelector(`#user-${realName}`);

    // 检查row是否存在
    if (!row) {
        alert('未找到用户信息行，请检查用户是否存在。');

        return;
    }
    console.info(row.innerHTML)
    // 获取行中的输入值
    const major = row.querySelector('.Major input').value;
    const grade = row.querySelector('.Grade input').value;
    const lcUsername = row.querySelector('.LcUsername input').value;
    const cfUsername = row.querySelector('.CfUsername input').value;
    console.info(major, grade, lcUsername, cfUsername)
    const userData = {
        realName: realName,
        major: major,
        grade: grade,
        lcUsername: lcUsername,
        cfUsername: cfUsername
    };

    fetch('/admin/api/rating/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    // 刷新页面
     .then(() => {
        updatePage(parseInt(document.getElementById("currentPage").value));
      })
}


function deleteUser(realName) {
    if (!confirm('确认删除该用户吗？')) {
        return;
    }
    fetch(`/admin/api/rating/delete?realName=${realName}`, {
        method: 'DELETE'
    })
     .then(() => {
         updatePage(parseInt(document.getElementById("currentPage").value));
     })
}
