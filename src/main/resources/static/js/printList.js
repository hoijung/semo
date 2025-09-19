$(document).ready(function () {
    let selectedPrintId = null;

    $('#btnPrintEnd').hide();
    $('#btnPrintEndCnl').hide();
    $('#btnSave').hide();
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
                if ('인쇄 서비스팀' == user.authList[key].screenId) {
                    $('#btnPrintEnd').show();
                    $('#btnPrintEndCnl').show();
                    $('#btnSave').show();
                }
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
    startDay.setDate(startDay.getDate() - 10);
    // yyyy-MM-dd 형식으로 변환
    const formattedStart = startDay.toISOString().split("T")[0];
    // input 기본값 설정
    inputStart.value = formattedStart;

    // 공통 컬럼 렌더러
    const renderCheckbox = (data, type) => type === 'display' ? `<input type="checkbox" ${eval(data) ? 'checked' : ''} disabled>` : data;

    const table = $('#grid').DataTable({
        responsive: true,
        dom: 'rtip',
        ajax: {
            url: '/api/prints/printList1?' + $('#searchForm').serialize(),
            dataSrc: 'data'
        },
        buttons: [
            {
                extend: "excel",
                className: "btn-sm",
                title: "인쇄 작업 목록",
                filename: `인쇄작업목록_${formattedToday}`,
                exportOptions: {
                    rows: '.selected'
                }
            }
        ],
        scrollY: getTableHeight("320"),
        scrollX: true,
        columns: [
            {
                title: '<input type="checkbox" id="selectAll" title="전체 선택/해제">',
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

            { data: 'deliveryDeadline', title: '발송최종기한', className: 'dt-center' },
            { data: 'itemName', title: '품목명' },
            { data: 'bagColor', title: '컬러' },
            { data: 'size', title: '사이즈' },
            { data: 'quantity', title: '장수' },
            { data: 'printSide', title: '인쇄면' },

            { data: 'printCount', title: '인쇄도수', className: 'dt-center' },
            { data: 'boxCount', title: '박스수량' },
            { data: 'printMemo' }, /*인쇄참고사항*/
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
            { data: 'logoColor', title: '로고인쇄색상' },

            { data: 'logoSize', title: '로고인쇄크기' },
            { data: 'logoPosition', title: '로고위치' },
            { data: 'colorData1', title: '조색데이터1' },
            { data: 'colorData2', title: '조색데이터2' },
            { data: 'colorData3', title: '조색데이터3' },

            { data: "pickingYn", title: "피킹완료", className: 'dt-center', render: renderCheckbox },
            { data: "printEndYn", title: "인쇄완료", className: 'dt-center', render: renderCheckbox },

        ],
        createdRow: function (row, data, dataIndex) {
            if (eval(data.importantYn)) {
                $(row).addClass('highlight-row');
            }
        },
        searching: true,
        lengthChange: false,
        paging: false,
        info: false,
        columnDefs: [
            { targets: "_all", className: "dt-center" }
        ],
        language: {
            emptyTable: "데이터가 없습니다."
        },
        drawCallback: function(settings) {
            const api = this.api();
            // 데이터가 로드된 후 첫 번째 행을 선택합니다.
            // 이전에 선택된 행이 있다면, 새로운 데이터셋의 첫 행을 선택하기 전에 초기화합니다.
            if (api.rows({ page: 'current' }).count() > 0) {
                const firstRowNode = api.row(0, { page: 'current' }).node();
                if (firstRowNode && !$(firstRowNode).hasClass('selected')) {
                    $(firstRowNode).trigger('click');
                }
            }
        }
    });

    $('#customSearch').on('keyup', function () {
        table.search(this.value).draw();
    });

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

    $('#btnExcel').on('click', function (e) {
        e.preventDefault();

        // 체크된 행들을 찾습니다.
        const checkedRows = table.rows().nodes().to$().find('input.row-select:checked').closest('tr');

        if (checkedRows.length === 0) {
            alert('다운로드할 행을 선택해주세요.');
            return;
        }

        // 엑셀 내보내기를 위해 체크된 행에 '.selected' 클래스를 임시로 추가합니다.
        checkedRows.addClass('selected');

        // 엑셀 버튼 트리거
        table.buttons('.buttons-excel').trigger();

        // 내보내기 후 임시 클래스를 다시 제거합니다.
        checkedRows.removeClass('selected');
    });

    $('#grid tbody').on('click', 'tr', function () {
        // 행 클릭 시 단일 선택 로직
        const $row = $(this);
        const data = table.row(this).data();

        if ($row.hasClass('selected')) {
            // 이미 선택된 행을 다시 클릭하면 선택 해제
            $row.removeClass('selected');
            selectedPrintId = null;
            $('#edit-area').hide();
        } else {
            // 다른 행을 클릭하면 기존 선택을 해제하고 새로 선택
            table.rows('.selected').nodes().to$().removeClass('selected');
            $row.addClass('selected');
            selectedPrintId = data.printId;
            $('#조색데이터1_edit').val(data.colorData1);
            $('#조색데이터2_edit').val(data.colorData2);
            $('#조색데이터3_edit').val(data.colorData3);
            $('#edit-area').show();
        }
    });

    $('#grid tbody').on('click', 'input.row-select', function (e) {
        const $row = $(this).closest('tr');
        // 체크박스 클릭은 행의 'selected' 상태에 영향을 주지 않고,
        // 행 클릭 이벤트가 중복 실행되지 않도록 이벤트 전파를 막습니다.
        e.stopPropagation();
    });

    // '전체 선택' 체크박스 클릭 이벤트 (안정성을 위해 document에 위임)
    $(document).on('click', '#selectAll', function () {
        const rows = table.rows({ page: 'current' }).nodes();
        const isChecked = this.checked;
        $('input.row-select', rows).prop('checked', isChecked).trigger('change');
    });

    // 개별 체크박스 변경 시 '전체 선택' 체크박스 상태 업데이트
    $('#grid tbody').on('change', 'input.row-select', function () {
        updateSelectAllCheckbox();
    });

    // 테이블이 다시 그려질 때(페이지 이동, 검색 등) '전체 선택' 체크박스 상태 업데이트
    table.on('draw', function () {
        updateSelectAllCheckbox();
    });

    function updateSelectAllCheckbox() {
        const currentPageRows = table.rows({ page: 'current' });
        const numRows = currentPageRows.count();
        const numSelectedRows = currentPageRows.nodes().to$().find('input.row-select:checked').length;

        $('#selectAll').prop('checked', numRows > 0 && numRows === numSelectedRows);
    }

    function getSelectedRows() {
        // '상세보기', '인쇄완료' 등 일괄 작업은 체크된 항목을 기준으로 동작하도록 변경합니다.
        return table.rows().nodes().to$().find('input.row-select:checked').closest('tr').map(function () {
            return $('#grid').DataTable().row(this).data();
        }).get();
    }

    $('#btnDetail').click(function () {
        const selected = getSelectedRows();
        const selectedCount = selected.length;

        if (selectedCount === 0) {
            alert("행을 선택해주세요.");
            return;
        }
        if (selectedCount > 10) {
            // alert("상세보기는 최대 10건까지만 선택할 수 있습니다.");
            // return;
        }

        const printIds = selected.map(row => row.printId).join(',');
        window.open(`printDetail.html?printId=${printIds}`, 'detailPopup', 'width=1000,height=900,scrollbars=yes');
    });

    $('#btnSave').on('click', async function () {
        if (!selectedPrintId) {
            alert("행을 선택해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append('printId', selectedPrintId);
        formData.append('colorData1', $('#조색데이터1_edit').val());
        formData.append('colorData2', $('#조색데이터2_edit').val());
        formData.append('colorData3', $('#조색데이터3_edit').val());

        const imageFile = $('#인쇄팀사진_file')[0].files[0];
        if (imageFile) {
            const options = {
                maxSizeMB: 1,
                maxWidthOrHeight: 1920,
                useWebWorker: true
            };
            try {
                const compressedFile = await imageCompression(imageFile, options);
                formData.append('photo', compressedFile, compressedFile.name);
            } catch (error) {
                console.error('Error during image compression:', error);
                formData.append('photo', imageFile);
            }
        }

        if (!confirm("조색 데이터를 저장하시겠습니까?")) {
            return;
        }

        $.ajax({
            url: '/api/print-info/update-color-data',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert("저장되었습니다.");
                table.ajax.reload(null, false);
                $('#edit-area').hide();
                selectedPrintId = null;
            },
            error: function (error) {
                console.error("Error saving color data:", error);
                alert("저장 중 오류가 발생했습니다.");
            }
        });
    });

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

    $('#btnPrintEnd').click(() => handleBatchAction('인쇄완료', 'printEnd'));

    $('#btnPrintEndCnl').click(() => handleBatchAction('인쇄완료 취소', 'cancel-printEnd'));

    // Image popup logic
    $('#grid tbody').on('click', '.clickable-image', function (e) {
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