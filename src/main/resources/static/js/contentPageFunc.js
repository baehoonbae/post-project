    // 전체 글
    $('#homeButton').click(function(event) {
        window.location.href = '/';
    });

    // 수정
    $('#updateButton').click(function(event) {
        // 로그인 상태 확인
        if (!isLoggedIn) {
            alert("권한이 없습니다.");
            // 로그인 페이지로 이동
            window.location.href = '/member/login';
            event.preventDefault();
        } else {
            if(sessionNickname === postNickname) {
                $.ajax({
                    url: '/post/' + postId + '/edit',
                    type: 'GET',
                    // 수정 로직 검증 완료 시
                    success: function(response) {
                        window.location.href = '/post/' + postId + '/edit';
                    },
                    error: function(xhr, status, error) {
                        alert('서버 오류가 발생했습니다.');
                    }
                });
            } else {
                alert("권한이 없습니다.");
                event.preventDefault();
            }
        }
    });

    // 삭제
    $('#deleteButton').click(function(event) {
        // 로그아웃 상태일 경우
        if (!isLoggedIn) {
            alert("권한이 없습니다.");
            // 로그인 페이지로 이동
            window.location.href = '/member/login';
            event.preventDefault();
        } else {
            // 로그인 상태일 경우
            var confirmDelete = confirm("정말 삭제하시겠습니까?");
            if (sessionNickname === postNickname && confirmDelete) {
                $.ajax({
                    url: '/post/' + postId + '/delete',
                    type: 'POST',
                    // 삭제 로직 검증 완료 시
                    success: function(response) {
                        alert('게시물이 성공적으로 삭제되었습니다.');
                        window.location.href = '/';
                    },
                    error: function(xhr, status, error) {
                        alert('서버 오류가 발생했습니다.');
                    }
                });
            } else if (sessionNickname === postNickname && !confirmDelete) {
                event.preventDefault();
            } else {
                alert("권한이 없습니다.");
                event.preventDefault();
            }
        }
    });

    // 글 쓰기
    $('#writeButton').click(function(event) {
        // 로그인 상태 확인
        if (!isLoggedIn) {
            alert("권한이 없습니다.");

            // 로그인 페이지로 이동
            location.href = '/member/login';
            event.preventDefault();
        } else {
            window.location.href = '/post/write';
        }
    });