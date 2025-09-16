// ツリー初期化 (サンプル JSON)
// 実際は /api/tree などから取得する想定
$('#tree').jstree({
    'core': {
        'data': [
            { "id": "Hello.java", "parent": "#", "text": "Hello.java" },
            { "id": "Utils.java", "parent": "#", "text": "Utils.java" }
        ]
    }
});

// ノード選択イベント
$('#tree').on("select_node.jstree", function (e, data) {
    const nodeId = data.node.id;

    // バックエンド API 呼び出し
    fetch(`/api/node/${nodeId}`)
        .then(res => res.json())
        .then(detail => {
            // 右ペイン更新
            document.getElementById("node-title").innerText = detail.name;
            document.getElementById("node-source").innerText = detail.source;

            // 差分がある場合は背景色変更
            if (detail.diff && detail.diff.trim() !== "") {
                document.getElementById("node-source").style.backgroundColor = "#ffdddd";
            } else {
                document.getElementById("node-source").style.backgroundColor = "white";
            }
        })
        .catch(err => {
            console.error("API error:", err);
            document.getElementById("node-title").innerText = nodeId;
            document.getElementById("node-source").innerText = "取得に失敗しました";
        });
});
