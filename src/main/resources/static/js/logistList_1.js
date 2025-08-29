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
			// Redirect to login page if not authenticated
			window.location.href = '/login.html';
		});

	loadMenu('logistList_1.html');

	// 모든 검색 폼의 날짜 기본값 설정 (HTML에 .search-form 클래스 필요)
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

	// DataTables 초기화 함수 (코드 중복 제거)
	function initializeDataTable(selector, ajaxUrl, columnsConfig) {
		return $(selector).DataTable({
			responsive: true,
			ajax: {
				url: ajaxUrl,
				dataSrc: 'data',
				data: function (d) {
					// DataTables가 데이터를 요청할 때마다 해당 탭의 검색 폼 데이터를 파라미터에 추가
					const searchData = $(selector).closest('.tab-content').find('.search-form').serializeArray();
					$.each(searchData, function (i, field) {
						d[field.name] = field.value;
					});
				}
			},
			scrollY: getTableHeight("370"),
			columns: columnsConfig,
			createdRow: function (row, data, dataIndex) {
				if (data.importantYn === '1') {
					$(row).addClass('highlight-row');
				}
			},
			searching: false,
			lengthChange: false,
			pageLength: 15,
			language: {
				emptyTable: "데이터가 없습니다.",
				info: "총 _TOTAL_개",
				infoEmpty: "",
				infoFiltered: "(_MAX_개 중에서 필터링됨)",
				paginate: { first: "<<", last: ">>", next: ">", previous: "<" }
			}
		});
	}

	// 공통 컬럼 렌더러
	const renderCheckbox = (data, type) => type === 'display' ? `<input type="checkbox" ${data == '1' ? 'checked' : ''} disabled>` : data;

	// 각 그리드에 대한 컬럼 정의
	const baseColumns = [
		{ title: '', orderable: false, className: 'dt-body-center', render: (data, type, row) => `<input type="checkbox" class="row-select" value="${row.printId || ''}">` },
		{ data: 'pickingDate', title: '피킹예정일', className: 'dt-center' },
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
		{ data: 'sizeText', title: '박스규격' },
		{ data: 'printMethod', title: '발송마감일', className: 'dt-center' },
		{ data: "outReadyYn", title: "출고준비", className: 'dt-center', render: renderCheckbox },
	];

	const columnsForGrid1 = [
		...baseColumns,
		{ data: "importantYn", title: "중요", className: 'dt-center', render: renderCheckbox },
		{
			data: 'printMemo', title: '인쇄참고사항',
			createdCell: function (td, cellData, rowData, row, col) {
				// 요청하신 대로 '인쇄참고사항' 내용의 색상을 빨간색으로 변경합니다.
				$(td).css('color', 'red');
			}
		},
		...extendedColumns
	];

	const columnsForGrid2And3 = [...baseColumns, ...extendedColumns];

	// DataTables 인스턴스 생성
	const table = initializeDataTable('#grid1', '/api/print-info/list1', columnsForGrid1);
	const table2 = initializeDataTable('#grid2', '/api/print-info/list1', columnsForGrid2And3); // TODO: API URL 변경 필요
	const table3 = initializeDataTable('#grid3', '/api/print-info/list1', columnsForGrid2And3); // TODO: API URL 변경 필요

	// 조회 버튼 클릭 이벤트
	$(document).on('click', '.btn-search', function (e) {
		e.preventDefault(); // form submit 방지
		// 현재 탭의 DataTables 인스턴스를 찾아 ajax.reload()를 호출
		const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
		tableInstance.ajax.reload();
	});

	// 행 클릭 이벤트 (상세 팝업)
	$(document).on('click', '.main-content table.display tbody tr', function (e) {
		if ($(e.target).is('input, a')) return; // 체크박스나 링크 클릭 시 제외
		const tableInstance = $(this).closest('table.display').DataTable();
		const data = tableInstance.row(this).data();
		if (data) {
			window.open(`assetDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=1000');
		}
	});

	// 체크박스 클릭 시 해당 행 선택/해제 (단일 선택)
	$(document).on('click', '.main-content table.display tbody input.row-select', function (e) {
		e.stopPropagation(); // 행 클릭 이벤트 전파 방지
		const $table = $(this).closest('table.display');
		const $row = $(this).closest('tr');

		if ($(this).is(':checked')) {
			$table.find('tbody tr.selected').removeClass('selected');
			$table.find('tbody input.row-select').not(this).prop('checked', false);
			$row.addClass('selected');
		} else {
			$row.removeClass('selected');
		}
	});

	// 선택된 행 가져오기
	function getSelectedRows(tableInstance) {
		return tableInstance.rows('.selected').data().toArray();
	}

	// 상세보기 버튼
	$(document).on('click', '.btn-detail', function () {
		const tableInstance = $(this).closest('.tab-content').find('table.display').DataTable();
		const selected = getSelectedRows(tableInstance);
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}
		const data = selected[0];
		window.open(`assetDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=1000');
	});

	// 공통 액션 처리 함수
	function handleBatchAction(actionName, urlPath, button) {
		const tableInstance = $(button).closest('.tab-content').find('table.display').DataTable();
		const selected = getSelectedRows(tableInstance);
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}

		if (urlPath === 'cancel-out-ready') {
			const notOutReadyItems = selected.filter(row => row.outReadyYn !== '1');
			if (notOutReadyItems.length > 0) {
				alert("출고완료된 항목만 출고준비를 취소할 수 있습니다.");
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
				tableInstance.ajax.reload(null, false); // 페이징 유지하고 새로고침
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

	// 피킹완료 버튼
	$(document).on('click', '.btn-picking', function () {
		handleBatchAction('피킹완료', 'picking', this);
	});

	// 피킹취소 버튼 (New)
	$(document).on('click', '.btn-cancel-picking', function () {
		handleBatchAction('피킹취소', 'cancel-picking', this);
	});

	// 출고준비완료 버튼
	$(document).on('click', '.btn-out-ready', function () {
		handleBatchAction('출고준비완료', 'out-ready', this);
	});

	// 출고준비취소 버튼 (New)
	$(document).on('click', '.btn-cancel-out-ready', function () {
		handleBatchAction('출고준비취소', 'cancel-out-ready', this);
	});

	// 탭 클릭 이벤트 핸들러
	$(document).on('click', '.tab-button', function () {
		const tabName = $(this).text();
		// alert(tabName);

		$('.tab-button').removeClass('active');
		$(this).addClass('active');

		$('.tab-content').hide();

		const activeTabContentId = $(this).data('tab');
		$('#' + activeTabContentId).css('display', 'flex');

		$.fn.dataTable.tables({ visible: true, api: true }).columns.adjust();
	});
});