document.addEventListener('DOMContentLoaded', ()=> {
    document.getElementById('runBtn').addEventListener('click', runDiff);
});

function runDiff() {
    const oldSource = document.getElementById('oldSource').value;
    const newSource = document.getElementById('newSource').value;
    fetch('/api/analysis/diff', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({oldSource, newSource})
    }).then(r => r.json())
        .then(renderTree)
        .catch(e => alert('差分取得エラー: ' + e));
}

function renderTree(diffs) {
    const tree = document.getElementById('tree');
    tree.innerHTML = '';
    diffs.forEach((d, idx) => {
        const el = document.createElement('div');
        el.className = 'method-item ' + d.status;
        el.dataset.idx = idx;
        el.innerHTML = `<strong>${d.className}</strong> / ${d.methodName} <div style="font-size:0.9em;color:#666">${d.signature}</div>`;
        el.addEventListener('click', ()=> showDetails(d));
        tree.appendChild(el);
    });
    // 初回で最初のアイテムを選択
    if (diffs.length > 0) showDetails(diffs[0]);
}

function showDetails(d) {
    const details = document.getElementById('details');
    let html = `<div>状態: <strong>${d.status}</strong></div>`;
    html += `<h4>新ソース</h4>`;
    html += `<pre>${escapeHtml(d.newSource || '(なし)')}</pre>`;
    html += `<h4>旧ソース</h4>`;
    html += `<pre>${escapeHtml(d.oldSource || '(なし)')}</pre>`;
    details.innerHTML = html;
}

function escapeHtml(s) {
    if (!s) return '';
    return s.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
}
