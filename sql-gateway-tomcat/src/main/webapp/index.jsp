<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>The SQL Gateway</title>
  <link rel="stylesheet" href="style.css"/>
</head>
<body>
<div class="card">
  <h1>The SQL Gateway</h1>
  <p class="sub">Enter an SQL statement and click the Execute button.</p>
  <label for="sql">SQL statement:</label>
  <textarea id="sql" placeholder="e.g. SELECT * FROM patients LIMIT 5;"></textarea>
  <div class="actions">
    <button id="execBtn">Execute</button>
    <span id="status" aria-live="polite"></span>
  </div>
  <h3>SQL result:</h3>
  <div id="result" class="result"></div>
</div>
<script src="app.js"></script>
</body>
</html>
