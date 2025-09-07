$(document).ready(function () {
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
        dom: 'rtip', // 'f'를 제거하여 기본 검색창을 숨김
        ajax: {
            url: '/api/prints/printList1?' + $('#searchForm').serialize(),
            dataSrc: 'data'
        },
        buttons: [
            {
                extend: "excel",
                className: "btn-sm",
                title: "인쇄 작업 목록", // 엑셀 파일의 제목
                filename: `인쇄작업목록_${formattedToday}` // 오늘 날짜를 포함한 파일명
            }
        ],
        scrollY: getTableHeight("320"), // 동적으로 높이 지정
        scrollX: true,   // ✅ 좌우 스크롤 허용
        columns: [
            {
                title: '',  // 체크박스 컬럼
                orderable: false,
                className: 'dt-body-center',
                render: function (data, type, row, meta) {
                    return `<input type="checkbox" class="row-select">`;
                }
            },
            { data: 'orderDate', title: '주문일자', className: 'dt-center' },
            { data: 'weekDay' },
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

        createdRow: function (row, data, dataIndex) {
            if (eval(data.importantYn)) {
                $(row).addClass('highlight-row');
            }
        },

        searching: true, // 커스텀 검색을 위해 기능은 활성화
		lengthChange: false, // 표시 건수 변경 기능 비활성화
		paging: false, // 페이징 기능 비활성화
		info: false, // '총 n개'와 같은 정보 표시 비활성화
		columnDefs: [
			{ targets: "_all", className: "dt-center" } // 전체 컬럼 가운데 정렬
		],
		language: {
			emptyTable: "데이터가 없습니다."
		}
    });

    // 커스텀 검색창 이벤트 리스너
    $('#customSearch').on('keyup', function () {
        table.search(this.value).draw();
    });

    // 조회 버튼 클릭 이벤트
    $('#btnSearch').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        const query = $('#searchForm').serialize();
        table.ajax.url('/api/prints/search?' + query).load();
    });

    $('#printTeam').on('change', function (e) {
        e.preventDefault();
        e.stopPropagation();
        const query = $('#searchForm').serialize();
        table.ajax.url('/api/prints/search?' + query).load();
    });

    // 엑셀 다운로드 버튼 클릭 이벤트
    $('#btnExcel').on('click', function () {
        table.buttons('.buttons-excel').trigger();
    });

    // 클릭 이벤트
    $('#grid tbody').on('click', 'tr', function () {
        const data = table.row(this).data();
        if (data) {
            // window.open(`assetDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=700');
        }
    });

    // 체크박스 클릭 시 해당 행 선택/해제
    $('#grid tbody').on('click', 'input.row-select', function (e) {
        const $table = $('#grid');
        const $row = $(this).closest('tr');
        $table.find('tbody tr.selected').removeClass('selected');
        $table.find('input.row-select').prop('checked', false);
        $row.addClass('selected');
        $(this).prop('checked', true);
        e.stopPropagation();
    });

    // 선택된 행 가져오기
    function getSelectedRows() {
        return $('#grid tbody tr.selected').map(function () {
            return $('#grid').DataTable().row(this).data();
        }).get();
    }

    // 상세보기 버튼
    $('#btnDetail').click(function () {
        const selected = getSelectedRows();
        if (selected.length === 0) {
            alert("행을 선택해주세요.");
            return;
        }
        const data = selected[0];
        window.open(`printDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=900');
    });

    // 공통 액션 처리 함수 (인쇄완료, 취소 등)
    function handleBatchAction(actionName, urlPath) {
        const selected = getSelectedRows();
        if (selected.length === 0) {
            alert("행을 선택해주세요.");
            return;
        }

        let endChk = true;
        selected.map(row => {
            if ('printEnd' == urlPath) {
                if ('true' == row.printEndYn) {
                    endChk = false;
                }
            }
        });

        if (!endChk) {
            alert("이미 인쇄완료 된건 입니다.");
            return;
        }

        let notEndChk = true;
        selected.map(row => {
            if ('cancel-printEnd' == urlPath) {
                if ('false' == row.printEndYn) {
                    notEndChk = false;
                }
            }
        });

        if (!notEndChk) {
            alert("인쇄완료 된건만 취소가능 합니다.");
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
                table.ajax.reload(null, false);
            })
            .catch(error => {
                console.error(`${actionName} 처리 중 오류 발생:`, error);
                let errorMessage = "처리 중 오류가 발생했습니다. 콘솔을 확인해주세요.";
                if (error && error.responseJSON && error.responseJSON.message) {
                    errorMessage = error.responseJSON.message;
                } else if (error && error.responseText) {
                    errorMessage = error.responseText;
                }
                alert(errorMessage);
                table.ajax.reload(null, false);
            });
    }

    // 인쇄완료 버튼
    $('#btnPrintEnd').click(() => handleBatchAction('인쇄완료', 'printEnd'));

    // 인쇄완료 취소 버튼
    $('#btnPrintEndCnl').click(() => handleBatchAction('인쇄완료 취소', 'cancel-printEnd'));

});