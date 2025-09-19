// ────────── Day1 構造解析 ──────────
const day1Btn = document.getElementById("day1-analyze-btn");
const day1Input = document.getElementById("day1-code-input");
const day1Status = document.getElementById("day1-status");
const day1Result = document.getElementById("day1-result");
const diffResult = document.getElementById("diff-result");

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
        const jsonText = JSON.stringify(data, null, 2);
        day1Result.textContent = jsonText;
        diffResult.textContent = jsonText; // 右ペインにも表示

    } catch (err) {
        console.error(err);
        day1Result.textContent = "構造解析エラー: " + err.message;
        diffResult.textContent = "構造解析エラー: " + err.message;
    } finally {
        day1Btn.disabled = false;
        day1Status.textContent = "";
    }
};

// ────────── Day2 差分解析（簡易行単位） ──────────
const oldCode = document.getElementById("day2-old-code");
const newCode = document.getElementById("day2-new-code");
const day2Btn = document.getElementById("day2-diff-btn");
const day2Result = document.getElementById("day2-result");

day2Btn.onclick = async () => {
    const oldSrc = oldCode.value.trim().split("\n");
    const newSrc = newCode.value.trim().split("\n");
    if (!oldSrc.length || !newSrc.length) { alert("両方のコードを入力してください"); return; }

    day2Btn.disabled = true;
    day2Result.textContent = "差分解析中…";
    diffResult.textContent = "";

    try {
        // 簡易行単位差分
        const diffLines = [];
        const maxLines = Math.max(oldSrc.length, newSrc.length);
        for (let i = 0; i < maxLines; i++) {
            const oldLine = oldSrc[i] || "";
            const newLine = newSrc[i] || "";
            if (oldLine !== newLine) {
                diffLines.push({
                    line: i+1,
                    oldSource: oldLine,
                    newSource: newLine
                });
            }
        }

        day2Result.textContent = JSON.stringify(diffLines, null, 2);

        // 右ペインに簡易プレビュー表示
        const preview = diffLines.map(d => {
            return `[行 ${d.line}] - ${d.oldSource}\n+ ${d.newSource}`;
        }).join("\n");
        diffResult.textContent = preview || "差分なし";

    } catch (err) {
        console.error(err);
        day2Result.textContent = "差分解析エラー: " + err.message;
        diffResult.textContent = "差分解析エラー: " + err.message;
    } finally {
        day2Btn.disabled = false;
    }
};
