window.onload = () => {// ────────── DOM要素 ──────────
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

// Day2 差分解析ボタン
const day2DiffBtn = document.getElementById("day2-diff-btn");
const LAST_CODE_KEY = "lastAnalyzedCode";

// ────────── サンプルファイルツリー ──────────
const files = [
    {
        name: "Hello.java",
        source: `public class Hello {\n    public void greet(String name) {\n        if (name != null) {\n            System.out.println("Hello, " + name);\n        }\n    }\n}`,
        methods: [
            { name: "greet", status: "removed", source: "void greet(String name) {\n    System.out.println(\"Hello, \" + name);\n}" },
            { name: "greet", status: "added", source: "void greet(String name) {\n    System.out.println(\"Hello, \" + name);\n}" }
        ]
    },
    { name: "Utils.java", methods: [], source: "" }
];

// ────────── ツリー表示 ──────────
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
            methodDiv.onclick = () => showNodeDetail(file, m);
            fileTreeDiv.appendChild(methodDiv);
        });
    });
}

// ────────── 右ペイン表示（構造解析含む） ──────────
async function showNodeDetail(file, method) {
    const source = method.source || file.source || "";
    nodeTitlePre.textContent = `${file.name}.${method.name}`;
    oldSourcePre.textContent = source;
    newSourcePre.textContent = source;

    if (!source) return;

    try {
        const res = await fetch("/api/analysis/parse-structure", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ code: source })
        });
        if (!res.ok) throw new Error("構造解析失敗");

        const json = await res.json();
        day1Result.textContent = JSON.stringify(json, null, 2);

    } catch (err) {
        console.error(err);
        day1Result.textContent = "構造解析エラー: " + err.message;
    }
}

// ────────── 本番用 fetch 差分取得 ──────────
async function fetchDiff(oldSrc, newSrc) {
    const res = await fetch("/api/analysis/diff", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ oldSource: oldSrc, newSource: newSrc })
    });
    if (!res.ok) throw new Error("差分取得失敗");
    return res.json();
}

// ────────── ボタン押下で差分反映 ──────────
runDiffBtn.onclick = async () => {
    const oldSrc = oldSourcePre.textContent;
    const newSrc = newSourcePre.textContent;

    try {
        const diff = await fetchDiff(oldSrc, newSrc);
        if (!diff || diff.length === 0) {
            alert("差分はありません");
            return;
        }

        diff.forEach(d => {
            oldSourcePre.innerHTML = `<span class="removed">${d.oldSource}</span>`;
            newSourcePre.innerHTML = `<span class="added">${d.newSource}</span>`;
        });
    } catch (e) {
        console.error(e);
        alert("差分取得に失敗しました");
    }
};

// ────────── Day1 解析ボタン ──────────
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

        // ✅ localStorage に保存
        localStorage.setItem(LAST_CODE_KEY, code);

    } catch (e) {
        console.error(e);
        day1Result.textContent = "エラー：" + e.message;
    } finally {
        day1Btn.disabled = false;
        day1Status.textContent = "";
    }
};

// ────────── Day2 差分解析ボタン ──────────

day2DiffBtn.addEventListener("click", async () => {
    const oldCode = document.getElementById("day2-old-code").value;
    const newCode = document.getElementById("day2-new-code").value;

    console.log("送信前旧コード:", oldCode);
    console.log("送信前新コード:", newCode);


    try {
        const res = await fetch("/api/analysis/diff", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ oldSource: oldCode, newSource: newCode })
        });
        const data = await res.json();
        console.log("差分結果:", data);
        document.getElementById("day2-result").textContent =
            JSON.stringify(data, null, 2);
    } catch (err) {
        console.error("差分解析エラー:", err);
        document.getElementById("day2-result").textContent =
            "エラーが発生しました: " + err;
    }
});
};

// ────────── 初期レンダリング ──────────
renderTree();
