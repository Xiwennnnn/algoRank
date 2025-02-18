setInterval(function() {
    location.reload();
}, 120000); // 每隔 120000 毫秒刷新一次页面
window.onload = function() {
    changeContestType(1);
}
function changeContestType(currentPage = 1) {
    let selectedContestType = document.getElementById("typeSelect").value;
    let url = "/page/contest?";
    if (selectedContestType === "ACM") {
        url += 'type=1'
    } else if (selectedContestType === "OI") {
        url += 'type=2';
    } else {
        url += 'type=0'
    }
    let isOver = document.getElementById("isOverSelect").value === "isOver";
    if (isOver === true) {
        url += "&isOver=true"
    } else {
        url += "&isOver=false"
    }
    let OthersCheckbox = document.getElementById("othersCheckbox");
    let LcCheckbox = document.getElementById("leetcodeCheckbox");
    let CfCheckbox = document.getElementById("codeforcesCheckbox");
    let NcCheckbox = document.getElementById("nowcoderCheckbox");
    let LgCheckbox = document.getElementById("luoguCheckbox");
    if (OthersCheckbox.checked) {
        url += "&platform=others";
    }
    if (LcCheckbox.checked) {
        url += "&platform=leetcode";
    }
    if (CfCheckbox.checked) {
        url += "&platform=codeforces";
    }
    if (NcCheckbox.checked) {
        url += "&platform=nowcoder";
    }
    if (LgCheckbox.checked) {
        url += "&platform=luogu";
    }
    url += "&currentPage=" + currentPage;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json())
        .then(data => {
            // 根据获取到的数据更新表格内容
            document.getElementById("currentPage").value = currentPage;
            document.getElementById("totalPage").textContent = Math.ceil(data.total / data.size);
            updateTable(data)
        })
}

function updateTable(data) {
    const contests = data.records;
    let tableBody = document.querySelector("tbody");
    tableBody.innerHTML = '';  // 清空表格内容

    contests.forEach((contest, index) => {
        let row = document.createElement('tr');
        row.className = `${contest.status} ${contest.isToday ? 'today' : 'notoday'}`;
        const currentPage = parseInt(document.getElementById("currentPage").value);

        row.innerHTML = `
                <td>${index + 1 + (currentPage - 1) * data.size}</td>
                <td class="hoverTd">${contest.oj}</td>
                <td class="hoverTd">${contest.name}</td>
                <td>${contest.startTime}</td>
                <td>${contest.duration}</td>
                <td>${contest.status}</td>
            `;
        tableBody.appendChild(row);
    });
}

// 跳转到第一页
function firstPage() {
    changeContestType(1);
}

// 跳转到上一页
function previousPage() {
    let currentPage = parseInt(document.getElementById("currentPage").value);
    const totalPage = parseInt(document.getElementById("totalPage").textContent);
    if (currentPage > 1) {
        changeContestType(currentPage - 1);
    }
}

// 跳转到下一页
function nextPage() {
    let currentPage = parseInt(document.getElementById("currentPage").value);
    const totalPage = parseInt(document.getElementById("totalPage").textContent);
    // 获取页面信息
    if (currentPage < totalPage) {
        changeContestType(currentPage + 1);
    }
}

// 跳转到最后一页
function lastPage() {
    const totalPage = parseInt(document.getElementById("totalPage").textContent);
    changeContestType(totalPage);
}

// 跳转到指定页
function gotoPage(currentPage, totalPage ) {
    // 调用验证函数，确保输入合法
    validateInput(currentPage);

    let newPage = parseInt(currentPage.value);

    if (!isNaN(newPage) && newPage >= 1 && newPage <= totalPage) {
        currentPage = newPage;
        changeContestType(currentPage);
    } else {
        currentPage.value = currentPage; // 如果输入无效，恢复到当前页
    }
}

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