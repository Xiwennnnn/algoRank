window.onload = function () {
    changeContestType(1);
}

function changeContestType(currentPage = 1) {
    let selectedContestType = document.getElementById("typeSelect").value;
    let url = "/admin/api/contest/page?";
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
    let table = document.querySelector('tbody');
    table.innerHTML = '';
    contests.forEach(contest => {
        let row = document.createElement('tr');
        row.innerHTML = `
                    <td>${contest.oj}</td>
                    <td>${contest.name}</td>
                    <td>${contest.startTime}</td>
                    <td>${contest.link}</td>
                    <td><button class="btn btn-light" onclick="openModifyModal('${contest.id}')">修改</button></td>
                    <td><button class="btn btn-danger" onclick="DeleteContest('${contest.id}')">删除</button></td>
                `;
        table.appendChild(row);
    });
}

function addContest() {
    let formDiv = document.getElementById('addContestFormDiv');
    let overlay = document.getElementById('overlay');
    formDiv.style.display = 'block';
    overlay.style.display = 'block';
}

function DeleteContest(id) {
    if (!confirm('确认删除该竞赛?')) {
        return;
    }
    let url = '/admin/api/contest/delete?id=' + id;
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json())
        .then(data => {
            if (data === 'success') {
                alert('删除成功');
                window.location.reload();
            } else {
                alert('删除失败');
            }
        })
        .catch(error => console.error(error));
}

function openModifyModal(id) {
    let formDiv = document.getElementById('addContestFormDiv');
    let overlay = document.getElementById('overlay');
    formDiv.style.display = 'block';
    overlay.style.display = 'block';
    let url = '/admin/api/contest/getContestById?id=' + id;
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json())
        .then(data => {
            let form = formDiv.querySelector('form');
            form.querySelector('#id').value = data.id;
            form.querySelector('#oj').value = data.oj;
            form.querySelector('#name').value = data.name;
            form.querySelector('#startTime').value = data.startTime;
            form.querySelector('#endTime').value = data.endTime;
            form.querySelector('#status').value= data.status;
            form.querySelector('#oiContest').value = data.oiContest;
            form.querySelector('#link').value = data.link;
        })
        .catch(error => console.error(error));
}

function closeModifyModal() {
    let formDiv = document.getElementById('addContestFormDiv');
    let overlay = document.getElementById('overlay');
    formDiv.style.display = 'none';
    overlay.style.display = 'none';
}