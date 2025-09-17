// main.js 内の差分取得・ツリー描画部分
async function fetchAndRenderDiff(oldSource, newSource) {
    const apiUrl = 'https://codeleader-production.up.railway.app/api/analysis/diff'; // 本番 URL

    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                oldSource: oldSource,
                newSource: newSource
            })
        });

        if (!response.ok) {
            throw new Error(`API エラー: ${response.status}`);
        }

        const diffData = await response.json();

        renderFileTree(diffData);
        renderMethodDetail(diffData);

    } catch (err) {
        console.error('差分取得に失敗しました:', err);
        document.getElementById('file-tree').textContent = '差分取得に失敗しました';
    }
}

// ツリー描画サンプル
function renderFileTree(diffData) {
    const treeDiv = document.getElementById('file-tree');
    treeDiv.innerHTML = '';
    diffData.forEach(method => {
        const node = document.createElement('div');
        node.textContent = `${method.className} : ${method.methodName} (${method.status})`;
        node.onclick = () => showMethodDetail(method);
        treeDiv.appendChild(node);
    });
}

// メソッド詳細表示サンプル
function showMethodDetail(method) {
    const detailDiv = document.getElementById('method-detail');
    detailDiv.innerHTML = `
        <strong>${method.className} : ${method.methodName}</strong><br/>
        <pre>旧ソース:\n${method.oldSource}</pre>
        <pre>新ソース:\n${method.newSource}</pre>
    `;
}

// ボタン押下でサンプルコードを解析
document.getElementById('run-diff').addEventListener('click', () => {
    const oldCode = `public class Hello { void greet() {} }`;
    const newCode = `public class Hello { void greet(String name) {} }`;
    fetchAndRenderDiff(oldCode, newCode);
});
