const fileTreeDiv = document.getElementById("file-tree");
const nodeTitlePre = document.getElementById("node-title");
const oldSourcePre = document.getElementById("old-source");
const newSourcePre = document.getElementById("new-source");
const runDiffBtn = document.getElementById("run-diff");

// Day1 用要素
const day1Btn = document.getElementById("day1-analyze-btn");
const day1Input = document.getElementById("day1-code-input");
const day1Status = document.getElementById("day1-status");
const day1Result = document.getElementById("day1-result");

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

// =========================
// Day1 追加 fetch 送信
// =========================
day1Btn.onclick = async () => {
    const code = day1Input.value.trim();
    if (!code) { alert("コードを入力してください"); return; }

    day1Btn.disabled = true;
    day1Status.textContent = "送信中…";

    try {
        const res = await fetch("/api/analysis/parse", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ code })
        });
        if (!res.ok) throw new Error("送信失敗");

        const data = await res.json();
        day1Result.textContent = JSON.stringify(data, null, 2);

    } catch (e) {
        console.error(e);
        day1Result.textContent = "エラー：" + e.message;
    } finally {
        day1Btn.disabled = false;
        day1Status.textContent = "";
    }
};

// =========================
// Day2 差分解析用処理
// =========================

const day2DiffBtn = document.getElementById("day2-diff-btn");
const LAST_CODE_KEY = "lastAnalyzedCode";

// Day1 の解析成功時に localStorage に保存
day1Btn.onclick = async () => {
    const code = day1Input.value.trim();
    if (!code) { alert("コードを入力してください"); return; }

    day1Btn.disabled = true;
    day1Status.textContent = "送信中…";

    try {
        const res = await fetch("/api/analysis/parse", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ code })
        });
        if (!res.ok) throw new Error("送信失敗");

        const data = await res.json();
        day1Result.textContent = JSON.stringify(data, null, 2);

        // ✅ 解析成功 → localStorage に保存
        localStorage.setItem(LAST_CODE_KEY, code);

    } catch (e) {
        console.error(e);
        day1Result.textContent = "エラー：" + e.message;
    } finally {
        day1Btn.disabled = false;
        day1Status.textContent = "";
    }
};

// Day2 差分解析ボタン
document.getElementById("day2-diff-btn").addEventListener("click", async () => {
    const oldCode = document.getElementById("old-code-input").value;
    const newCode = document.getElementById("new-code-input").value;

    try {
        const res = await fetch("/analyze-diff", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ oldCode, newCode }),
        });

        const data = await res.json();

        // Console 出力（デバッグ用）
        console.log("差分結果:", data);

        // ★ Day2 サーバー応答（JSON表示）に反映
        document.getElementById("diff-result").textContent =
            JSON.stringify(data, null, 2);

    } catch (err) {
        console.error("差分解析エラー:", err);
        document.getElementById("diff-result").textContent =
            "エラーが発生しました: " + err;
    }
});



// 初期レンダリング
renderTree();
