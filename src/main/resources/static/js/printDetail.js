function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function fetchDetail(id) {
    // [수정] 일반적인 REST API 엔드포인트 형식으로 변경합니다. 서버의 실제 경로와 일치해야 합니다.
    return fetch(`/api/print-info/${id}/detail`) // [수정] fetch가 반환하는 Promise를 리턴해야 합니다.
        .then(res => {
            if (!res.ok) {
                // 서버에서 4xx, 5xx 응답을 받으면 에러를 발생시킵니다.
                throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.json();
        })
        .catch(err => {
            console.error("상세 조회 중 오류:", err);
            // 오류가 발생한 ID에 대한 정보를 반환하여 UI에 표시할 수 있도록 합니다.
            return { error: true, message: `ID ${id}의 정보를 불러오는 데 실패했습니다.` };
        });
}

function loadMultipleDetails(ids) {
    const container = document.getElementById('details-container');
    const templateNode = document.getElementById('detail-card-template');

    if (!container || !templateNode) {
        console.error("필수 HTML 요소(details-container 또는 detail-card-template)를 찾을 수 없습니다.");
        return;
    }

    // 각 ID에 대해 fetch 요청을 생성합니다.
    const promises = ids.map(id => fetchDetail(id.trim()));

    // 모든 요청이 완료될 때까지 기다립니다.
    Promise.all(promises).then(results => {
        results.forEach(data => {
            const card = templateNode.content.cloneNode(true);
            const cardElement = card.querySelector('.detail-card');
            
            if (!data || data.error) { // [수정] data가 undefined이거나 error 객체일 경우 처리
                cardElement.innerHTML = `<p style="color: red;">${data ? data.message : '데이터를 불러오는 중 알 수 없는 오류가 발생했습니다.'}</p>`;
                container.appendChild(cardElement);
                return;
            }

            // [수정] 서버 응답이 { "info": {...} } 형태일 수도 있고, 데이터가 바로 최상위에 있을 수도 있습니다.
            // 실제 데이터 객체를 'info' 변수에 할당합니다.
            const info = data.info || data;

            // 데이터가 비어있는 경우를 확인합니다.
            if (!info || Object.keys(info).length === 0) {
                cardElement.innerHTML = `<p style="color: orange;">ID에 해당하는 데이터를 찾을 수 없습니다.</p>`;
                container.appendChild(cardElement);
                return;
            }

            // 템플릿 내의 요소를 찾아 데이터를 채웁니다.
            const setText = (field, value) => {
                const element = cardElement.querySelector(`[data-field="${field}"]`);
                if (element) {
                    // null이나 undefined일 경우 빈 문자열로 안전하게 처리합니다.
                    element.innerHTML = value ?? ''; 
                }
            };

            // [수정] for...in 루프 대신, 각 필드를 명시적으로 매핑하여 안정성을 높입니다.
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
            setText('logoColor', info.logoColor);
            setText('logoPosition', info.logoPosition);
            setText('logoSize', info.logoSize);
            setText('printMemo', info.printMemo);

            // 특수 처리 필드
            if (info.printMemo) {
                const memoElement = cardElement.querySelector('[data-field="printMemo"]');
                if (memoElement) {
                    memoElement.style.color = 'red';
                    memoElement.style.fontWeight = 'bold';
                }
            }

            const colorData = [info.colorData1, info.colorData2, info.colorData3].filter(Boolean).join('&nbsp;/&nbsp;');
            setText('colorData', colorData);

            const attachmentList = cardElement.querySelector('[data-field="attachmentList"]');
            if (info.logoSamplePath) {
                const li = document.createElement("li");
                const img = document.createElement("img");
                img.src = "/File/" + info.logoSamplePath;
                img.alt = "인쇄팀 사진";
                img.style.cssText = "max-width: 100%; height: auto; cursor: pointer;";
                img.onclick = () => window.open(img.src);
                li.appendChild(img);
                attachmentList.appendChild(li);
            } else {
                attachmentList.innerHTML = "<li>첨부파일이 없습니다.</li>";
            }

            container.appendChild(card);
        });
    });
}

// 단일 항목을 표시하기 위한 레거시 함수 (필요 시 사용)
function loadDetail(id) {
    // 이 함수는 이제 여러 항목 표시를 위해 직접 사용되지 않습니다.
    // 단일 항목만 표시해야 하는 특별한 경우가 있다면 이 함수를 호출할 수 있습니다.
    loadMultipleDetails([id]);
}

window.onload = function () {
    const printIdsParam = getQueryParam("printId");
    if (printIdsParam) {
        // 쉼표로 구분된 ID 문자열을 배열로 분리합니다.
        const ids = printIdsParam.split(',');
        loadMultipleDetails(ids);
    }
}
