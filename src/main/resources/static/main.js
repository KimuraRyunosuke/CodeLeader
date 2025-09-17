document.addEventListener('DOMContentLoaded', () => {
    const fileTree = document.getElementById('file-tree');
    const methodDetail = document.getElementById('method-detail');
    const runDiffBtn = document.getElementById('run-diff');
    const oldSourceInput = document.getElementById('old-source');
    const newSourceInput = document.getElementById('new-source');

    async function fetchDiff(oldSource, newSource) {
        const res = await fetch('https://codeleader-production.up.railway.app/api/analysis/diff', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ oldSource, newSource })
        });
        return res.json();
    }

    function renderTree(methods) {
        fileTree.innerHTML = '';
        methods.forEach((method) => {
            const li = document.createElement('li');
            li.textContent = `${method.className}.${method.methodName} (${method.status})`;
            li.style.cursor = 'pointer';
            li.addEventListener('click', () => renderMethodDetail(method));
            fileTree.appendChild(li);
        });
    }

    function renderMethodDetail(method) {
        methodDetail.innerHTML = `
      <h4>${method.className}.${method.methodName}</h4>
      <pre><strong>oldSource:</strong>\n${method.oldSource || ''}</pre>
      <pre><strong>newSource:</strong>\n${method.newSource || ''}</pre>
      <p>status: ${method.status}</p>
    `;
    }

    runDiffBtn.addEventListener('click', async () => {
        const oldSrc = oldSourceInput.value;
        const newSrc = newSourceInput.value;
        if (!oldSrc || !newSrc) return alert('旧ソース・新ソースを入力してください');

        try {
            const methods = await fetchDiff(oldSrc, newSrc);
            renderTree(methods);
            if (methods.length) renderMethodDetail(methods[0]);
        } catch (err) {
            console.error(err);
            alert('差分取得に失敗しました');
        }
    });
});
