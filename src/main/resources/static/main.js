// main.js（完全統合版）

// 要素取得
const fileTreeEl = document.getElementById('file-tree');
const methodDetailEl = document.getElementById('method-detail');
const oldSourceEl = document.getElementById('old-source');
const newSourceEl = document.getElementById('new-source');
const runDiffBtn = document.getElementById('run-diff');

// 差分取得＆レンダリング
async function fetchAndRenderDiff(oldSource, newSource) {
    try {
        const response = await fetch('https://codeleader-production.up.railway.app/api/analysis/diff', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ oldSource, newSource })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const diffData = await response.json();
        renderTree(diffData);
    } catch (err) {
        console.error('差分取得に失敗しました:', err);
        fileTreeEl.innerHTML = '<p style="color:red;">差分取得に失敗しました</p>';
    }
}

// ファイルツリー・メソッド詳細レンダリング
function renderTree(diffData) {
    fileTreeEl.innerHTML = '';
    methodDetailEl.innerHTML = '';

    diffData.forEach((method, idx) => {
        const node = document.createElement('div');
        node.textContent = `${method.className}.${method.methodName} (${method.status})`;
        node.style.cursor = 'pointer';
        node.onclick = () => {
            oldSourceEl.textContent = method.oldSource || '';
            newSourceEl.textContent = method.newSource || '';
        };
        fileTreeEl.appendChild(node);
    });
}

// ボタンイベント
runDiffBtn.addEventListener('click', () => {
    const oldSource = oldSourceEl.value || '';
    const newSource = newSourceEl.value || '';
    fetchAndRenderDiff(oldSource, newSource);
});
