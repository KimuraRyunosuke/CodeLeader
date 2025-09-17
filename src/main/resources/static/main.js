const fileTreeDiv = document.getElementById("file-tree");
const nodeTitlePre = document.getElementById("node-title");
const oldSourcePre = document.getElementById("old-source");
const newSourcePre = document.getElementById("new-source");
const runDiffBtn = document.getElementById("run-diff");

// サンプルファイルツリー
const files = [
    { name: "Hello.java", methods: [
            { name: "greet", status: "removed" },
            { name: "greet", status: "added" }
        ]},
    { name: "Utils.java", methods: [] }
];

// ツリー表示
function renderTree() {
    fileTreeDiv.innerHTML = "";
    files.forEach(file => {
        const fileDiv = document.createElement("div");
        fileDiv.textContent = file.name;
        fileDiv.style.fontWeight = "bold";
        fileTreeDiv.appendChild(fileDiv);

        file.methods.forEach(m => {
            const methodDiv = document.createElement("div");
            methodDiv.textContent = `${file.name}.${m.name} (${m.status})`;
            methodDiv.className = m.status;
            methodDiv.style.paddingLeft = "15px";
            methodDiv.style.cursor = "pointer";
            methodDiv.onclick = () => showNodeDetail(file.name, m);
            fileTreeDiv.appendChild(methodDiv);
        });
    });
}

// 右ペイン表示
function showNodeDetail(fileName, method) {
    nodeTitlePre.textContent = `${fileName}.${method.name}`;
    // 旧・新ソースは事前の内容を保持
}

// 本番用 fetch 差分取得
async function fetchDiff(oldSrc, newSrc) {
    const res = await fetch("/api/analysis/diff", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ oldSource: oldSrc, newSource: newSrc })
    });
    if (!res.ok) throw new Error("差分取得失敗");
    return res.json();
}

// ボタン押下で差分反映
runDiffBtn.onclick = async () => {
    const oldSrc = oldSourcePre.textContent;
    const newSrc = newSourcePre.textContent;

    try {
        const diff = await fetchDiff(oldSrc, newSrc);
        if (!diff || diff.length === 0) {
            alert("差分はありません");
            return;
        }
        // 右ペインに差分を反映
        diff.forEach(d => {
            oldSourcePre.innerHTML = `<span class="removed">${d.oldSource}</span>`;
            newSourcePre.innerHTML = `<span class="added">${d.newSource}</span>`;
        });
    } catch (e) {
        console.error(e);
        alert("差分取得に失敗しました");
    }
};

// 初期レンダリング
renderTree();
