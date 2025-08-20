<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>자산관리 조회</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/handsontable@12.1.0/dist/handsontable.min.css">
<script src="https://cdn.jsdelivr.net/npm/handsontable@12.1.0/dist/handsontable.min.js"></script>
<style>
#hot { width: 100%; height: 500px; }
</style>
</head>
<body>
<h2>자산관리 조회</h2>
<div id="hot"></div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    const container = document.getElementById("hot");

    fetch("/api/print-info")
        .then(res => res.json())
        .then(data => {
            const columns = Object.keys(data[0] || {}).map(key => ({data: key, title: key}));
            new Handsontable(container, {
                data: data,
                colHeaders: columns.map(c => c.title),
                columns: columns,
                rowHeaders: true,
                width: '100%',
                height: 500,
                licenseKey: 'non-commercial-and-evaluation',
                manualColumnResize: true,
                manualRowResize: true,
            });
        })
        .catch(err => console.error(err));
});
</script>
</body>
</html>
