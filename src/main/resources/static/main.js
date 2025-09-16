// ツリーを描画
function renderTree(treeData, parentElement) {
    treeData.forEach(node => {
        const li = document.createElement("li");
        li.textContent = node.name;
        li.style.cursor = "pointer";

        // 差分がある場合は色付け
        if (node.hasDiff) {
            li.style.backgroundColor = "#ffe0e0";
        }

        // クリックイベント
        li.addEventListener("click", () => {
            const sourceBlock = document.getElementById("node-source");
            sourceBlock.textContent = node.source || "(ソース未登録)";
        });

        parentElement.appendChild(li);

        if (node.children && node.children.length > 0) {
            const ul = document.createElement("ul");
            li.appendChild(ul);
            renderTree(node.children, ul);
        }
    });
}

// APIからツリー取得
async function loadTree() {
    try {
        const res = await fetch("/api/analysis/tree");
        const data = await res.json();
        const fileTree = document.getElementById("file-tree");
        fileTree.innerHTML = "";
        renderTree(data, fileTree);
    } catch (err) {
        console.error("ツリー取得エラー:", err);
    }
}

// 差分解析の実行
async function runAnalysis() {
    const oldSource = document.getElementById("old-source").value;
    const newSource = document.getElementById("new-source").value;

    try {
        const res = await fetch("/api/analysis/diff", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ oldSource, newSource })
        });

        const diffs = await res.json();
        console.log("差分解析結果:", diffs);

        // TODO: ツリーに差分を反映させる処理
        await loadTree();
    } catch (err) {
        console.error("差分解析エラー:", err);
    }
}

document.getElementById("analyze-btn").addEventListener("click", runAnalysis);

// 初期ロード
loadTree();
