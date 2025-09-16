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
            if (authority !== '관리자' && authority !== '물류팀') {
                $('#btnPicking').hide();
                $('#btnOutReady').hide();
                $('#btnCancelPicking').hide();
                $('#btnCancelOutReady').hide();
            }
            if (authority === '모든 데이터 조회') {
                $('#btnDetail').hide();
            }
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            window.location.href = '/login.html';
        });

    loadMenu('logistList_1.html');

    $('.search-form').each(function () {
        const $form = $(this);
        const inputStart = $form.find('input[name="orderDateStart"]');
        const inputEnd = $form.find('input[name="orderDateEnd"]');
        const today = new Date();
        const formattedToday = today.toISOString().split("T")[0];
        if (inputEnd.length) inputEnd.val(formattedToday);
        const startDay = new Date();
        startDay.setDate(startDay.getDate() - 31);
        const formattedStart = startDay.toISOString().split("T")[0];
        if (inputStart.length) inputStart.val(formattedStart);
    });

    const today = new Date();
    const formattedToday = today.toISOString().split("T")[0];

    function initializeDataTable(selector, ajaxUrl, columnsConfig) {
        return $(selector).DataTable({
            responsive: true,
            dom: 'rtip', // 'f' 제거하여 기본 검색창 숨김
            ajax: {
                url: ajaxUrl,
                dataSrc: 'data',
                data: function (d) {
                    const searchData = $(selector).closest('.tab-content').find('.search-form').serializeArray();
                    $.each(searchData, function (i, field) {
                        d[field.name] = field.value;
                    });
                }
            },
            buttons: [
                {
                    extend: "excel",
                    className: "buttons-excel",
                    title: "물류 작업 목록",
                    filename: `물류작업목록_${formattedToday}`,
                    exportOptions: {
                        rows: '.selected'
                    }
                }
            ],
            scrollY: getTableHeight("390"),
            scrollX: true,
            columns: columnsConfig,
            createdRow: function (row, data, dataIndex) {
                if (data.importantYn === '1' || data.importantYn === 'true') {
                    $(row).addClass('highlight-row');
                }
            },
            searching: true, // 커스텀 검색을 위해 기능 활성화
            lengthChange: false,
            paging: false,
            info: false,
            columnDefs: [
                { targets: "_all", className: "dt-center" }
            ],
            language: {
                emptyTable: "데이터가 없습니다."
            }
        });
    }

    const renderCheckbox = (data, type) => {
        if (type === 'display') {
            const isChecked = data === '1' || data === true || data === 'true';
            return `<input type="checkbox" ${isChecked ? 'checked' : ''} disabled>`;
        }
        return data;
    };

    const baseColumns = [
        { title: '', orderable: false, className: 'dt-body-center', render: (data, type, row) => `<input type="checkbox" class="row-select" value="${row.printId || ''}">` },
        { data: 'orderDate', title: '주문일자', className: 'dt-center' },
        { data: 'weekDay', title: '요일', className: 'dt-center' },
        // { data: 'pickingDate', title: '피킹예정일', className: 'dt-center' },
        { data: 'printTeam', title: '담당팀' },
        { data: 'companyContact', title: '업체명(고객명)' },
    ];

    const extendedColumns = [
        { data: 'itemName', title: '품목명' },
        { data: 'bagColor', title: '컬러' },
        { data: 'size', title: '사이즈' },
        { data: 'quantity', title: '장수' },
        { data: "pickingYn", title: "피킹완료", className: 'dt-center', render: renderCheckbox },
        { data: 'phoneNumber', title: '전화번호' },
        { data: 'deliveryZip', title: '우편번호', className: 'dt-center' },
        { data: 'deliveryAddress', title: '주소' },
        { data: 'boxCount', title: '박스수량' },
        { data: 'deliveryDeadline', title: '발송마감일', className: 'dt-center' },
        { data: "outReadyYn", title: "출고완료", className: 'dt-center', render: renderCheckbox },
    ];

    const columnsForGrid1 = [
        ...baseColumns,
        { data: "importantYn", title: "중요", className: 'dt-center', render: renderCheckbox },
        {
            data: 'printMemo', title: '인쇄참고사항',
            createdCell: function (td, cellData, rowData, row, col) {
                $(td).css('color', 'red');
            }
        },
        ...extendedColumns
    ];

    const columnsForGrid2 = [...baseColumns, { data: 'itemName', title: '품목명' }, { data: 'bagColor', title: '컬러' }, { data: 'size', title: '사이즈' }, { data: 'quantity', title: '장수' }, { data: "pickingYn", title: "피킹완료", className: 'dt-center', render: renderCheckbox }];
    const columnsForGrid3 = [...baseColumns, { data: 'phoneNumber', title: '전화번호' }, { data: 'deliveryZip', title: '우편번호', className: 'dt-center' }, { data: 'deliveryAddress', title: '주소' }, { data: 'sizeText', title: '박스규격' }, { data: 'deliveryDeadline', title: '발송마감일', className: 'dt-center' }, { data: "outReadyYn", title: "출고완료", className: 'dt-center', render: renderCheckbox }];

    const table = initializeDataTable('#grid1', '/api/print-info/logistic-list1', columnsForGrid1);
    const table2 = initializeDataTable('#grid2', '/api/print-info/logistic-list2', columnsForGrid2);
    const table3 = initializeDataTable('#grid3', '/api/print-info/logistic-list3', columnsForGrid3);

    // 커스텀 검색 이벤트 리스너
    $(document).on('keyup', '.custom-search-input', function () {
        const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
        tableInstance.search(this.value).draw();
    });

    $(document).on('click', '.btn-search', function (e) {
        e.preventDefault();
        const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
        tableInstance.ajax.reload();
    });

    $(document).on('click', '.main-content table.display tbody tr', function (e) {
        if ($(e.target).is('input, a')) return;
        const tableInstance = $(this).closest('table.display').DataTable();
        const data = tableInstance.row(this).data();
        if (data) {
            //  window.open(`printDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1500,height=900');
        }
    });

    $(document).on('click', '.main-content table.display tbody input.row-select', function (e) {
        e.stopPropagation();
        $(this).closest('tr').toggleClass('selected', this.checked);
    });

    function getSelectedRows(tableInstance) {
        return tableInstance.rows('.selected').data().toArray();
    }

    $(document).on('click', '.btn-detail', function () {
        const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
        const selected = getSelectedRows(tableInstance);
        if (selected.length === 0) {
            alert("행을 선택해주세요.");
            return;
        }
        const data = selected[0];
        window.open(`printDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=900');
    });

    $(document).on('click', '.btn-excel', function () {
        const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
        const selectedRows = tableInstance.rows('.selected').count();
        if (selectedRows === 0) {
            alert('엑셀로 다운로드할 행을 선택해주세요.');
            return;
        }
        tableInstance.buttons('.buttons-excel').trigger();
    });

    function handleBatchAction(actionName, urlPath, button) {
        const tableInstance = $(button).closest('.tab-content').find('table.display').DataTable();
        const selected = getSelectedRows(tableInstance);
        if (selected.length === 0) {
            alert("행을 선택해주세요.");
            return;
        }

        if (urlPath === 'picking') {
            const chkItems = selected.filter(row => row.pickingYn == 'true');
            if (chkItems.length > 0) {
                alert("피킹미완료 항목만 피킹완료 할 수 있습니다.");
                return;
            }
        }

        if (urlPath === 'cancel-picking') {
            const chkItems = selected.filter(row => row.pickingYn == 'false');
            if (chkItems.length > 0) {
                alert("피킹완료 항목만 피킹완료 취소할 수 있습니다.");
                return;
            }
        }

        if (urlPath === 'out-ready') {
            const chkItems = selected.filter(row => row.outReadyYn == 'true');
            if (chkItems.length > 0) {
                alert("출고미완료 항목만 출고완료 할 수 있습니다.");
                return;
            }
        }

        if (urlPath === 'cancel-out-ready') {
            const notOutReadyItems = selected.filter(row => row.outReadyYn !== 'true');
            if (notOutReadyItems.length > 0) {
                alert("출고완료된 항목만 출고완료를 취소할 수 있습니다.");
                return;
            }
        }
        if (!confirm(`선택한 ${selected.length}개의 항목을 ${actionName} 처리하시겠습니까?`)) {
            return;
        }

        const requests = selected.map(row => $.post(`/api/print-info/${row.printId}/${urlPath}`));

        Promise.all(requests)
            .then(responses => {
                alert(`${responses.length}개의 항목이 성공적으로 ${actionName} 처리되었습니다.`);
                tableInstance.ajax.reload(null, false);
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
                tableInstance.ajax.reload(null, false);
            });
    }

    $(document).on('click', '.btn-picking', function () {
        handleBatchAction('피킹완료', 'picking', this);
    });

    $(document).on('click', '.btn-cancel-picking', function () {
        handleBatchAction('피킹취소', 'cancel-picking', this);
    });

    $(document).on('click', '.btn-out-ready', function () {
        handleBatchAction('출고완료', 'out-ready', this);
    });

    $(document).on('click', '.btn-cancel-out-ready', function () {
        handleBatchAction('출고완료취소', 'cancel-out-ready', this);
    });

    $(document).on('click', '.tab-button', function () {
        const tabName = $(this).text();
        $('.tab-button').removeClass('active');
        $(this).addClass('active');
        $('.tab-content').hide();
        const activeTabContentId = $(this).data('tab');
        const $activeTabContent = $('#' + activeTabContentId);
        $activeTabContent.css('display', 'flex');
        $.fn.dataTable.tables({ visible: true, api: true }).columns.adjust();
        const tableInstance = $activeTabContent.find('table.display').DataTable();
        tableInstance.ajax.reload();
    });
});