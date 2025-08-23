// 현재 URL: 예를 들어, http://example.com/page.html?name=Alice&age=30
    const queryString = window.location.search; // "?name=Alice&age=30"

    if (queryString) {
        const params = {};
        // ?를 제거하고 &로 분리
        queryString.substring(1).split('&').forEach(param => {
            const parts = param.split('=');
            if (parts.length === 2) {
                // 디코딩 (예: 한글 또는 특수문자)
                params[decodeURIComponent(parts[0])] = decodeURIComponent(parts[1]);
            }
        });

        alert(params);
        // 출력: { name: "Alice", age: "30" }
        alert(params.image); // "Alice"
        const displayElement = document.getElementById('img_file');
            
            alert("http://localhost:8080/File/" + params.image)
            
            displayElement.src = "http://localhost:8080/File/" + params.image
    }
    //debugger
   // const image = urlParams.get('image');