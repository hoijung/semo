$(document).ready(function () {
    let table;
    let selectedRow = null;
    let currentSelectedId = null;
    let authTable;
    let authSelectedRow = null;

    // 1. 메뉴 로드 및 활성화
    $('.menu-container').load('menu.html', function() {
        const menuBar = document.getElementById('menuBar');
        if (menuBar) {
            const currentPageMenuItem = menuBar.querySelector('[data-page="user.html"]');
            if (currentPageMenuItem) {
                menuBar.querySelectorAll('.menu-item').forEach(item => item.classList.remove('active'));
                currentPageMenuItem.classList.add('active');
            }

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

    // 2. DataTable 초기화
    table = $('#userTable').DataTable({
        ajax: {url: '/api/users/list', dataSrc: ''},
        columns: [
            {data: 'userId'},
            {data: 'userName'},
        ],
        destroy: true,
        paging: false,
        info: false,
        searching: false,
        drawCallback: function(settings) {
            if (this.api().rows({page: 'current'}).data().length > 0) {
                const firstRow = $('#userTable tbody tr:first');
                if (!firstRow.hasClass('selected-row')) {
                    firstRow.trigger('click');
                }
            }
        }
    });

    authTable = $('#authGrid').DataTable({
        paging: false,
        info: false,
        searching: false,
        columns: [
            { 
                data: 'screenId',
                render: function(data, type, row, meta) {
                    if (row.isNew) {
                        return '<input type="text" class="screenId-input" value="'>''
                    }
                    return data;
                }
            },
            {
                data: 'canCreate',
                render: function (data, type, row) {
                    return '<input type="checkbox" class="auth-check" data-auth-type="canCreate" ' + (data === 'Y' ? 'checked' : '') + '>';
                }
            },
            {
                data: 'canUpdate',
                render: function (data, type, row) {
                    return '<input type="checkbox" class="auth-check" data-auth-type="canUpdate" ' + (data === 'Y' ? 'checked' : '') + '>';
                }
            },
            {
                data: 'canDelete',
                render: function (data, type, row) {
                    return '<input type="checkbox" class="auth-check" data-auth-type="canDelete" ' + (data === 'Y' ? 'checked' : '') + '>';
                }
            }
        ]
    });

    // 3. 테이블 행 클릭 이벤트
    $('#userTable tbody').on('click', 'tr', function () {
        const data = table.row(this).data();
        if (!data) return;

        if (selectedRow) {
            $(selectedRow).removeClass('selected-row');
        }

        $(this).addClass('selected-row');
        selectedRow = this;
        currentSelectedId = data.userId;

        fillDetailForm(data);
        loadAuthGrid(data.userId);
        updateButtonState(true);
    });

    $('#authGrid tbody').on('click', 'tr', function () {
        if (authSelectedRow) {
            $(authSelectedRow).removeClass('selected-row');
        }
        $(this).addClass('selected-row');
        authSelectedRow = this;
    });

    function loadAuthGrid(userId) {
        $.get('/api/user-screen-auth/' + userId, function (data) {
            authTable.clear().rows.add(data).draw();
        });
    }

    // 4. 버튼 이벤트 리스너
    $('#btnAdd').on('click', addRecord);
    $('#btnUpdate').on('click', updateRecord);
    $('#btnDelete').on('click', deleteRecord);
    $('#btnSaveAuth').on('click', saveAuth);
    $('#btnAddAuthRow').on('click', addAuthRow);
    $('#btnDeleteAuthRow').on('click', deleteAuthRow);

    // 초기 버튼 상태
    updateButtonState(false);

    function addAuthRow() {
        authTable.row.add({ isNew: true, screenId: '', canCreate: 'N', canUpdate: 'N', canDelete: 'N' }).draw();
    }

    function deleteAuthRow() {
        if (authSelectedRow) {
            authTable.row(authSelectedRow).remove().draw();
            authSelectedRow = null;
        }
    }

    function fillDetailForm(data) {
        resetDetailFormValues();
        for (let key in data) {
            const el = $('#' + key);
            if (el.length) {
                if (el.attr('type') === 'checkbox') {
                    el.prop('checked', data[key]);
                } else {
                    el.val(data[key]);
                }
            }
        }
    }

    function getFormData() {
        const formData = {};
        $('.detail-card').find('input').not('#authGrid input').each(function () {
            const id = $(this).attr('id');
            if (!id) return;

            if ($(this).attr('type') === 'checkbox') {
                formData[id] = $(this).prop('checked');
            } else {
                formData[id] = $(this).val();
            }
        });
        return formData;
    }

    function reloadTableAndRestoreSelection() {
        table.ajax.reload(() => {
            if (currentSelectedId !== null) {
                let foundRow = null;
                table.rows().every(function () {
                    const rowData = this.data();
                    if (rowData && rowData.userId === currentSelectedId) {
                        foundRow = this.node();
                        fillDetailForm(rowData);
                        loadAuthGrid(currentSelectedId);
                        return false;
                    }
                });

                $('#userTable tbody tr').removeClass('selected-row');

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

    function sendRequest(url, method, data) {
        const options = {
            method: method,
            headers: {'Content-Type': 'application/json'},
        };
        if (data) {
            options.body = JSON.stringify(data);
        }
        return fetch(url, options).then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error('Network response was not ok: ' + text) });
            }
            if (response.status === 204) return; // No Content
            return response.json();
        });
    }

    function addRecord() {
        const jsonData = getFormData();
        if (!jsonData.userName || !jsonData.userId || !jsonData.password) {
            alert("사용자명, 아이디, 비밀번호는 필수입니다.");
            return;
        }
        if (!confirm("등록 하시겠습니까?")) return;

        sendRequest('/api/users', 'POST', jsonData)
            .then(data => {
                alert("새 사용자가 등록되었습니다.");
                currentSelectedId = data.userId;
                reloadTableAndRestoreSelection();
            })
            .catch(error => {
                console.error("Add Error:", error);
                alert("사용자 등록 실패: " + error.message);
            });
    }

    function updateRecord() {
        const id = $('#userId').val();
        if (!id) { alert("수정할 사용자를 선택하세요."); return; }
        if (!confirm("수정 하시겠습니까?")) return;

        const jsonData = getFormData();
        if (jsonData.password === '') {
            delete jsonData.password;
        }

        sendRequest('/api/users/' + id, 'PUT', jsonData)
            .then(data => {
                alert("사용자 정보가 수정되었습니다.");
                reloadTableAndRestoreSelection();
            })
            .catch(error => {
                console.error("Update Error:", error);
                alert("사용자 수정 실패: " + error.message);
            });
    }

    function deleteRecord() {
        const id = $('#userId').val();
        if (!id) { alert("삭제할 사용자를 선택하세요."); return; }
        if (!confirm("정말 삭제하시겠습니까?")) return;

        sendRequest('/api/users/' + id, 'DELETE')
            .then(() => {
                alert("사용자가 삭제되었습니다.");
                currentSelectedId = null;
                reloadTableAndRestoreSelection();
            })
            .catch(error => {
                console.error("Delete Error:", error);
                alert("사용자 삭제 실패: " + error.message);
            });
    }

    function saveAuth() {
        const userId = $('#userId').val();
        if (!userId) {
            alert("사용자를 선택해주세요.");
            return;
        }

        const authData = [];
        authTable.rows().every(function () {
            const rowNode = this.node();
            const rowData = this.data();
            let screenId = rowData.screenId;
            if(rowData.isNew) {
                screenId = $(rowNode).find('.screenId-input').val();
            }

            const auth = {
                userId: userId,
                screenId: screenId,
                canCreate: $(rowNode).find('.auth-check[data-auth-type="canCreate"]').prop('checked') ? 'Y' : 'N',
                canUpdate: $(rowNode).find('.auth-check[data-auth-type="canUpdate"]').prop('checked') ? 'Y' : 'N',
                canDelete: $(rowNode).find('.auth-check[data-auth-type="canDelete"]').prop('checked') ? 'Y' : 'N',
            };
            authData.push(auth);
        });

        sendRequest('/api/user-screen-auth', 'POST', authData)
            .then(() => {
                alert("권한이 저장되었습니다.");
                loadAuthGrid(userId);
            })
            .catch(error => {
                console.error("Auth Save Error:", error);
                alert("권한 저장 실패: " + error.message);
            });
    }

    function resetDetailFormValues() {
        $('#detail-container').find('input[type="text"], input[type="password"], input[type="hidden"]').val('');
        $('#detail-container').find('input[type="checkbox"]').prop('checked', false);
        if (authTable) {
            authTable.clear().draw();
        }
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
        $('#btnUpdate, #btnDelete, #btnSaveAuth, #btnAddAuthRow, #btnDeleteAuthRow').prop('disabled', !enable).toggleClass('enabled', enable);
    }
});
