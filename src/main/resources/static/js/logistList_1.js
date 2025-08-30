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

	const table = $('#grid').DataTable({
		responsive: true,
		ajax: {
			url: '/api/print-info/list1',
			dataSrc: 'data'
		},
		scrollY: getTableHeight("370"), // 동적으로 높이 지정
		columns: [
			{
				title: '',  // 체크박스 컬럼
				orderable: false,
				className: 'dt-body-center',
				render: function (data, type, row, meta) {
					return `<input type="checkbox" class="row-select">`;
				}
			},
			{ data: 'pickingDate', title: '피킹예정일', className: 'dt-center' },
			{ data: 'printTeam', title: '담당팀' },
			{ data: 'companyContact', title: '업체명(고객명)' },
			{ data: 'itemName', title: '품목명' },
			{ data: 'bagColor', title: '컬러' },
			{ data: 'size', title: '사이즈' },
			{ data: 'quantity', title: '장수' },
			{
				data: "pickingYn", title: "피킹완료"
				, className: 'dt-center'
				, render: function (data, type, row) {
					if (type === 'display') {
						return `<input type="checkbox" ${data == '1' ? 'checked' : ''} disabled>`;
					}
					return data;
				}
			},
			{ data: 'phoneNumber', title: '전화번호' },
			{ data: 'deliveryZip', title: '우편번호', className: 'dt-center' },
			{ data: 'deliveryAddress', title: '주소' },
			{ data: 'sizeText', title: '박스규격' },
			{ data: 'printMethod', title: '발송마감일', className: 'dt-center' },
			{
				data: "outReadyYn", title: "출고준비"
				, className: 'dt-center'
				, render: function (data, type, row) {
					if (type === 'display') {
						return `<input type="checkbox" ${data == '1' ? 'checked' : ''} disabled>`;
					}
					return data;
				}
			},
		],
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
		table.ajax.url('/api/print-info/list1?' + query).load();
		// table.ajax.url('/api/print-info/list1?' + $('#searchForm').serialize()).load();
	});

	// 클릭 이벤트
	$('#grid tbody').on('click', 'tr', function () {
		const data = table.row(this).data();
		if (data) {
			window.open(`assetDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=1000');
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
		// 선택된 행 중 첫 번째 항목의 상세 정보를 엽니다.
		const data = selected[0];
		window.open(`assetDetail.html?printId=${data.printId}`, 'detailPopup', 'width=1000,height=1000');
	});

	// 공통 액션 처리 함수
	function handleBatchAction(actionName, urlPath) {
		const selected = getSelectedRows();
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}

		// Client-side validation for "출고준비취소"
		if (urlPath === 'cancel-out-ready') {
			const notOutReadyItems = selected.filter(row => row.outReadyYn !== '1');
			if (notOutReadyItems.length > 0) {
				alert("출고완료된 항목만 출고준비를 취소할 수 있습니다.");
				return; // Prevent action if any item is not '출고완료'
			}
		}
		if (!confirm(`선택한 ${selected.length}개의 항목을 ${actionName} 처리하시겠습니까?`)) {
			return;
		}

		const requests = selected.map(row => {
			if (urlPath === 'picking' || urlPath === 'out-ready') {
				return $.post(`/api/print-info/${row.printId}/${urlPath}`, { status: 'Y' });
			} else { // For cancel-picking and cancel-out-ready
				return $.post(`/api/print-info/${row.printId}/${urlPath}`);
			}
		});

		Promise.all(requests)
			.then(responses => {
				alert(`${responses.length}개의 항목이 성공적으로 ${actionName} 처리되었습니다.`);
				$('#grid').DataTable().ajax.reload();
			})
			.catch(error => {
				console.error(`${actionName} 처리 중 오류 발생:`, error);
				// Attempt to parse error message from server response if available
				let errorMessage = "처리 중 오류가 발생했습니다. 콘솔을 확인해주세요.";
				if (error && error.responseJSON && error.responseJSON.message) {
					errorMessage = error.responseJSON.message;
				} else if (error && error.responseText) {
					errorMessage = error.responseText;
				}
				alert(errorMessage);
				// 실패하더라도 테이블을 새로고침하여 최신 상태를 반영합니다.
				$('#grid').DataTable().ajax.reload();
			});
	}

	// 피킹완료 버튼
	$('#btnPicking').click(function () {
		handleBatchAction('피킹완료', 'picking');
	});

	// 피킹취소 버튼 (New)
	$('#btnCancelPicking').click(function () {
		handleBatchAction('피킹취소', 'cancel-picking'); // New action
	});

	// 출고준비완료 버튼
	$('#btnOutReady').click(function () {
		handleBatchAction('출고준비완료', 'out-ready');
	});

	// 출고준비취소 버튼 (New)
	$('#btnCancelOutReady').click(function () {
		handleBatchAction('출고준비취소', 'cancel-out-ready'); // New action
	});
});