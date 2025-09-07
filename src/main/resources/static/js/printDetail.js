function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function loadDetail(id) {
    fetch(`/api/print-info/${id}/detail`)
        .then(res => {
            if (!res.ok) {
                // 서버에서 4xx, 5xx 응답을 받으면 에러를 발생시킵니다.
                throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            // API 응답 구조에 맞게 'info' 객체를 가져옵니다.
            // 만약 데이터가 최상위에 있다면 const info = data; 로 변경하세요.
            const info = data.info;

            if (!info) {
                alert('상세 정보를 불러오지 못했습니다.');
                return;
            }

            // 페이지 제목에 업체명을 추가합니다. (ID 오타 수정: companyConatact -> companyContact)
            const titleElement = document.getElementById('companyContact');
            if (titleElement && info.companyContact) { // API 응답에 companyContact 필드가 있다고 가정합니다.
                titleElement.textContent = `${info.companyContact}`;
            }

            // 각 필드의 값을 안전하게 설정하는 헬퍼 함수
            const setText = (elementId, value) => {
                const element = document.getElementById(elementId);
                if (element) {
                    // 값이 null이나 undefined일 경우 빈 문자열로 표시합니다.
                    // element.textContent = value ?? '';
                    element.innerHTML = value ?? '';
                }
            };

            // API 응답의 필드명(예: info.orderDate)과 HTML의 ID를 매핑합니다.
            // 아래 필드명은 예시이므로, 실제 API 응답에 맞게 수정해야 합니다.
            setText('companyContact', info.companyContact);
            setText('orderDate', info.orderDate);
            setText('deliveryDeadline', info.deliveryDeadline);
            setText('printTeam', info.printTeam);
            setText('previousOrderCount', info.previousOrderCount);
            setText('itemName', info.itemName);
            setText('size', info.size);
            setText('bagColor', info.bagColor);
            setText('quantity', info.quantity);
            setText('printSide', info.printSide);
            setText('printCount', info.printCount);
            setText('colorMixingData', info.colorMixingData);
            setText('logoColor', info.logoColor);
            setText('logoPosition', info.logoPosition);
            setText('logoSize', info.logoSize);
            setText('printMemo', info.printMemo);
            setText('colorData', info.colorData1 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + info.colorData2 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + info.colorData3);
            // setText('colorData1', info.colorData1);
            // setText('colorData2', info.colorData2);
            // setText('colorData3', info.colorData3);

            // 첨부파일 처리
            const attachmentList = document.getElementById("attachmentList");
            attachmentList.innerHTML = "";

            if (info.logoSamplePath) {
                const li = document.createElement("li");
                const img = document.createElement("img");

                // [오류 수정] 잘못된 정규식을 수정하여 파일 경로에서 파일명만 추출합니다.
                const fileName = info.logoSamplePath.split(/[/\\]/).pop();

                img.src = "/File/" + fileName;
                img.alt = "인쇄 샘플 이미지";
                img.style.maxWidth = "100%"; // 컨테이너 너비에 맞게 이미지 크기 조정
                img.style.height = "auto";
                li.appendChild(img);
                attachmentList.appendChild(li);
            }
        })
        .catch(err => {
            console.error("상세 조회 중 오류:", err);
            alert("상세 정보를 조회하는 중 오류가 발생했습니다.");
        });
}

window.onload = function () {
    const id = getQueryParam("printId");
    if (id) {
        loadDetail(id);
    }
}
