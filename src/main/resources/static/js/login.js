function isEmpty(str) {
        return !str || str.trim() === '';
    }
    
    document.getElementById("year").textContent = new Date().getFullYear();

    document.getElementById("loginForm").addEventListener("submit", function(e) {
      e.preventDefault();
      const id = document.getElementById("id").value.trim();
      const pw = document.getElementById("password").value;

      if (!id || !pw) {
        alert("아이디와 비밀번호를 입력하세요.");
        return;
      }

      // 실제 ERP 백엔드 인증 API 호출 부분
      fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id: id,
          password: pw
        })
      })
      .then(res => res.json())
      .then(msg => {
        console.log(msg);
        debugger
        if (msg.status == 'success') {

           // alert(`환영합니다, ${msg.사용자명}님! (데모)`);
            window.location.href = "/";
        } else {
           alert(msg.message);
        }
      });

    });

    function forgotPassword() {
      alert("비밀번호 재설정은 IT 관리자에게 문의하세요.");
    }
