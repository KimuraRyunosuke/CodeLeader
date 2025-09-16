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
        node.source.split("\n").forEach((line, i) => {
            let lineClass = "";
            if (node.diffStatus === "added") lineClass = "added";
            if (node.diffStatus === "removed") lineClass = "removed";
            if (node.diffStatus === "modified") lineClass = "modified";

            sourceHtml += `<div class="${lineClass}">${i + 1}: ${line}</div>`;
        });
    } else {
        sourceHtml = "<div>(ソース未登録)</div>";
    }

    document.getElementById("node-source").innerHTML = sourceHtml;
}
