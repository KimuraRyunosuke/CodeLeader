// ダミーツリー（本来はサーバAPIから取得）
const treeData = [
    {
        id: "1",
        text: "Hello.java",
        children: [
            {
                id: "1-1",
                text: "greet()",
                diffStatus: "modified",
                source: "void greet(String name) {\n    System.out.println(\"Hello, \" + name);\n}"
            }
        ]
    }
];

// ツリー初期化
$(function () {
    $('#file-tree').jstree({
        core: { data: treeData }
    });

    $('#file-tree').on("select_node.jstree", function (e, data) {
        const node = data.node;
        updateRightPane(node);
    });
});

// 右ペイン更新処理
function updateRightPane(node) {
    document.getElementById("node-title").textContent = node.text;
    let sourceHtml = "";

    if (node.original.source) {
        sourceHtml = renderSource(node.original.source, node.original.diffStatus);
    } else if (node.children && node.children.length > 0) {
        node.children.forEach(childId => {
            const child = $('#file-tree').jstree(true).get_node(childId).original;
            if (child.source) {
                sourceHtml += `<div class="child-title">▼ ${child.text}</div>`;
                sourceHtml += renderSource(child.source, child.diffStatus);
            }
        });
    } else {
        sourceHtml = "<div>(ソース未登録)</div>";
    }
    document.getElementById("node-source").innerHTML = sourceHtml;
}

// ソース描画
function renderSource(source, diffStatus) {
    let html = "";
    source.split("\n").forEach((line, i) => {
        let lineClass = "";
        if (diffStatus === "added") lineClass = "added";
        if (diffStatus === "removed") lineClass = "removed";
        if (diffStatus === "modified") lineClass = "modified";
        html += `<div class="${lineClass}">${i + 1}: ${line}</div>`;
    });
    return html;
}

// =======================
// ✅ Day1 + Day2 追加部分
// =======================
const codeInput = document.getElementById("codeInput");
const parseBtn = document.getElementById("parseBtn");
const diffBtn = document.getElementById("diffBtn");
const status = document.getElementById("status");
const result = document.getElementById("result");

function showStatus(msg, isError = true) {
    status.style.color = isError ? "red" : "green";
    status.textContent = msg;
}

async function postData(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error("サーバエラー: " + response.status);
        return await response.json();
    } catch (err) {
        showStatus("通信エラー: " + err.message);
        throw err;
    }
}

parseBtn.addEventListener("click", async () => {
    const code = codeInput.value.trim();
    if (!code) {
        showStatus("入力が空です。コードを入力してください。");
        return;
    }
    showStatus("解析中...", false);
    try {
        const res = await postData("/api/analysis/parse", { code });
        result.textContent = JSON.stringify(res, null, 2);
        showStatus("解析完了", false);
        localStorage.setItem("lastCode", code);
    } catch {}
});

diffBtn.addEventListener("click", async () => {
    const code = codeInput.value.trim();
    const oldCode = localStorage.getItem("lastCode") || "";
    if (!code) {
        showStatus("入力が空です。コードを入力してください。");
        return;
    }
    if (!oldCode) {
        showStatus("前回のコードがありません。先に構造解析をしてください。");
        return;
    }
    showStatus("差分解析中...", false);
    try {
        const res = await postData("/api/analysis/diff", { oldCode, newCode: code });
        result.textContent = JSON.stringify(res, null, 2);
        showStatus("差分解析完了", false);
        localStorage.setItem("lastCode", code);
    } catch {}
});
