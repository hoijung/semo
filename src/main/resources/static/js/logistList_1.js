$(document).ready(function () {
    $('#btnPicking2').hide();
    $('#btnCancelPicking2').hide();
    $('#btnOutReady3').hide();
    $('#btnCancelOutReady3').hide();

    // Fetch user authority and set up UI
    fetch('/api/auth/user')
        .then(response => {
            if (!response.ok) {
                throw new Error('Not authenticated');
            }
            return response.json();
        })
        .then(user => {
            //  debugger
            for (let key in user.authList) {
                // console.log(user.authList[key]);
                if ('물류팀' == user.authList[key].screenId) {
                    $('#btnPicking2').show();
                    $('#btnCancelPicking2').show();
                    $('#btnOutReady3').show();
                    $('#btnCancelOutReady3').show();
                }
            }
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            window.location.href = '/login.html';
        });

    let selectedPrintIdForEdit = null;


    loadMenu('logistList_1.html');

    $('.search-form').each(function () {
        const $form = $(this);
        const inputStart = $form.find('input[name="orderDateStart"]');
        const inputEnd = $form.find('input[name="orderDateEnd"]');
        const today = new Date();
        const formattedToday = today.toISOString().split("T")[0];
        if (inputEnd.length) inputEnd.val(formattedToday);
        const startDay = new Date();
        startDay.setDate(startDay.getDate() - 10);
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
            },
            initComplete: function (settings, json) {
                // '전체목록' 탭의 테이블('#grid1')이 처음 로드될 때만 첫 행을 선택합니다.
                if (this.api().table().node().id === 'grid1') {
                    const firstRow = this.api().row(0).node();
                    if (firstRow) {
                        $(firstRow).trigger('click');
                    }
                }
            },
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
        { title: '<input type="checkbox" class="select-all">', orderable: false, className: 'dt-body-center', render: (data, type, row) => `<input type="checkbox" class="row-select" value="${row.printId || ''}">` },
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
        // {
        //     data: 'printMemo', title: '인쇄참고사항',
        //     createdCell: function (td, cellData, rowData, row, col) {
        //         $(td).css('color', 'red');
        //     }
        // },
        ...extendedColumns,
        {
            data: 'printPhoto',
            title: '인쇄팀사진',
            render: function (data, type, row) {
                if (data) {
                    return `<img src="/File/${data}" alt="인쇄팀사진" height="50" class="clickable-image" data-src="/File/${data}">`;
                }
                return '';
            }
        },
    ];

    const columnsForGrid2 = [...baseColumns, { data: 'itemName', title: '품목명' }, { data: 'bagColor', title: '컬러' }, { data: 'size', title: '사이즈' }, { data: 'quantity', title: '장수' }, { data: "pickingYn", title: "피킹완료", className: 'dt-center', render: renderCheckbox }
        , {
        data: 'printPhoto',
        title: '인쇄팀사진',
        render: function (data, type, row) {
            if (data) {
                return `<img src="/File/${data}" alt="인쇄팀사진" height="50" class="clickable-image" data-src="/File/${data}">`;
            }
            return '';
        }
    },
    ];
    const columnsForGrid3 = [...baseColumns, { data: 'phoneNumber', title: '전화번호' }, { data: 'deliveryZip', title: '우편번호', className: 'dt-center' }, { data: 'deliveryAddress', title: '주소' }, { data: 'sizeText', title: '박스규격' }, { data: 'deliveryDeadline', title: '발송마감일', className: 'dt-center' }, { data: "outReadyYn", title: "출고완료", className: 'dt-center', render: renderCheckbox }
        , {
        data: 'printPhoto',
        title: '인쇄팀사진',
        render: function (data, type, row) {
            if (data) {
                return `<img src="/File/${data}" alt="인쇄팀사진" height="50" class="clickable-image" data-src="/File/${data}">`;
            }
            return '';
        }
    },
    ];

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
        // ajax.reload 후 데이터 로드가 완료되면 첫 행을 선택합니다.
        tableInstance.ajax.reload(() => {
            const firstRow = tableInstance.row(0, { page: 'current' }).node();
            if (firstRow) {
                $(firstRow).trigger('click');
            }
        });
    });

    // 행 클릭 시 단일 선택(하이라이트) 처리
    $(document).on('click', '.main-content table.display tbody tr', function (e) {
        // 체크박스 클릭 시에는 이 이벤트가 동작하지 않도록 함
        if ($(e.target).is('input.row-select')) {
            return;
        }

        const $row = $(this);
        const $table = $row.closest('table');
        const tableId = $table.attr('id');
        const data = $table.DataTable().row($row).data();

        // Hide edit area if not on the first tab
        if (tableId !== 'grid1') {
            $('#editArea1').hide();
            selectedPrintIdForEdit = null;
        }

        if ($row.hasClass('selected')) {
            $row.removeClass('selected');
            // Hide edit area on deselect
            if (tableId === 'grid1') {
                $('#editArea1').hide();
                selectedPrintIdForEdit = null;
            }
        } else {
            $table.find('tr.selected').removeClass('selected');
            $row.addClass('selected');

            // Show edit area only for the first tab's grid
            if (tableId === 'grid1' && data) {
                selectedPrintIdForEdit = data.printId;
                $('#boxCount_edit').val(data.boxCount);
                $('#editArea1').show();
            } else {
                $('#editArea1').hide();
                selectedPrintIdForEdit = null;
            }
        }
    });

    // 체크박스 클릭 시 다중 선택 처리
    $(document).on('click', '.main-content table.display tbody input.row-select', function (e) {
        // 행 클릭 이벤트가 중복으로 실행되지 않도록 이벤트 전파를 막습니다.
        e.stopPropagation();
    });

    // Save Box Count button click event
    $('#btnSaveBoxCount').on('click', function () {
        if (!selectedPrintIdForEdit) {
            alert('수정할 행을 선택해주세요.');
            return;
        }

        const newBoxCount = $('#boxCount_edit').val();
        if (newBoxCount === '' || newBoxCount < 0) {
            alert('유효한 박스수량을 입력해주세요.');
            return;
        }

        if (!confirm('박스수량을 저장하시겠습니까?')) {
            return;
        }

        $.ajax({
            url: `/api/print-info/${selectedPrintIdForEdit}/box-count`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ boxCount: newBoxCount }),
            success: function (response) {
                alert('박스수량이 성공적으로 수정되었습니다.');
                const tableInstance = $('#grid1').DataTable();
                // ajax.reload 후 데이터 로드가 완료되면 첫 행을 선택합니다.
                tableInstance.ajax.reload(() => {
                    const firstRow = tableInstance.row(0, { page: 'current' }).node();
                    if (firstRow) {
                        $(firstRow).trigger('click');
                    }
                }, false); // 페이징을 유지하면서 리로드
                $('#editArea1').hide();
                selectedPrintIdForEdit = null;
            },
            error: function (xhr) {
                alert('수정 중 오류가 발생했습니다: ' + (xhr.responseJSON ? xhr.responseJSON.message : '서버 오류'));
            }
        });
    });

    // 각 테이블 인스턴스에 대해 '전체 선택' 이벤트를 개별적으로 바인딩합니다.
    [table, table2, table3].forEach(t => {
        $(t.table().header()).on('click', '.select-all', function () {
            const rows = t.rows({ 'search': 'applied' }).nodes();
            const isChecked = $(this).is(':checked');
            $('input.row-select', rows).prop('checked', isChecked).trigger('change');
        });
    });


    // 개별 체크박스 변경 시 '전체선택' 체크박스 상태 업데이트
    $(document).on('change', '.row-select', function () {
        const tableInstance = $(this).closest('table').DataTable();
        updateSelectAllCheckbox(tableInstance);
    });

    // 테이블 draw 이벤트 발생 시 '전체선택' 체크박스 상태 업데이트
    [table, table2, table3].forEach(t => {
        t.on('draw', () => updateSelectAllCheckbox(t));
    });

    function updateSelectAllCheckbox(tableInstance) {
        const allRows = tableInstance.rows({ 'search': 'applied' }).nodes();
        const checkedRows = $(allRows).find('input.row-select:checked').length;
        $(tableInstance.table().header()).find('.select-all').prop('checked', allRows.length > 0 && allRows.length === checkedRows);
    }

    function getSelectedRows(tableInstance) {
        // 일괄 작업의 기준을 '.selected'가 아닌 체크된 체크박스로 변경
        const checkedIds = tableInstance.rows({ 'search': 'applied' }).nodes().to$().find('input.row-select:checked').map(function () {
            return $(this).val();
        }).get();

        return tableInstance.rows((idx, data, node) => {
            return checkedIds.includes(String(data.printId));
        }).data().toArray();
    }

    $(document).on('click', '.btn-detail', function () {
        const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
        const selected = getSelectedRows(tableInstance);
        if (selected.length === 0) {
            alert("상세보기할 행을 체크해주세요.");
            return;
        }
        if (selected.length > 10) {
            // alert("상세보기는 최대 10건까지만 가능합니다.");
            // return;
        }
        // 선택된 모든 항목의 printId를 쉼표로 구분된 문자열로 만듭니다.
        const printIds = selected.map(row => row.printId).join(',');
        // URL 파라미터로 모든 ID를 전달하여 팝업을 엽니다.
        window.open(`printDetail.html?printId=${printIds}`, 'detailPopup', 'width=1500,height=900,scrollbars=yes');
    });

    $(document).on('click', '.btn-excel', function () {
        const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
        // Find all checked rows within the current search results
        const checkedRows = tableInstance.rows({ 'search': 'applied' }).nodes().to$().find('input.row-select:checked').closest('tr');

        if (checkedRows.length === 0) {
            alert('엑셀로 다운로드할 행을 체크해주세요.');
            return;
        }

        // Temporarily add the '.selected' class to checked rows for the export
        checkedRows.addClass('selected');

        // Trigger the Excel export
        tableInstance.buttons('.buttons-excel').trigger();

        // Remove the temporary class after the export is triggered
        checkedRows.removeClass('selected');
    });

    function handleBatchAction(actionName, urlPath, button) {
        const tableInstance = $(button).closest('.tab-content').find('table.display').DataTable();
        const selected = getSelectedRows(tableInstance);
        if (selected.length === 0) {
            alert("처리할 행을 체크해주세요.");
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

        // Hide edit area when switching tabs
        $('#editArea1').hide();
        selectedPrintIdForEdit = null;

        const tableInstance = $activeTabContent.find('table.display').DataTable();

        // ajax.reload 후 데이터 로드가 완료되면 첫 행을 선택합니다.
        tableInstance.ajax.reload(() => {
            const firstRow = tableInstance.row(0).node();
            if (firstRow) {
                $(firstRow).trigger('click');
            }
        });
    });

        // Image popup logic
    $('#grid1 tbody').on('click', '.clickable-image', function (e) {
        e.stopPropagation();
        const src = $(this).data('src');
        $('#popupImage').attr('src', src);
        $('#imagePopup').css('display', 'flex');
    });

    $('.image-popup-close, .image-popup-overlay').on('click', function () {
        $('#imagePopup').hide();
    });

    $('.image-popup-content').on('click', function (e) {
        e.stopPropagation();
    });
});