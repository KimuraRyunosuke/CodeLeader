const fileTreeEl = document.getElementById("file-tree");
const nodeTitleEl = document.getElementById("node-title");
const oldSourceEl = document.getElementById("old-source");
const newSourceEl = document.getElementById("new-source");
const runDiffBtn = document.getElementById("run-diff");

// サンプルソース（ここにユーザ入力でも可）
let sampleOld = `public class Hello { void greet() {} }`;
let sampleNew = `public class Hello { void greet(String name) {} }`;

// ツリー表示
function renderTree(diffJson) {
    fileTreeEl.innerHTML = '';
    diffJson.forEach(node => {
        const div = document.createElement("div");
        div.textContent = `${node.className}.${node.methodName} (${node.status})`;
        div.className = node.status === "added" ? "diff-added" : node.status === "removed" ? "diff-removed" : "";
        div.onclick = () => showNodeDetail(node);
        fileTreeEl.appendChild(div);
    });
}

// 詳細表示
function showNodeDetail(node) {
    nodeTitleEl.textContent = `${node.className}.${node.methodName}`;
    oldSourceEl.textContent = node.oldSource || '';
    newSourceEl.textContent = node.newSource || '';
}

// 本番 URL 対応 fetch
async function fetchDiff(oldSource, newSource) {
    // URLは本番環境に合わせて変更済み
    const baseUrl = window.location.origin; // サーバ上のURLを自動取得
    const res = await fetch(`${baseUrl}/api/analysis/diff`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ oldSource, newSource })
    });
    return await res.json();
}

runDiffBtn.onclick = async () => {
    try {
        const diff = await fetchDiff(sampleOld, sampleNew);
        renderTree(diff);
        if(diff.length > 0) showNodeDetail(diff[0]);
    } catch(err) {
        alert("差分取得に失敗しました");
        console.error(err);
    }
};
