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
		// 이미지 자체를 클릭했을 때는 닫히지 않도록 함
		if (e.target === this || $(e.target).hasClass('image-popup-close')) {
			$('#imagePopup').hide();
		}
	});

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


	// form 직렬화 (검색조건을 한 번에 쿼리스트링으로)
	const query = $('#searchForm').serialize();

	// 새로운 url로 다시 로드
	//table.ajax.url('/api/prints/search?' + query).load();

	const table = $('#grid').DataTable({
		responsive: true,
		dom: 'frtip', // 'B'를 제거하여 기본 버튼 컨테이너를 숨김
		ajax: {
			url: '/api/prints/search?' + $('#searchForm').serialize(),
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
		scrollY: getTableHeight("410"), // 동적으로 높이 지정
		scrollX: true,   // ✅ 좌우 스크롤 허용
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
						// 파일명을 클릭 가능한 링크로 만들고, 이미지 URL을 data 속성에 저장
						return `<a href="#" class="image-popup-trigger" data-image-url="/File/${data}">${data}</a>`;
					}
					return ''; // 데이터가 없으면 빈 문자열 반환
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
			{ data: 'pickingDate', title: '피킹예정일', className: 'dt-center' },
			{ data: 'pickingDate', title: '피킹완료', className: 'dt-center' },
			{ data: 'pickingDate', title: '피킹인쇄완료', className: 'dt-center' },
			{ data: 'pickingDate', title: '출고준비', className: 'dt-center' },
			{ data: 'pickingDate', title: '발송마감일', className: 'dt-center' },
		],
		// 아래 'createdRow' 옵션을 추가합니다.
		createdRow: function (row, data, dataIndex) {
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
		columnDefs: [
			{ targets: "_all", className: "dt-center" } // 전체 컬럼 가운데 정렬
		],
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

	// 엑셀 다운로드 버튼 클릭 이벤트
	$('#btnExcel').on('click', function () {
		// DataTables의 excel 버튼 기능을 프로그래밍 방식으로 실행
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