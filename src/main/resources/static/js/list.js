$(document).ready(function () {
	loadMenu('list.html');

	// 이미지 팝업 열기
	$('#grid').on('click', '.image-popup-trigger', function (e) {
		e.preventDefault();
		const imageUrl = $(this).data('image-url');
		if (imageUrl) {
			$('#popupImage').attr('src', imageUrl);
			$('#imagePopup').css('display', 'flex');
		}
	});

	// 이미지 팝업 닫기 (팝업 외부 또는 닫기 버튼 클릭)
	$(document).on('click', '#imagePopup, .image-popup-close', function (e) {
		if (e.target === this || $(e.target).hasClass('image-popup-close')) {
			$('#imagePopup').hide();
		}
	});

	const inputStart = document.getElementById("orderDateStart");
	const inputEnd = document.getElementById("orderDateEnd");
	const today = new Date();
	const formattedToday = today.toISOString().split("T")[0];
	inputEnd.value = formattedToday;

	const startDay = new Date();
	startDay.setDate(startDay.getDate() - 31);
	const formattedStart = startDay.toISOString().split("T")[0];
	inputStart.value = formattedStart;

	const query = $('#searchForm').serialize();

	const table = $('#grid').DataTable({
		responsive: true,
		dom: 'rtip', // 'f'를 제거하여 기본 검색창을 숨김
		ajax: {
			url: '/api/prints/search?' + query,
			dataSrc: 'data'
		},
		buttons: [
			{
				extend: "excel",
				className: "btn-sm",
				title: "인쇄 작업 목록",
				filename: `인쇄작업목록_${formattedToday}`
			}
		],
		scrollY: getTableHeight("350"),
		scrollX: true,
		columns: [
			{ data: null, render: () => `<input type="checkbox" class="row-select">` },
			{ data: 'printId' },
			{ data: 'companyContact' },
			{ data: 'customerId' },
			{ data: 'printTeam' },
			{ data: 'salesChannel' },
			{ data: 'printMethod' },
			{ data: 'oldOrderYn' },
			{ data: 'itemName' },
			{ data: 'bagColor' },
			{ data: 'size' },
			{ data: 'quantity' },
			{ data: 'printTeam' },
			{ data: 'printSide' },
			{ data: 'printCount' },
			{
				data: 'logoSamplePath',
				title: '예시파일명',
				render: function (data, type, row) {
					if (data) {
						return `<a href="#" class="image-popup-trigger" data-image-url="/File/${data}">${data}</a>`;
					}
					return '';
				}
			},
			{ data: 'boxSize' },
			{ data: 'boxCount' },
			{ data: 'deliveryType' },
			{ data: 'deliveryAddress' },
			{ data: 'billPubYn' },
			{ data: 'companyContact' },
			{ data: 'bussNo' },
			{ data: 'representativeName' },
			{ data: 'email' },
			{ data: 'taxAmount' },
			{ data: 'supplyAmount' },
			{ data: 'orderDate', title: '주문일자', className: 'dt-center' },
			{ data: 'pickingEndAt', title: '피킹완료', className: 'dt-center' },
			{ data: 'printEndAt', title: '인쇄완료', className: 'dt-center' },
			{ data: 'outReadyAt', title: '출고완료', className: 'dt-center' },
			{ data: 'pickingDate', title: '발송마감일', className: 'dt-center' },
		],
		createdRow: function (row, data, dataIndex) {
			if (eval(data.importantYn)) {
				$(row).addClass('highlight-row');
			}
		},
		searching: true, // 커스텀 검색을 위해 기능은 활성화
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
		window.open(`assetDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=1000');
	});

	// 피킹완료 버튼
	$('#btnPicking').click(function () {
		const selected = getSelectedRows();
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}
		selected.forEach(row => {
			$.post(`/api/print-info/${row.printId}/picking`, { status: 'Y' });
		});
		alert("선택한 행 피킹완료 처리되었습니다.");
		$('#grid').DataTable().ajax.reload();
	});

	// 출고완료완료 버튼
	$('#btnOutReady').click(function () {
		const selected = getSelectedRows();
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}
		selected.forEach(row => {
			$.post(`/api/print-info/${row.printId}/out-ready`, { status: 'Y' });
		});
		alert("선택한 행 출고완료 완료 처리되었습니다.");
		$('#grid').DataTable().ajax.reload();
	});
});