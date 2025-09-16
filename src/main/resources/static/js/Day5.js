// ツリーノードクリック時
document.querySelectorAll('.tree-node').forEach(node => {
    node.addEventListener('click', () => {
        const nodeId = node.dataset.id;

        // APIからノード詳細を取得
        fetch(`/api/node/${nodeId}`)
            .then(res => res.json())
            .then(data => {
                const rightPane = document.getElementById('node-source');
                rightPane.textContent = data.source || "ソースなし";

                // 差分があれば背景色変更
                const container = document.getElementById('right-pane');
                if(data.diff && data.diff !== "") {
                    container.classList.add('has-diff');
                } else {
                    container.classList.remove('has-diff');
                }
            })
            .catch(err => {
                document.getElementById('node-source').textContent = "取得エラー";
                console.error(err);
            });
    });
});
