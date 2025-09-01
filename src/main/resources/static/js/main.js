function calculateVat() {
	// 공급가액 입력 필드에서 값 가져오기
	const supplyPriceInput = document.getElementById('공급가액');
	const supplyPrice = parseFloat(removeCommas(supplyPriceInput.value));

	// 부가세 및 합계금액 필드 가져오기
	const vatInput = document.getElementById('부가세액');
	const totalPriceInput = document.getElementById('합계금액');

	// 값이 숫자인지 확인
	if (!isNaN(supplyPrice)) {
		// 부가세 계산 (공급가액의 10%)
		const vat = supplyPrice * 0.1;
		// 합계금액 계산 (공급가액 + 부가세)
		const totalPrice = supplyPrice + vat;

		// 결과 표시
		vatInput.value = vat.toFixed(0); // 소수점 없이 표시
		totalPriceInput.value = totalPrice.toFixed(0); // 소수점 없이 표시

		onlyNumberWithComma(vatInput);
		onlyNumberWithComma(totalPriceInput);
	} else {
		// 숫자가 아니면 필드 비우기
		vatInput.value = '';
		totalPriceInput.value = '';
	}
}

// Function to remove commas from a string
function removeCommas(str) {
	return str.replace(/,/g, '');
}

function formatNumberWithCommas(number) {
	// Ensure it's an integer before formatting
	return Math.round(number).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}


function onlyNumberWithComma(obj) {
	obj.value = Number(obj.value.replace(/[^0-9]/g, '')).toLocaleString();
}


	// Function to load item names into the 품목명 combobox
	function loadItemNames() {
		fetch('/api/commoncodes/group/품목명')
			.then(response => response.json())
			.then(data => {
				const 품목명Select = $('#품목명');
				품목명Select.empty(); // Clear existing options
				품목명Select.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					품목명Select.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading item names:', error));
	}

	// Call loadItemNames when the document is ready
	loadItemNames();

	// Function to toggle the tax info section based on sales channel
	function toggleTaxInfoSection() {
		const salesChannel = $('#판매채널').val();
		const isDirectDeposit = salesChannel === '직접입금';

		// Enable/disable all inputs, selects, and buttons within the tax info group
		$('#tax-info-group').find('input, select, button').prop('disabled', !isDirectDeposit);
	}

	// Function to load sales channels into the 판매채널 combobox
	function loadSalesChannels() {
		fetch('/api/commoncodes/group/판매채널')
			.then(response => response.json())
			.then(data => {
				const salesChannelSelect = $('#판매채널');
				salesChannelSelect.empty(); // Clear existing options
				salesChannelSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					salesChannelSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
				// After loading, set the initial state for the tax section
				toggleTaxInfoSection();
			})
			.catch(error => console.error('Error loading sales channels:', error));
	}

	// Call loadSalesChannels when the document is ready
	loadSalesChannels();

	// Add change event listener to the sales channel dropdown
	$('#판매채널').on('change', toggleTaxInfoSection);


	// Function to load print types into the 인쇄방식 combobox
	function loadPrintTypes() {
		fetch('/api/commoncodes/group/인쇄방식')
			.then(response => response.json())
			.then(data => {
				const printTypeSelect = $('#인쇄방식');
				printTypeSelect.empty(); // Clear existing options
				printTypeSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					printTypeSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading print types:', error));
	}

	// Call loadPrintTypes when the document is ready
	loadPrintTypes();

	// Function to load item names into the 인쇄도수 combobox
	function loadPrintCounts() {
		fetch('/api/commoncodes/group/인쇄도수')
			.then(response => response.json())
			.then(data => {
				const pSelect = $('#인쇄도수');
				pSelect.empty(); // Clear existing options
				pSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					pSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading item names:', error));
	}

	// Call loadItemNames when the document is ready
	loadPrintCounts();

	// Function to load item names into the 인쇄면 combobox
	function loadPrintSides() {
		fetch('/api/commoncodes/group/인쇄면')
			.then(response => response.json())
			.then(data => {
				const pSelect = $('#인쇄면');
				pSelect.empty(); // Clear existing options
				pSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					pSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading item names:', error));
	}

	// Call loadItemNames when the document is ready
	loadPrintSides();

	// Function to load item names into the 인쇄팀 combobox
	function loadPrintTeams() {
		fetch('/api/commoncodes/group/인쇄담당팀')
			.then(response => response.json())
			.then(data => {
				const pSelect = $('#인쇄담당팀');
				pSelect.empty(); // Clear existing options
				pSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					pSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading item names:', error));
	}

	// Call loadItemNames when the document is ready
	loadPrintTeams();

	// Function to load item names into the 배송타입 combobox
	function loadDeliveryTypes() {
		fetch('/api/commoncodes/group/배송타입')
			.then(response => response.json())
			.then(data => {
				const pSelect = $('#배송타입');
				pSelect.empty(); // Clear existing options
				pSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					pSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading item names:', error));
	}

	// Call loadItemNames when the document is ready
	loadDeliveryTypes();

	// Function to load item names into the 계산서발행타입 combobox
	function loadTaxTypes() {
		fetch('/api/commoncodes/group/계산서발행타입')
			.then(response => response.json())
			.then(data => {
				const pSelect = $('#계산서발행타입');
				pSelect.empty(); // Clear existing options
				pSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					pSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading item names:', error));
	}

	// Call loadItemNames when the document is ready
	loadTaxTypes();

	// Function to load item names into the 계산서발행타입 combobox
	function loadPrintMethods() {
		fetch('/api/commoncodes/group/인쇄방법')
			.then(response => response.json())
			.then(data => {
				const pSelect = $('#인쇄방법');
				pSelect.empty(); // Clear existing options
				pSelect.append('<option value="">선택하세요</option>'); // Add a default option
				data.forEach(code => {
					pSelect.append(`<option value="${code.codeName}">${code.codeName}</option>`);
				});
			})
			.catch(error => console.error('Error loading item names:', error));
	}

	// Call loadItemNames when the document is ready
	loadPrintMethods();

// jQuery의 document.ready()를 사용하여 DOM이 완전히 로드된 후 스크립트를 실행합니다.
// 이는 페이지별 JavaScript를 초기화하는 표준적이고 안정적인 방법입니다.
$(document).ready(function() {
	fetch('/api/auth/user')
		.then(response => response.json())
		.then(user => {
			document.getElementById('greeting').textContent = `${user.userName} 님`;

			const authority = user.authority;
			if (authority !== '관리자' ) {
				$('#btnDistribute').hide();
				$('#btnCancelDistribute').hide();
				$('#btnAdd').hide();
				$('#btnUpdate').hide();
				$('#btnDelete').hide();
			}
		})
		.catch(error => console.error('Error fetching user data:', error));
	let table;
	let selectedRow = null;
	let currentSelectedId = null;
	let isFormDirty = false; // 폼 변경 여부를 추적하는 플래그

	loadMenu('main.html');

	// 이미지 팝업 열기
	$('#detail-container').on('click', '.image-popup-trigger', function (e) {
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

	function getTableHeight() {
		// 예: 화면 높이에서 200px 여유 공간 빼기
		return $(window).height() - 200 + "px"; 
	}

	table = $('#printTable').DataTable({
		ajax: { url: '/api/prints', dataSrc: '' },
		order: [[0, 'desc']],
		scrollY: getTableHeight(), // 동적으로 높이 지정
		columns: [
			{ data: '인쇄ID', title: 'No' },
			{ data: '주문일자' },
			{
				data: '배분여부', title: '배분', // Assuming this field exists in the API response
				render: function(data, type, row) {
					return eval(data) ? 'Y' : 'N'; // Display 'Y' for true, 'N' for false
				}
			}
		],
		destroy: true, // 동적으로 로드되는 콘텐츠에서 재초기화를 허용하는 중요한 옵션입니다.
		paging: false,
		info: false,
		searching: false,
		drawCallback: function(settings) {
			// This is to ensure that the selection happens only on the initial draw.
			if (this.api().page.info().page === 0 && !this.api().state.loaded()) {
				// Select the first row
				const firstRow = $('#printTable tbody tr:first');
				if (firstRow.length) {
					firstRow.trigger('click');
				}
			}
		}
	});

	// 테이블 행 클릭 이벤트
	$('#printTable tbody').on('click', 'tr', function() {
		// 폼에 저장되지 않은 변경사항이 있는지 확인
		if (isFormDirty) {
			if (!confirm('변경사항이 저장되지 않았습니다. 다른 항목을 선택하시겠습니까?')) {
				return; // 사용자가 '취소'를 누르면 아무 작업도 하지 않음
			}
			// 사용자가 '확인'을 누르면 isFormDirty를 초기화하고 계속 진행
			isFormDirty = false;
		}

		const data = table.row(this).data();
		if (!data) return;

		if (selectedRow) {
			$(selectedRow).removeClass('selected-row');
		}

		$(this).addClass('selected-row');
		selectedRow = this;

		currentSelectedId = data.인쇄ID;

		fillDetailForm(data);
		updateButtonState(true);
	});

	// 버튼 이벤트 리스너
	$('#btnDistribute').on('click', distributeRecord); // New button
	$('#btnCancelDistribute').on('click', cancelDistributeRecord); // New button
	$('#btnAdd').on('click', addRecord);
	$('#btnUpdate').on('click', updateRecord);
	$('#btnDelete').on('click', deleteRecord);

	// 폼 내의 모든 입력 요소에 대한 변경 감지
	$('#detail-container').on('change', 'input, select, textarea', function() {
		isFormDirty = true;
		console.log("Form is now dirty.");
	});

	// 초기 버튼 상태 설정 (수정/삭제 비활성화)
	updateButtonState(false);

	function fillDetailForm(data) {
		resetDetailFormValues();

		for (let key in data) {
			const el = $('#' + key);
			if (el.length) {
				if (el.attr('type') === 'checkbox') {
					// debugger
					el.prop('checked', eval(data[key]));
				} else if (el.attr('type') === 'date') {
					if (data[key]) {
						el.val(data[key].substring(0, 10));
					} else {
						el.val('');
					}
				} else if (key === '공급가액' || key === '부가세액' || key === '합계금액') {
					//el.val(data[key]);
					el.val(formatNumberWithCommas(data[key]));
				} else {

					el.val(data[key]);
				}
			}
		}

		// 인쇄로고예시 이미지 표시
		const logoFileName = data.인쇄로고예시;
		const $displayDiv = $('#인쇄로고예시_display');
		if (logoFileName) {
			const fileUrl = `/File/${logoFileName}`;
			// 팝업을 띄우는 링크로 변경
			const $link = $('<a>', { href: '#', text: logoFileName, 'data-image-url': fileUrl, 'class': 'image-popup-trigger' });
			const $image = $('<img>', { src: fileUrl, alt: '로고 이미지', style: 'max-width: 100%; max-height: 100px; margin-top: 5px; display: block;' });
			
			$displayDiv.append($link).append($image);
		}

		// Set initial state of tax info section
		toggleTaxInfoSection();
		isFormDirty = false; // 행 선택 시 폼 상태 초기화
	}

	function createMultipartFormData(data) {
		const formData = new FormData();
		formData.append('dto', new Blob([JSON.stringify(data)], { type: 'application/json' }));
		const fileInput = $('#인쇄로고예시_file')[0];
		if (fileInput.files && fileInput.files[0]) {
			formData.append('logoFile', fileInput.files[0]);
		}
		return formData;
	}

	function getFormData() {
		const formData = {};
		$('.detail-card').find('input, textarea, select').each(function() {
			const id = $(this).attr('id');
			if (!id || this.type === 'file') return;

			if ($(this).attr('type') === 'checkbox') {
				formData[id] = $(this).prop('checked');
			} else if (id === '공급가액' || id === '부가세액' || id === '합계금액') {
				//debugger
				// 입력값에서 콤마 제거
				let rawValue = $(this).val().replace(/,/g, "");
				// 숫자형 변환
				formData[id] = parseInt(rawValue, 10) || 0;
			}
			else {
				formData[id] = $(this).val();
			}
		});
		return formData;
	}

	function reloadTableAndRestoreSelection() {
		table.ajax.reload(function() {
			if (currentSelectedId !== null) {
				let foundRow = null;
				table.rows().every(function() {
					const rowData = this.data();
					if (rowData && rowData.인쇄ID === currentSelectedId) {
						foundRow = this.node();
						fillDetailForm(rowData);
						return false;
					}
				});

				$('#printTable tbody tr').removeClass('selected-row');

				if (foundRow) {
					$(foundRow).addClass('selected-row');
					selectedRow = foundRow;
					updateButtonState(true);
				} else {
					resetDetailForm();
					updateButtonState(false);
				}
			} else {
				resetDetailForm();
				updateButtonState(false);
			}
		}, false);
	}

	function distributeRecord() {
		const id = $('#인쇄ID').val();
		if (!id) {
			alert("배분할 레코드를 선택하세요.");
			return;
		}
		if ('true' == $('#배분여부').val()) {
			alert("이미 배분된 레코드입니다.");
			return;
		}
		if (!confirm("선택된 레코드를 부서배분 하시겠습니까?")) return;

		// Assuming an API endpoint for distribution
		fetch('/api/prints/' + id + '/distribute', {
			method: 'POST' // Or PUT, depending on API design
		})
			.then(response => {
				if (!response.ok) {
					return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
				}
				return response.json(); // Or response.text() if no JSON is returned
			})
			.then(data => {
				alert("부서배분이 완료되었습니다.");
				reloadTableAndRestoreSelection(); // Reload table to reflect status change
			})
			.catch(error => {
				console.error("Distribution Error:", error);
				alert("부서배분 실패: " + error.message);
			});
	}

	function cancelDistributeRecord() {
		const id = $('#인쇄ID').val();
		if (!id) {
			alert("취소할 레코드를 선택하세요.");
			return;
		}
		if ('false' == $('#배분여부').val()) {
			alert("배분된 건만 가능합니다.");
			return;
		}
		if (!confirm("선택된 레코드의 부서배분을 취소하시겠습니까?")) return;

		// Assuming an API endpoint for canceling distribution
		fetch('/api/prints/' + id + '/cancelDistribute', {
			method: 'POST' // Or PUT, depending on API design
		})
			.then(response => {
				if (!response.ok) {
					// Read the error message from the response
					return response.text().then(text => { throw new Error(text) });
				}
				return response.json(); // Or response.text() if no JSON is returned
			})
			.then(data => {
				alert("부서배분이 취소되었습니다.");
				reloadTableAndRestoreSelection(); // Reload table to reflect status change
			})
			.catch(error => {
				console.error("Cancel Distribution Error:", error);
				alert("부서배분 취소 실패: " + error.message);
			});
	}

	function addRecord() {
		const jsonData = getFormData();
		if (!confirm("등록 하시겠습니까?")) return;

		delete jsonData.인쇄ID;
		const multipartFormData = createMultipartFormData(jsonData);

		fetch('/api/prints', {
			method: 'POST',
			body: multipartFormData
		})
			.then(response => {
				if (!response.ok) {
					return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
				}
				return response.json();
			})
			.then(data => {
				alert("새 레코드가 등록되었습니다.");
				currentSelectedId = data.인쇄ID; // 새로 추가된 레코드의 ID를 설정합니다.
				isFormDirty = false; // 등록 후 폼 상태 초기화
				reloadTableAndRestoreSelection();
			})
			.catch(error => {
				console.error("Add Error:", error);
				alert("레코드 등록 실패: " + error.message);
			});
	}

	function updateRecord() {
		const id = $('#인쇄ID').val();
		if (!id) { alert("수정할 레코드를 선택하세요."); return; }

		// 유효성 검사: 주문일자, 업체명(담당자)
		const orderDate = $('#주문일자').val();
		if (!orderDate) {
			alert('주문일자를 입력해주세요.');
			$('#주문일자').focus();
			return;
		}
		const companyName = $('#업체명_담당자').val().trim();
		if (!companyName) {
			alert('업체명(담당자)을 입력해주세요.');
			$('#업체명_담당자').focus();
			return;
		}

		if (!confirm("수정 하시겠습니까?")) return;

		const jsonData = getFormData();
		const multipartFormData = createMultipartFormData(jsonData);

		fetch('/api/prints/' + id, {
			method: 'PUT',
			body: multipartFormData
		})
			.then(response => {
				if (!response.ok) {
					return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
				}
				return response.json();
			})
			.then(data => {
				isFormDirty = false; // 수정 후 폼 상태 초기화
				reloadTableAndRestoreSelection();
			})
			.catch(error => {
				console.error("Update Error:", error);
				alert("레코드 수정 실패: " + error.message);
			});
	}

	function deleteRecord() {
		const id = $('#인쇄ID').val();
		if (!id) { alert("삭제할 레코드를 선택하세요."); return; }
		if (!confirm("정말 삭제하시겠습니까?")) return;

		fetch('/api/prints/' + id, { method: 'DELETE' })
			.then(response => {
				if (!response.ok) {
					return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
				}
				currentSelectedId = null;
				reloadTableAndRestoreSelection();
			})
			.catch(error => {
				console.error("Delete Error:", error);
				alert("레코드 삭제 실패: " + error.message);
			});
	}

	function resetDetailFormValues() {
		$('#detail-container').find('input[type="text"], input[type="number"], input[type="date"], textarea').val('');
		$('#detail-container').find('input[type="checkbox"]').prop('checked', false);
		$('#인쇄ID').val('');
		$('#인쇄로고예시_display').empty();
		$('#인쇄로고예시_file').val('');
		isFormDirty = false; // 폼 값 초기화 시 상태 초기화
	}

	function resetDetailForm() {
		resetDetailFormValues();

		if (selectedRow) {
			$(selectedRow).removeClass('selected-row');
			selectedRow = null;
		}
		currentSelectedId = null;
	}

	function updateButtonState(enable) {
		$('#btnAdd').prop('disabled', false).addClass('enabled');

		$('#btnUpdate, #btnDelete').prop('disabled', !enable)
			.toggleClass('enabled', enable);
	}

});