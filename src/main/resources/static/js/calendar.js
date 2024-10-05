function changeContestType() {
    let selectedContestType = document.getElementById("typeSelect").value;
    let url = "/page/contest";
    if (selectedContestType === "ACM") {
        url += "/acm";
    } else if (selectedContestType === "OI") {
        url += "/oi";
    }
    let LcCheckbox = document.getElementById("leetcodeCheckbox");
    let CfCheckbox = document.getElementById("codeforcesCheckbox");
    let NcCheckbox = document.getElementById("nowcoderCheckbox");
    let LgCheckbox = document.getElementById("luoguCheckbox");
    if (LcCheckbox.checked) {
        url += "?platform=leetcode";
    }
    if (CfCheckbox.checked) {
        url += (url.indexOf("?") === -1? "?" : "&") + "platform=codeforces";
    }
    if (NcCheckbox.checked) {
        url += (url.indexOf("?") === -1? "?" : "&") + "platform=nowcoder";
    }
    if (LgCheckbox.checked) {
        url += (url.indexOf("?") === -1 ? "?" : "&") + "platform=luogu";
    }
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json())
        .then(data => updateTable(data))

    function updateTable(contests) {
        let tableBody = document.querySelector("tbody");
        tableBody.innerHTML = '';  // 清空表格内容

        contests.forEach((contest, index) => {
            let row = document.createElement('tr');
            row.className = `${contest.status} ${contest.isToday ? 'today' : 'notoday'}`;

            row.innerHTML = `
                <td>${index + 1}</td>
                <td class="hoverTd">${contest.oj}</td>
                <td class="hoverTd">${contest.name}</td>
                <td>${contest.startTime}</td>
                <td>${contest.duration}</td>
                <td>${contest.status}</td>
            `;
            tableBody.appendChild(row);
        });
    }
}