const API_BASE = "/api/analysis";

function createNodeLabel(method) {
    const span = document.createElement("span");
    span.textContent = `${method.className}.${method.methodName}`;
    if (method.status === "added") span.classList.add("method-added");
    if (method.status === "removed") span.classList.add("method-removed");
    span.addEventListener("click", () => showDetail(method));
    return span;
}

function renderTree(diffList) {
    const container = document.getElementById("file-tree");
    container.innerHTML = "";
    diffList.forEach(method => {
        const node = createNodeLabel(method);
        container.appendChild(node);
        container.appendChild(document.createElement("br"));
    });
}

function showDetail(method) {
    document.getElementById("method-detail").textContent = `${method.className}.${method.methodName}`;
    document.getElementById("old-source").textContent = method.oldSource || "";
    document.getElementById("new-source").textContent = method.newSource || "";
}

async function fetchAndRenderDiff(oldSource, newSource) {
    const resp = await fetch(`${API_BASE}/diff`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({oldSource, newSource})
    });
    if (!resp.ok) {
        alert("差分取得に失敗しました");
        return;
    }
    const diffList = await resp.json();
    renderTree(diffList);
}

document.getElementById("run-diff").addEventListener("click", () => {
    const oldSrc = document.getElementById("old-source").textContent;
    const newSrc = document.getElementById("new-source").textContent;
    fetchAndRenderDiff(oldSrc, newSrc);
});
