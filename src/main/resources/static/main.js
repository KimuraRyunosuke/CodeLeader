// main.js

// API から差分取得して JSON を返す
async function fetchDiff(oldSource, newSource) {
    const response = await fetch('https://codeleader-production.up.railway.app/api/analysis/diff', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ oldSource, newSource })
    });
    if (!response.ok) {
        throw new Error('差分取得に失敗しました: ' + response.status);
    }
    return await response.json();
}

// ツリー描画（メソッド一覧）
function renderTree(methods) {
    const tree = document.getElementById('file-tree');
    tree.innerHTML = ''; // 初期化
    methods.forEach((m, idx) => {
        const div = document.createElement('div');
        div.textContent = `${m.className}.${m.methodName} (${m.status})`;
        div.style.cursor = 'pointer';
        div.onclick = () => renderMethodDetail(m);
        tree.appendChild(div);
    });
}

// メソッド詳細描画
function renderMethodDetail(method) {
    const detail = document.getElementById('method-detail');
    detail.innerHTML = `
    <h4>${method.className}.${method.methodName}</h4>
    <pre><strong>oldSource:</strong>\n${method.oldSource || ''}</pre>
    <pre><strong>newSource:</strong>\n${method.newSource || ''}</pre>
    <p>status: ${method.status}</p>
  `;
}

// ボタン押下時の処理
document.getElementById('run-diff').addEventListener('click', async () => {
    const oldSource = document.getElementById('old-source').value;
    const newSource = document.getElementById('new-source').value;
    try {
        const methods = await fetchDiff(oldSource, newSource);
        renderTree(methods);
        if (methods.length > 0) renderMethodDetail(methods[0]);
    } catch (err) {
        alert(err.message);
    }
});
