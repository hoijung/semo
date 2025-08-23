// jQuery의 document.ready()를 사용하여 DOM이 완전히 로드된 후 스크립트를 실행합니다.
		// 이는 페이지별 JavaScript를 초기화하는 표준적이고 안정적인 방법입니다.
		$(document).ready(function () {
			let table;
			let selectedRow = null;
			let currentSelectedId = null;

			// 1. .menu-container에 menu.html을 로드합니다.
			$('.menu-container').load('menu.html', function() {
				// 2. 메뉴 로드가 완료된 후, 이벤트 리스너를 설정하고 현재 페이지 메뉴를 활성화합니다.
				const menuBar = document.getElementById('menuBar');
				if (menuBar) {
					// 현재 페이지(main.html)에 해당하는 메뉴 항목에 'active' 클래스를 추가합니다.
					const currentPageMenuItem = menuBar.querySelector('[data-page="main.html"]');
					if (currentPageMenuItem) {
						// 기존 active 클래스를 모두 제거하고 현재 페이지만 활성화합니다.
						menuBar.querySelectorAll('.menu-item').forEach(item => item.classList.remove('active'));
						currentPageMenuItem.classList.add('active');
					}

					// 메뉴 클릭 이벤트 리스너
					menuBar.addEventListener('click', (event) => {
						const target = event.target.closest('.menu-item');
						//if (target && !target.classList.contains('active')) {
							const pageToLoad = target.getAttribute('data-page');
							//if (pageToLoad && pageToLoad !== 'initial') {
								location.href = pageToLoad;
							//}
						//}
					});
				}
			});

			table = $('#printTable').DataTable({
				ajax: {url: '/api/prints', dataSrc: ''},
				columns: [
					{data: '인쇄ID'},
					{data: '주문일자'}
				],
				destroy: true, // 동적으로 로드되는 콘텐츠에서 재초기화를 허용하는 중요한 옵션입니다.
				paging: false,
				info: false,
				searching: false
			});

			// 테이블 행 클릭 이벤트
			$('#printTable tbody').on('click', 'tr', function () {
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
			$('#btnAdd').on('click', addRecord);
			$('#btnUpdate').on('click', updateRecord);
			$('#btnDelete').on('click', deleteRecord);

			// 초기 버튼 상태 설정 (수정/삭제 비활성화)
			updateButtonState(false);

			function fillDetailForm(data) {
				resetDetailFormValues();

				for (let key in data) {
					const el = $('#' + key);
					if (el.length) {
						if (el.attr('type') === 'checkbox') {
							el.prop('checked', data[key]);
						} else if (el.attr('type') === 'date') {
							if (data[key]) {
								el.val(data[key].substring(0, 10));
							} else {
								el.val('');
							}
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
					const $link = $('<a>', {href: fileUrl, text: logoFileName, target: '_blank'});
					const $image = $('<img>', {src: fileUrl, alt: '로고 이미지', style: 'max-width: 100%; max-height: 100px; margin-top: 5px; display: block;'});
					$displayDiv.append($link).append($image);
				}
			}

			function createMultipartFormData(data) {
				const formData = new FormData();
				formData.append('dto', new Blob([JSON.stringify(data)], {type: 'application/json'}));
				const fileInput = $('#인쇄로고예시_file')[0];
				if (fileInput.files && fileInput.files[0]) {
					formData.append('logoFile', fileInput.files[0]);
				}
				return formData;
			}

			function getFormData() {
				const formData = {};
				$('.detail-card').find('input, textarea, select').each(function () {
					const id = $(this).attr('id');
					if (!id || this.type === 'file') return;

					if ($(this).attr('type') === 'checkbox') {
						formData[id] = $(this).prop('checked');
					} else {
						formData[id] = $(this).val();
					}
				});
				return formData;
			}

			function reloadTableAndRestoreSelection() {
				table.ajax.reload(function () {
					if (currentSelectedId !== null) {
						let foundRow = null;
						table.rows().every(function () {
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
							return response.text().then(text => {throw new Error('Network response was not ok: ' + text)});
						}
						return response.json();
					})
					.then(data => {
						alert("새 레코드가 등록되었습니다.");
						currentSelectedId = data.인쇄ID; // 새로 추가된 레코드의 ID를 설정합니다.
						reloadTableAndRestoreSelection();
					})
					.catch(error => {
						console.error("Add Error:", error);
						alert("레코드 등록 실패: " + error.message);
					});
			}

			function updateRecord() {
				const id = $('#인쇄ID').val();
				if (!id) {alert("수정할 레코드를 선택하세요."); return;}
				if (!confirm("수정 하시겠습니까?")) return;

				const jsonData = getFormData();
				const multipartFormData = createMultipartFormData(jsonData);

				fetch('/api/prints/' + id, {
					method: 'PUT',
					body: multipartFormData
				})
					.then(response => {
						if (!response.ok) {
							return response.text().then(text => {throw new Error('Network response was not ok: ' + text)});
						}
						return response.json();
					})
					.then(data => {
						reloadTableAndRestoreSelection();
					})
					.catch(error => {
						console.error("Update Error:", error);
						alert("레코드 수정 실패: " + error.message);
					});
			}

			function deleteRecord() {
				const id = $('#인쇄ID').val();
				if (!id) {alert("삭제할 레코드를 선택하세요."); return;}
				if (!confirm("정말 삭제하시겠습니까?")) return;

				fetch('/api/prints/' + id, {method: 'DELETE'})
					.then(response => {
						if (!response.ok) {
							return response.text().then(text => {throw new Error('Network response was not ok: ' + text)});
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
					})
					.catch(error => console.error('Error loading sales channels:', error));
			}

			// Call loadSalesChannels when the document is ready
			loadSalesChannels();

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
		});