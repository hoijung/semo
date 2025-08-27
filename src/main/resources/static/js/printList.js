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
	// 1. .menu-container에 menu.html을 로드합니다.
	$('.menu-container').load('menu.html', function () {
		// 2. 메뉴 로드가 완료된 후, 이벤트 리스너를 설정하고 현재 페이지 메뉴를 활성화합니다.
		const menuBar = document.getElementById('menuBar');
		if (menuBar) {
			// 현재 페이지(main.html)에 해당하는 메뉴 항목에 'active' 클래스를 추가합니다.
			const currentPageMenuItem = menuBar.querySelector('[data-page="printList.html"]');
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

	function getTableHeight() {
        // 예: 화면 높이에서 200px 여유 공간 빼기
        return $(window).height() - 300 + "px";
    }

	const table = $('#grid').DataTable({
		responsive: true,
		ajax: {
			url: '/api/prints/printList1',
			dataSrc: 'data'
		},
		scrollY: getTableHeight(), // 동적으로 높이 지정
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
			{ data: 'printTeam'},
			{ data: 'companyContact'},
			{ data: 'companyContact'},
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
			// {
			// 	data: "outReadyYn", title: "작업완료"
			// 	, className: 'dt-center'
			// 	, render: function (data, type, row) {
			// 		if (type === 'display') {
			// 			return `<input type="checkbox" ${data == '1' ? 'checked' : ''} disabled>`;
			// 		}
			// 		return data;
			// 	}
			// },
			{
				data: "printEndYn", title: "인쇄완료"
				, className: 'dt-center'
				, render: function (data, type, row) {
					if (type === 'display') {
						return `<input type="checkbox" ${data == '1' ? 'checked' : ''} disabled>`;
					}
					return data;
				}
			}

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

	// 인쇄완료 버튼
	$('#btnPrintEnd').click(function () {
		const selected = getSelectedRows();
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}
		// AJAX 호출로 서버 업데이트 예시
		selected.forEach(row => {
			$.post(`/api/print-info/${row.printId}/printEnd`, { status: 'Y' });
		});
		alert("선택한 행 인쇄완료 처리되었습니다.");
		$('#grid').DataTable().ajax.reload(); // 테이블 새로고침
	});

	// 인쇄완료 취소 버튼
	$('#btnPrintEndCnl').click(function () {
		const selected = getSelectedRows();
		if (selected.length === 0) {
			alert("행을 선택해주세요.");
			return;
		}
		// AJAX 호출로 서버 업데이트 예시
		selected.forEach(row => {
			$.post(`/api/print-info/${row.printId}/cancel-printEnd`, { status: 'Y' });
		});
		alert("선택한 행 인쇄완료 취소되었습니다.");
		$('#grid').DataTable().ajax.reload(); // 테이블 새로고침
	});

});