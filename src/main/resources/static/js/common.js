function loadMenu(currentPage) {
    $('.menu-container').load('menu.html', function () {
        fetch('/api/auth/user')
            .then(response => {
                if (!response.ok) {
                    // Not authenticated, redirect to login
                    window.location.href = '/login.html';
                    return;
                }
                return response.json();
            })
            .then(user => {
                if (user) {
                    $('#greeting').text(`${user.user.userName}님`);
                    const authority = user.user.authority;
                    if (authority !== '관리자') {
                        $('[data-page="user.html"]').hide();
                        $('[data-page="buseo.html"]').hide();
                    }
                }
            })
            .catch(error => {
                console.error('Error fetching user data:', error);
                window.location.href = '/login.html';
            });

        const menuBar = document.getElementById('menuBar');
        if (menuBar) {
            const currentPageMenuItem = menuBar.querySelector(`[data-page="${currentPage}"]`);
            if (currentPageMenuItem) {
                menuBar.querySelectorAll('.menu-item').forEach(item => item.classList.remove('active'));
                currentPageMenuItem.classList.add('active');
            }

            menuBar.addEventListener('click', (event) => {
                const target = event.target.closest('.menu-item');
                const pageToLoad = target.getAttribute('data-page');
                if (pageToLoad) {
                    location.href = pageToLoad;
                }
            });
        }
    });
}

function getTableHeight(size) {
        // 예: 화면 높이에서 200px 여유 공간 빼기
        return $(window).height() - size + "px";
    }
