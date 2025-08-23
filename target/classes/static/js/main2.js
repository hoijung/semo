let table;
        let selectedRow = null; // 선택된 테이블 행 DOM 요소를 추적하기 위한 변수
        let currentSelectedId = null; // 현재 선택된 레코드의 고유 ID를 저장하는 변수

        $(document).ready(function () {
            table = $('#printTable').DataTable({
                ajax: { url: '/api/prints', dataSrc: '' },
                columns: [
                    { data: '인쇄ID' },
                    { data: '주문일자' } // DataTables는 '주문일자' 필드에서 직접 날짜 문자열을 가져옴
                ],
                paging: false,
                info: false,
                searching: false
            });

            // 테이블 행 클릭 이벤트
            $('#printTable tbody').on('click', 'tr', function () {
                const data = table.row(this).data();
                if (!data) return;

                // 기존 선택된 행의 클래스 제거
                if (selectedRow) {
                    $(selectedRow).removeClass('selected-row');
                }

                // 현재 클릭된 행에 클래스 추가 및 selectedRow 변수 업데이트
                $(this).addClass('selected-row');
                selectedRow = this;

                // 선택된 행의 인쇄ID 저장
                currentSelectedId = data.인쇄ID;

                fillDetailForm(data); // 상세 정보 폼 채우기
                updateButtonState(true); // 수정/삭제 버튼 활성화
            });

            // 버튼 이벤트 리스너
            $('#btnAdd').click(addRecord);
            $('#btnUpdate').click(updateRecord);
            $('#btnDelete').click(deleteRecord);

            // 초기 버튼 상태 설정 (수정/삭제 비활성화)
            updateButtonState(false);
        });

        function fillDetailForm(data) {
            // 폼 필드의 값을 먼저 초기화
            resetDetailFormValues();

            for (let key in data) {
                const el = $('#' + key);
                if (el.length) { // 해당 ID를 가진 요소가 존재하면
                    if (el.attr('type') === 'checkbox') {
                        el.prop('checked', data[key]);
                    } else if (el.attr('type') === 'date') {
                        // 서버에서 오는 날짜 문자열 (예: "YYYY-MM-DDTHH:MM:SS.sssZ" 또는 "YYYY-MM-DD")
                        //에서 'YYYY-MM-DD' 부분만 추출하여 date input에 설정
                        if (data[key]) {
                            el.val(data[key].substring(0, 10)); // YYYY-MM-DD
                        } else {
                            el.val(''); // 값이 없으면 빈 문자열
                        }
                    } else {
                        el.val(data[key]);
                    }
                }
            }
        }

        function getFormData() {
            const formData = {};
            // .detail-card 내부의 모든 input, textarea에서 데이터 추출
            $('.detail-card').find('input, textarea').each(function () {
                const id = $(this).attr('id');
                if (!id) return;

                if ($(this).attr('type') === 'checkbox') {
                    formData[id] = $(this).prop('checked');
                } else {
                    formData[id] = $(this).val();
                }
            });
            return formData;
        }

        // DataTables 재로드 및 선택된 행 복원 로직
        function reloadTableAndRestoreSelection() {
            table.ajax.reload(function () {
                if (currentSelectedId !== null) {
                    let foundRow = null;
                    // DataTables 내부 API를 사용하여 ID로 행 찾기
                    table.rows().every(function () {
                        const rowData = this.data();
                        if (rowData && rowData.인쇄ID === currentSelectedId) {
                            foundRow = this.node(); // DOM 노드 저장
                            fillDetailForm(rowData); // 최신 데이터로 폼 채우기
                            return false; // 루프 중단
                        }
                    });

                    // 이전에 선택된 행의 클래스 제거 (혹시 모를 경우를 대비)
                    $('#printTable tbody tr').removeClass('selected-row');

                    if (foundRow) {
                        $(foundRow).addClass('selected-row'); // 찾은 행에 클래스 추가
                        selectedRow = foundRow; // selectedRow 변수 업데이트
                        updateButtonState(true); // 버튼 활성화
                    } else {
                        // 선택했던 ID의 레코드가 더 이상 존재하지 않는 경우 (예: 삭제 후)
                        resetDetailForm();
                        updateButtonState(false);
                    }
                } else {
                    // currentSelectedId가 null인 경우 (추가 또는 삭제 후)
                    resetDetailForm(); // 폼 초기화
                    updateButtonState(false); // 버튼 비활성화
                }
            }, false); // `false`는 DataTables의 `resetPaging` 옵션으로, 페이지를 현재 상태로 유지
        }

        function addRecord() {
            const formData = getFormData();
            if (!confirm("등록 하시겠습니까?")) return;

            delete formData.인쇄ID; // 인쇄ID는 서버에서 자동 생성

            fetch('/api/prints', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
                    }
                    return response.json();
                })
                .then(data => {
                    alert("새 레코드가 등록되었습니다!");
                    currentSelectedId = null; // 등록 후에는 선택된 ID를 초기화
                    reloadTableAndRestoreSelection();
                })
                .catch(error => {
                    console.error("Add Error:", error);
                    alert("레코드 등록 실패: " + error.message);
                });
        }

        function updateRecord() {
            const id = $('#인쇄ID').val();
            if (!id) { alert("수정할 레코드를 선택하세요."); return; }
            if (!confirm("수정 하시겠습니까?")) return;

            fetch('/api/prints/' + id, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(getFormData())
            })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
                    }
                    return response.json();
                })
                .then(data => {
                    //alert("레코드가 성공적으로 수정되었습니다!");
                    // 수정 후에는 currentSelectedId를 유지한 채 재로드하여 포커스 복원
                    reloadTableAndRestoreSelection();
                })
                .catch(error => {
                    console.error("Update Error:", error);
                    alert("레코드 수정 실패: " + error.message);
                });
        }

        function deleteRecord() {
            const id = $('#인쇄ID').val();
            if (!id) { alert("삭제할 레코드를 선택하세요."); return; }
            if (!confirm("정말 삭제하시겠습니까?")) return;

            fetch('/api/prints/' + id, { method: 'DELETE' })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
                    }
                    //alert("레코드가 성공적으로 삭제되었습니다!");
                    currentSelectedId = null; // 삭제 후에는 선택된 ID를 초기화
                    reloadTableAndRestoreSelection();
                })
                .catch(error => {
                    console.error("Delete Error:", error);
                    alert("레코드 삭제 실패: " + error.message);
                });
        }

        // 상세 정보 폼의 값만 초기화하는 함수
        function resetDetailFormValues() {
            $('#detail-container').find('input[type="text"], input[type="number"], input[type="date"], textarea').val('');
            $('#detail-container').find('input[type="checkbox"]').prop('checked', false);
            $('#인쇄ID').val(''); // 숨겨진 인쇄ID 필드 초기화
        }

        // 상세 정보 폼 전체를 초기화 (선택 상태까지)
        function resetDetailForm() {
            resetDetailFormValues(); // 값 초기화

            // 이전에 선택된 행이 있었다면 클래스 제거
            if (selectedRow) {
                $(selectedRow).removeClass('selected-row');
                selectedRow = null;
            }
            currentSelectedId = null; // 저장된 ID도 초기화
        }

        // 버튼 활성화/비활성화 함수
        function updateButtonState(enable) {
            // 등록 버튼은 항상 활성화
            $('#btnAdd').prop('disabled', false).addClass('enabled');

            // 수정, 삭제 버튼은 enable 값에 따라 활성화/비활성화
            $('#btnUpdate, #btnDelete').prop('disabled', !enable)
                .toggleClass('enabled', enable);
        }
