const API_BASE = "/api/analysis";

async function fetchDiff(oldSource, newSource) {
    const res = await fetch(`${API_BASE}/diff`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ oldSource, newSource })
    });
    return res.json();
}

function renderTree(diffData) {
    const tree = document.getElementById("file-tree");
    tree.innerHTML = "";
    diffData.forEach(d => {
        const item = document.createElement("div");
        item.textContent = `${d.className}.${d.methodName} (${d.status})`;
        item.style.color = d.status === "added" ? "green" : d.status === "removed" ? "red" : "black";
        item.onclick = () => renderDetail(d);
        tree.appendChild(item);
    });
}

function renderDetail(node) {
    document.getElementById("old-source").textContent = node.oldSource || "// empty";
    document.getElementById("new-source").textContent = node.newSource || "// empty";
}

document.getElementById("run-diff").onclick = async () => {
    const oldSource = document.getElementById("old-source").textContent;
    const newSource = document.getElementById("new-source").textContent;
    const diffData = await fetchDiff(oldSource, newSource);
    renderTree(diffData);
};
