$(document).ready(function () {
	// 1. .menu-container에 menu.html을 로드합니다.
	$('.menu-container').load('menu.html', function () {
		// 2. 메뉴 로드가 완료된 후, 이벤트 리스너를 설정하고 현재 페이지 메뉴를 활성화합니다.
		const menuBar = document.getElementById('menuBar');
		if (menuBar) {
			// 현재 페이지(main.html)에 해당하는 메뉴 항목에 'active' 클래스를 추가합니다.
			const currentPageMenuItem = menuBar.querySelector('[data-page="list.html"]');
			if (currentPageMenuItem) {
				// 기존 active 클래스를 모두 제거하고 현재 페이지만 활성화합니다.
				menuBar.querySelectorAll('.menu-item').forEach(item => item.classList.remove('active'));
				currentPageMenuItem.classList.add('active');
			}

			// 메뉴 클릭 이벤트 리스너
			menuBar.addEventListener('click', (event) => {
				const target = event.target.closest('.menu-item');
				if (target && !target.classList.contains('active')) {
					const pageToLoad = target.getAttribute('data-page');
					if (pageToLoad && pageToLoad !== 'initial') {
						location.href = pageToLoad;
					}
				}
			});
		}
	});

	const table = $('#grid').DataTable({
		responsive: true,
		ajax: {
			url: '/api/prints/search',
			dataSrc: 'data'
		},
		scrollX: true,   // ✅ 좌우 스크롤 허용
		columns: [
        { data: null, render: () => `<input type="checkbox" class="row-select">` },
		{ data: 'printId' },
        { data: 'companyContact' },
        { data: 'customerId' },
        // { data: 'printTeam' },
		{ data: 'phoneNumber' },
		{ data: 'salesChannel' },

{ data: 'printMethod' },
{ data: 'orderYn' },
{ data: 'itemName' },
{ data: 'bagColor' },
{ data: 'size' },
{ data: 'quantity' },
{ data: 'printTeam' },
{ data: 'printSide' },
{ data: 'printCount' },

{ data: 'logoSamplePath' },
{ data: 'boxSize' },
{ data: 'boxCount' },
{ data: 'deliveryType'},
{ data: 'deliveryAddress'},
{ data: 'billPubYn'},
{ data: 'companyContact'},
{ data: 'bussNo'},
{ data: 'representativeName'},
{ data: 'email'},
{ data: 'taxAmount'},
{ data: 'supplyAmount'},


    ],
		// columns: [
		// 	{
		// 		title: '',  // 체크박스 컬럼
		// 		orderable: false,
		// 		className: 'dt-body-center',
		// 		render: function (data, type, row, meta) {
		// 			return `<input type="checkbox" class="row-select">`;
		// 		}
		// 	},
		// 	{ data: 'orderDate', title: '주문일자', className: 'dt-center' },
		// 	{ data: 'pickingDate', title: '피킹예정일', className: 'dt-center' },
		// 	{ data: 'printTeam', title: '담당팀' },
		// 	{ data: 'companyContact', title: '업체명(고객명)' },
		// 	{ data: 'itemName', title: '품목명' },
		// 	{ data: 'bagColor', title: '컬러' },
		// 	{ data: 'size', title: '사이즈' },
		// 	{ data: 'quantity', title: '장수' },
		// 	{
		// 		data: "pickingYn", title: "피킹완료"
		// 		, className: 'dt-center'
		// 		, render: function (data, type, row) {
		// 			if (type === 'display') {
		// 				return `<input type="checkbox" ${data == '1' ? 'checked' : ''} disabled>`;
		// 			}
		// 			return data;
		// 		}
		// 	},
		// 	{ data: 'phoneNumber', title: '전화번호' },
		// 	{ data: 'deliveryZip', title: '우편번호', className: 'dt-center' },
		// 	{ data: 'deliveryAddress', title: '주소' },
		// 	{ data: 'sizeText', title: '박스규격' },
		// 	{ data: 'quantity', title: '수량' },
		// 	{ data: 'printMethod', title: '발송마감일', className: 'dt-center' },
		// 	{
		// 		data: "outReadyYn", title: "출고준비"
		// 		, className: 'dt-center'
		// 		, render: function (data, type, row) {
		// 			if (type === 'display') {
		// 				return `<input type="checkbox" ${data == '1' ? 'checked' : ''} disabled>`;
		// 			}
		// 			return data;
		// 		}
		// 	},
		// ],
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
	$('#btnSearch').on('click', function (e) {
		e.preventDefault();   // form submit 방지
		e.stopPropagation();  // 이벤트 전파 방지 (선택사항)

		// form 직렬화 (검색조건을 한 번에 쿼리스트링으로)
		const query = $('#searchForm').serialize();

		// 새로운 url로 다시 로드
		table.ajax.url('/api/prints/search?' + query).load();
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
		// 여러 행 선택 가능, 첫 번째 행 상세 보기
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
		// AJAX 호출로 서버 업데이트 예시
		selected.forEach(row => {
			$.post(`/api/print-info/${row.printId}/picking`, { status: 'Y' });
		});
		alert("선택한 행 피킹완료 처리되었습니다.");
		$('#grid').DataTable().ajax.reload(); // 테이블 새로고침
	});

	// 출고준비완료 버튼
	$('#btnOutReady').click(function () {
		const selected = getSelectedRows();
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}
		selected.forEach(row => {
			$.post(`/api/print-info/${row.printId}/out-ready`, { status: 'Y' });
		});
		alert("선택한 행 출고준비 완료 처리되었습니다.");
		$('#grid').DataTable().ajax.reload();
	});
});