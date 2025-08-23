function getQueryParam(name) {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(name);
        }

        function loadDetail(id) {
            fetch(`/api/print-info/${id}/detail`)
                .then(res => res.json())
                .then(data => {
                    const info = data.info;
                    const tbody = document.querySelector("#detailTable tbody");
                    tbody.innerHTML = "";
                    const fields = [
                        {key: "printId", label: "인쇄ID"},
                        {key: "printMethod", label: "인쇄방법"},
                        {key: "itemName", label: "품목명"},
                        {key: "bagColor", label: "쇼핑백색상"},
                        {key: "sizeText", label: "사이즈"},
                        {key: "quantity", label: "제작장수"},
                        {key: "team", label: "인쇄담당팀"},
                        {key: "sides", label: "인쇄면"}
                    ];
                    fields.forEach(f => {
                        const tr = document.createElement("tr");
                        const th = document.createElement("th");
                        th.textContent = f.label;
                        const td = document.createElement("td");
                        td.textContent = info[f.key] ?? "";
                        tr.appendChild(th);
                        tr.appendChild(td);
                        tbody.appendChild(tr);
                    });

                    // 첨부파일
                    const ul = document.getElementById("attachmentList");
                    ul.innerHTML = "";
                    //debugger

                    const li = document.createElement("li");

                    // 이미지인 경우 <img>로 표시
                    const img = document.createElement("img");

                    const fileName = info.printSample.split(///|\\/).pop();

                    img.src = "/File/" + fileName;
                    //img.alt = file.fileName;
                    img.style.maxWidth = "800px";
                    li.appendChild(img);
                    ul.appendChild(li);
                    //    }
                })
            //  .catch(err => alert("상세 조회 중 오류"));
        }

        window.onload = function () {
            const id = getQueryParam("printId");
            if (id) loadDetail(id);
        }
