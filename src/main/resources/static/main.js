// ────────── DOM要素 ──────────
const day1Btn = document.getElementById("day1-analyze-btn");
const day1Input = document.getElementById("day1-code-input");
const day1Status = document.getElementById("day1-status");
const day1Result = document.getElementById("day1-result");

const day2Old = document.getElementById("day2-old-code");
const day2New = document.getElementById("day2-new-code");
const day2Btn = document.getElementById("day2-diff-btn");
const day2Result = document.getElementById("day2-result");
const diffResult = document.getElementById("diff-result");

// ────────── Day1 構造解析 ──────────
day1Btn.onclick = async () => {
    const code = day1Input.value.trim();
    if (!code) { alert("コードを入力してください"); return; }

    day1Btn.disabled = true;
    day1Status.textContent = "解析中…";
    day1Result.textContent = "";
    diffResult.textContent = "";

    try {
        const res = await fetch("/api/analysis/parse-structure", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ code })
        });
        if (!res.ok) throw new Error("構造解析に失敗");

        const data = await res.json();
        day1Result.textContent = JSON.stringify(data, null, 2);

    } catch (err) {
        console.error(err);
        day1Result.textContent = "エラー: " + err.message;
    } finally {
        day1Btn.disabled = false;
        day1Status.textContent = "";
    }
};

// ────────── 簡易行単位差分関数 ──────────
function simpleLineDiff(oldCode, newCode) {
    const oldLines = oldCode.split(/\r?\n/);
    const newLines = newCode.split(/\r?\n/);
    const diff = [];

    const maxLen = Math.max(oldLines.length, newLines.length);
    for (let i = 0; i < maxLen; i++) {
        const oldLine = oldLines[i] || "";
        const newLine = newLines[i] || "";
        if (oldLine !== newLine) {
            diff.push({
                line: i + 1,
                oldLine,
                newLine,
                status: oldLine && !newLine ? "removed"
                    : !oldLine && newLine ? "added"
                        : "modified"
            });
        }
    }
    return diff;
}

// ────────── Day2 差分解析 ──────────
day2Btn.onclick = async () => {
    const oldCode = day2Old.value.trim();
    const newCode = day2New.value.trim();
    if (!oldCode || !newCode) { alert("両方のコードを入力してください"); return; }

    day2Btn.disabled = true;
    day2Result.textContent = "差分解析中…";
    diffResult.textContent = "";

    try {
        const res = await fetch("/api/analysis/diff", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ oldSource: oldCode, newSource: newCode })
        });

        if (!res.ok) throw new Error("サーバー差分取得に失敗");

        let data = await res.json();
        console.log("サーバー差分結果:", data);

        // サーバーが空配列ならフロントで簡易差分補完
        if (!data || data.length === 0) {
            data = simpleLineDiff(oldCode, newCode);
            console.log("フロント補完差分:", data);
        }

        day2Result.textContent = JSON.stringify(data, null, 2);

        // 右ペインに差分表示
        let html = "";
        data.forEach(d => {
            if (d.status === "removed") html += `<span class="removed">${d.oldLine}</span>\n`;
            else if (d.status === "added") html += `<span class="added">${d.newLine}</span>\n`;
            else html += `<span class="removed">${d.oldLine}</span>\n<span class="added">${d.newLine}</span>\n`;
        });
        diffResult.innerHTML = html;

    } catch (err) {
        console.error(err);
        day2Result.textContent = "エラー: " + err.message;
        diffResult.textContent = "";
    } finally {
        day2Btn.disabled = false;
    }
};
