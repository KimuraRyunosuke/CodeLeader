// --- 変数 ---
const fileTreeDiv = document.getElementById('file-tree');
const oldSourcePre = document.getElementById('old-source');
const newSourcePre = document.getElementById('new-source');
const runDiffBtn = document.getElementById('run-diff');

// --- サンプルツリー用関数 ---
function renderTree(diffData) {
    fileTreeDiv.innerHTML = '';
    diffData.forEach(node => {
        const div = document.createElement('div');
        div.textContent = `${node.className}.${node.methodName} (${node.status})`;
        div.style.color = node.status === 'removed' ? 'red' : node.status === 'added' ? 'green' : 'black';
        div.onclick = () => showDetail(node);
        fileTreeDiv.appendChild(div);
    });
}

function showDetail(node) {
    oldSourcePre.textContent = node.oldSource || '';
    newSourcePre.textContent = node.newSource || '';
}

// --- 差分API呼び出し ---
async function fetchAndRenderDiff(oldSrc, newSrc) {
    try {
        const res = await fetch('/api/analysis/diff', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ oldSource: oldSrc, newSource: newSrc })
        });
        const data = await res.json();
        renderTree(data);
    } catch (err) {
        console.error('差分取得に失敗:', err);
        fileTreeDiv.textContent = '差分取得に失敗しました';
    }
}

// --- ボタンイベント ---
runDiffBtn.onclick = () => {
    const oldSrc = oldSourcePre.textContent;
    const newSrc = newSourcePre.textContent;
    fetchAndRenderDiff(oldSrc, newSrc);
};

// --- 初期サンプル ---
oldSourcePre.textContent = 'public class Hello { void greet() {} }';
newSourcePre.textContent = 'public class Hello { void greet(String name) {} }';
fetchAndRenderDiff(oldSourcePre.textContent, newSourcePre.textContent);
