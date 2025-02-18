setInterval(function() {
    location.reload();
}, 120000); // 每隔 120000 毫秒刷新一次页面
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
    // 获取页面信息
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
function gotoPage(currentPage, totalPage ) {
    // 调用验证函数，确保输入合法
    validateInput(currentPage);

    let newPage = parseInt(currentPage.value);

    if (!isNaN(newPage) && newPage >= 1 && newPage <= totalPage) {
        currentPage = newPage;
        updatePage(currentPage);
    } else {
        currentPage.value = currentPage; // 如果输入无效，恢复到当前页
    }
}

function updatePage(currentPage) {
    const name = document.getElementById("nameInput").value;
    const major = document.getElementById("majorSelect").value;
    const grade = document.getElementById("gradeSelect").value;
    let url = '/api/rating/lcs/page?';
    if (name) {
        url += `name=${name}&`;
    }
    if (major && major !== '全部') {
        url += `major=${major}&`;
    }
    if (grade && grade !== '全部') {
        url += `grade=${grade}&`;
    }
    // 发起分页请求并获取当前页的数据
    fetch(url + `current=${currentPage}`)
        .then(response => response.json())
        .then(data => {
            // 根据获取到的数据更新表格内容
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

    // 遍历数据并生成新的表格行
    records.forEach((user, index) => {
        const currentPage = parseInt(document.getElementById("currentPage").value);
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td class="RankNum">${index + 1 + (currentPage - 1) * data.size}</td>
            <td class="RankUser">${user.userName}</td>
            <td class="RankName">${user.realName}</td>
            <td class="RankScore">${user.rating}</td>
            <td class="RankLevel"><span class="${user.grading}">${user.grading}</span></td>
            <td class="RankGrade">${user.grade}</td>
            <td class="RankMajor">${user.major}</td>
        `;

        tbody.appendChild(tr);
    });
}

// // 根据当前页更新按钮的禁用状态
// function updateButtons() {
//     document.querySelector('button[onclick="firstPage()"]').disabled = currentPage <= 1;
//     document.querySelector('button[onclick="previousPage()"]').disabled = currentPage <= 1;
//     document.querySelector('button[onclick="nextPage()"]').disabled = currentPage >= totalPage;
//     document.querySelector('button[onclick="lastPage()"]').disabled = currentPage >= totalPage;
// }

// 输入验证函数：只允许输入正整数，并确保页码在总页数范围内
function validateInput(input) {
    // 只允许输入数字
    input.value = input.value.replace(/[^0-9]/g, '');

    // 去除前导零
    input.value = input.value.replace(/^0+/, '');

    // 获取当前输入的页码和总页数
    let pageValue = parseInt(input.value);
    const totalPage = parseInt(document.getElementById("totalPage").textContent);

    // 如果输入的页码超出总页数，则将其限制在总页数内
    if (pageValue > totalPage) {
        input.value = totalPage;
    }

    // 如果输入的页码小于1，则限制为1
    if (pageValue < 1 || isNaN(pageValue)) {
        input.value = 1;
    }
}

function searchByChoice() {
    const name = document.getElementById("nameInput").value;
    const major = document.getElementById("majorSelect").value;
    const grade = document.getElementById("gradeSelect").value;
    console.log(name, major, grade);
    let url = '/api/rating/lcs/page?';
    if (name) {
        url += `name=${name}&`;
    }
    if (major && major!== '全部') {
        url += `major=${major}&`;
    }
    if (grade && grade !== '全部') {
        url += `grade=${grade}&`;
    }
    fetch(url)
        .then(response => response.json())
        .then(data => {
            // 根据获取到的数据更新表格内容
            document.getElementById("currentPage").value = 1;
            document.getElementById("totalPage").textContent = Math.ceil(data.total / data.size);
            renderTable(data);
        })
        .catch(error => {
            console.error("获取分页数据失败:", error);
        });
}