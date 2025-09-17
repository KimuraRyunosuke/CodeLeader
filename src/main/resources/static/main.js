// main.js（既存のUIスクリプトに統合済み）

/**
 * 指定された旧ソース・新ソースをサーバに送り、差分を取得して描画する
 * @param {string} oldSource
 * @param {string} newSource
 */
async function fetchAndRenderDiff(oldSource, newSource) {
    try {
        const response = await fetch('/api/analysis/diff', {
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
        console.error('Error fetching diff:', err);
        document.getElementById('file-tree').innerText = '差分取得に失敗しました';
    }
}

/**
 * 取得した差分データをツリー表示＆詳細表示に描画
 * @param {Array} diffData
 */
function renderTree(diffData) {
    const treeContainer = document.getElementById('file-tree');
    const detailContainer = document.getElementById('method-detail');

    treeContainer.innerHTML = '';
    detailContainer.innerHTML = '';

    // ファイルごとにまとめる
    const filesMap = {};
    diffData.forEach(method => {
        if (!filesMap[method.className]) filesMap[method.className] = [];
        filesMap[method.className].push(method);
    });

    Object.keys(filesMap).forEach(fileName => {
        const fileDiv = document.createElement('div');
        fileDiv.className = 'file-node';
        fileDiv.innerText = fileName;
        treeContainer.appendChild(fileDiv);

        const methods = filesMap[fileName];
        const methodList = document.createElement('ul');
        methodList.style.display = 'none';
        methods.forEach(method => {
            const li = document.createElement('li');
            li.innerText = `${method.signature} (${method.status})`;
            li.style.cursor = 'pointer';
            li.addEventListener('click', () => {
                detailContainer.innerHTML = `
                    <h3>${method.className}#${method.methodName}</h3>
                    <p>Signature: ${method.signature}</p>
                    <p>Status: ${method.status}</p>
                    <pre>Old Source:\n${method.oldSource || ''}</pre>
                    <pre>New Source:\n${method.newSource || ''}</pre>
                `;
            });
            methodList.appendChild(li);
        });

        fileDiv.appendChild(methodList);
        fileDiv.addEventListener('click', () => {
            methodList.style.display = methodList.style.display === 'none' ? 'block' : 'none';
        });
    });
}

// 初期表示サンプル（必要に応じて呼び出し元で差し替え）
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderDiff(
        'public class Hello { void greet() {} }',
        'public class Hello { void greet(String name) {} }'
    );
});
