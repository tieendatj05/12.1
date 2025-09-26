const $ = (sel) => document.querySelector(sel);
const result = $("#result");
const status = $("#status");
const execBtn = $("#execBtn");
const textarea = $("#sql");

async function execSQL() {
  const sql = textarea.value;
  status.textContent = "Running...";
  execBtn.disabled = true;
  result.innerHTML = "";

  try {
    const res = await fetch("api/query", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ sql })
    });
    const data = await res.json();
    if (!data.ok) {
      renderError(data.error || "Unknown error");
    } else if (data.type === "rows") {
      renderTable(data.rows);
    } else {
      renderMessage(`OK. Changes: ${data.changes ?? 0}` + (data.lastID ? `, lastID: ${data.lastID}` : ""));
    }
  } catch (e) {
    renderError(e.message || String(e));
  } finally {
    execBtn.disabled = false;
    status.textContent = "";
  }
}

function renderTable(rows) {
  if (!rows || rows.length === 0) {
    result.textContent = "No rows.";
    return;
  }
  const cols = Object.keys(rows[0]);
  const table = document.createElement("table");
  table.className = "table";
  const thead = document.createElement("thead");
  const trh = document.createElement("tr");
  cols.forEach(c => {
    const th = document.createElement("th");
    th.textContent = c;
    trh.appendChild(th);
  });
  thead.appendChild(trh);
  table.appendChild(thead);

  const tbody = document.createElement("tbody");
  rows.forEach(r => {
    const tr = document.createElement("tr");
    cols.forEach(c => {
      const td = document.createElement("td");
      td.textContent = r[c];
      tr.appendChild(td);
    });
    tbody.appendChild(tr);
  });
  table.appendChild(tbody);
  result.innerHTML = "";
  result.appendChild(table);
}

function renderError(msg) {
  result.innerHTML = `<div class="error">Error executing the SQL statement:\n${msg}</div>`;
}

function renderMessage(msg) {
  const pre = document.createElement("pre");
  pre.className = "code";
  pre.textContent = msg;
  result.innerHTML = "";
  result.appendChild(pre);
}

execBtn.addEventListener("click", execSQL);
textarea.value = "SELECT * FROM patients LIMIT 5;";
