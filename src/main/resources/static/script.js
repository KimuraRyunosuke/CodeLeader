// ダミーデータ（本来はサーバAPIから取得）
const treeData = [
    {
        id: "1",
        text: "Hello.java",
        children: [
            {
                id: "1-1",
                text: "greet()",
                diffStatus: "modified",
                source: "void greet(String name) {\n    System.out.println(\"Hello, \" + name);\n}"
            }
        ]
    }
];

$(function () {
    // ツリー初期化
    $('#tree').jstree({
        core: { data: treeData }
    });

    // ノード選択イベント
    $('#tree').on("select_node.jstree", function (e, data) {
        const node = data.node.original;
        updateRightPane(node);
    });
});

// 右ペイン更新処理
function updateRightPane(node) {
    document.getElementById("node-title").textContent = node.text;
    let sourceHtml = "";

    if (node.source) {
        // 直接ソースを持つノード
        sourceHtml = renderSource(node.source, node.diffStatus);
    } else if (node.children && node.children.length > 0) {
        // 子ノードがある場合 → 子のソースをまとめる
        node.children.forEach(childId => {
            const child = $('#tree').jstree(true).get_node(childId).original;
            if (child.source) {
                sourceHtml += `<div class="child-title">▼ ${child.text}</div>`;
                sourceHtml += renderSource(child.source, child.diffStatus);
            }
        });
    } else {
        sourceHtml = "<div>(ソース未登録)</div>";
    }

    document.getElementById("node-source").innerHTML = sourceHtml;
}

// ソース描画
function renderSource(source, diffStatus) {
    let html = "";
    source.split("\n").forEach((line, i) => {
        let lineClass = "";
        if (diffStatus === "added") lineClass = "added";
        if (diffStatus === "removed") lineClass = "removed";
        if (diffStatus === "modified") lineClass = "modified";

        html += `<div class="${lineClass}">${i + 1}: ${line}</div>`;
    });
    return html;
}
