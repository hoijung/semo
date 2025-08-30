$(document).ready(function() {
    // Fetch user authority and set up UI
    fetch('/api/auth/user')
        .then(response => {
            if (!response.ok) {
                throw new Error('Not authenticated');
            }
            return response.json();
        })
        .then(user => {
            document.getElementById('greeting').textContent = `${user.userName} 님`;
            const authority = user.authority;
            if (authority !== '관리자' && authority !== '인쇄팀') {
                $('#btnPrintEnd').hide();
                $('#btnPrintEndCnl').hide();
            }
            if (authority === '모든 데이터 조회') {
                $('#btnDetail').hide();
            }
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            // Redirect to login page if not authenticated
            window.location.href = '/login.html';
        });
    loadMenu('printList.html');

    const inputStart = document.getElementById("orderDateStart");
    const inputEnd = document.getElementById("orderDateEnd");

    // 오늘 날짜 구하기
    const today = new Date();

    // yyyy-MM-dd 형식으로 변환
    const formattedToday = today.toISOString().split("T")[0];
    // input 기본값 설정
    inputEnd.value = formattedToday;

    const startDay = new Date();
    // 14일(=2주) 전 날짜 구하기
    startDay.setDate(startDay.getDate() - 31);
    // yyyy-MM-dd 형식으로 변환
    const formattedStart = startDay.toISOString().split("T")[0];
    // input 기본값 설정
    inputStart.value = formattedStart;

    // 공통 컬럼 렌더러
    const renderCheckbox = (data, type) => type === 'display' ? `<input type="checkbox" ${eval(data) ? 'checked' : ''} disabled>` : data;

    const table = $('#grid').DataTable({
        responsive: true,
        dom: 'frtip', // 'B'를 제거하여 기본 버튼 컨테이너를 숨김
        ajax: {
            url: '/api/prints/printList1',
            dataSrc: 'data'
        },
        buttons: [
            // { extend: "copy", className: "btn-sm" },
            // { extend: "csv", className: "btn-sm" },
            {
                extend: "excel",
                className: "btn-sm",
                title: "인쇄 작업 목록", // 엑셀 파일의 제목
                filename: `인쇄작업목록_${formattedToday}` // 오늘 날짜를 포함한 파일명
            }
        ],
        scrollY: getTableHeight("370"), // 동적으로 높이 지정
        scrollX: true,   // ✅ 좌우 스크롤 허용
        columns: [
            {
                title: '',  // 체크박스 컬럼
                orderable: false,
                className: 'dt-body-center',
                render: function(data, type, row, meta) {
                    return `<input type="checkbox" class="row-select">`;
                }
            },
            { data: 'orderDate', title: '주문일자', className: 'dt-center' },
            { data: 'printTeam' },
            { data: 'companyContact' },
            { data: "importantYn", title: "중요", render: renderCheckbox },
            { data: 'printMemo' },
            { data: 'itemName', title: '품목명' },
            { data: 'bagColor', title: '컬러' },
            { data: 'size', title: '사이즈' },
            { data: 'quantity', title: '장수' },
            { data: 'quantity', title: '기존주문횟수' },
            { data: 'printSide', title: '인쇄면' },
            { data: 'printCount', title: '인쇄도수', className: 'dt-center' },
            { data: 'colorData1', title: '조색데이터1' },
            { data: 'colorData2', title: '조색데이터2' },
            { data: 'colorData3', title: '조색데이터3' },

            { data: 'logoColor', title: '로고인쇄색상' },
            { data: 'logoSize', title: '로고인쇄크기' },
            { data: 'logoPosition', title: '로고위치' },
            { data: 'quantity', title: '박스수량' },
            { data: 'printMethod', title: '발송최종기한', className: 'dt-center' },
            { data: "pickingYn", title: "피킹완료", className: 'dt-center', render: renderCheckbox },
            { data: "printEndYn", title: "인쇄완료", className: 'dt-center', render: renderCheckbox },

        ],

        // 아래 'createdRow' 옵션을 추가합니다.
        createdRow: function(row, data, dataIndex) {
            // 'data' 객체에서 'printMemo' (인쇄참고사항) 필드를 확인합니다.
            // 필드명이 다를 경우 실제 사용하는 필드명으로 변경해주세요. (예: data.memo)
            if (eval(data.importantYn)) {
                // printMemo에 내용이 있으면 'highlight-row' 클래스를 추가합니다.
                $(row).addClass('highlight-row');
            }
        },

        searching: false, // 기본 검색 기능 비활성화
        lengthChange: false, // 표시 건수 변경 기능 비활성화
        pageLength: 15, // 기본 페이지당 행 수
        language: {
            emptyTable: "데이터가 없습니다.",
            info: "총 _TOTAL_개",
            infoEmpty: "",
            infoFiltered: "(_MAX_개 중에서 필터링됨)",
            paginate: {
                first: "<<",
                last: ">>",
                next: ">",
                previous: "<"
            }
        }
    });

    // 조회 버튼 클릭 이벤트
    $('#btnSearch').on('click', function(e) {
        e.preventDefault();   // form submit 방지
        e.stopPropagation();  // 이벤트 전파 방지 (선택사항)

        // form 직렬화 (검색조건을 한 번에 쿼리스트링으로)
        const query = $('#searchForm').serialize();
        //debugger

        // 새로운 url로 다시 로드
        table.ajax.url('/api/prints/search?' + query).load();
    });

    $('#printTeam').on('change', function(e) {
        e.preventDefault();   // form submit 방지
        e.stopPropagation();  // 이벤트 전파 방지 (선택사항)

        // form 직렬화 (검색조건을 한 번에 쿼리스트링으로)
        const query = $('#searchForm').serialize();
        //debugger

        // 새로운 url로 다시 로드
        table.ajax.url('/api/prints/search?' + query).load();
    });

    // 엑셀 다운로드 버튼 클릭 이벤트
    $('#btnExcel').on('click', function() {
        // DataTables의 excel 버튼 기능을 프로그래밍 방식으로 실행
        table.buttons('.buttons-excel').trigger();
    });

    // 클릭 이벤트
    $('#grid tbody').on('click', 'tr', function() {
        const data = table.row(this).data();
        if (data) {
            // window.open(`assetDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=700');
        }
    });

    // 체크박스 클릭 시 해당 행 선택/해제
    $('#grid tbody').on('click', 'input.row-select', function(e) {
        const $table = $('#grid');
        const $row = $(this).closest('tr');

        // 다른 행 체크박스 모두 해제
        $table.find('tbody tr.selected').removeClass('selected');
        $table.find('input.row-select').prop('checked', false);

        // 클릭한 행 선택
        $row.addClass('selected');
        $(this).prop('checked', true);

        e.stopPropagation(); // tr 클릭 이벤트 방지
    });

    // 선택된 행 가져오기
    function getSelectedRows() {
        return $('#grid tbody tr.selected').map(function() {
            return $('#grid').DataTable().row(this).data();
        }).get();
    }

    // 상세보기 버튼
    $('#btnDetail').click(function() {
        const selected = getSelectedRows();
        if (selected.length === 0) {
            alert("행을 선택해주세요.");
            return;
        }
        // 여러 행 선택 가능, 첫 번째 행 상세 보기
        const data = selected[0];
        window.open(`printDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1500,height=900');
    });

    // 공통 액션 처리 함수 (인쇄완료, 취소 등)
    function handleBatchAction(actionName, urlPath) {
        const selected = getSelectedRows();
        if (selected.length === 0) {
            alert("행을 선택해주세요.");
            return;
        }

        if (!confirm(`선택한 ${selected.length}개의 항목을 ${actionName} 처리하시겠습니까?`)) {
            return;
        }

        const requests = selected.map(row =>
            $.post(`/api/print-info/${row.printId}/${urlPath}`)
        );

        Promise.all(requests)
            .then(responses => {
                alert(`${responses.length}개의 항목이 성공적으로 ${actionName} 처리되었습니다.`);
                table.ajax.reload(null, false); // 페이징 유지하고 새로고침
            })
            .catch(error => {
                console.error(`${actionName} 처리 중 오류 발생:`, error);
                let errorMessage = "처리 중 오류가 발생했습니다. 콘솔을 확인해주세요.";
                // Spring Boot에서 보내는 기본 오류 메시지 형식(JSON)을 파싱합니다.
                if (error && error.responseJSON && error.responseJSON.message) {
                    errorMessage = error.responseJSON.message;
                } else if (error && error.responseText) {
                    errorMessage = error.responseText;
                }
                alert(errorMessage);
                table.ajax.reload(null, false); // 오류 발생 후에도 최신 상태를 반영하기 위해 새로고침
            });
    }

    // 인쇄완료 버튼
    $('#btnPrintEnd').click(() => handleBatchAction('인쇄완료', 'printEnd'));

    // 인쇄완료 취소 버튼
    $('#btnPrintEndCnl').click(() => handleBatchAction('인쇄완료 취소', 'cancel-printEnd'));

});