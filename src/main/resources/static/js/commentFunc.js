    // 댓글 삭제
    $(document).on('click', '.delete', function(event) {
        var commentId = $(this).attr('id').split('-')[1];
        $.ajax({
            url: '/post/' + postId + '/comment/' + commentId,
            type: 'POST',
            success: function(response) {
                alert('댓글이 성공적으로 삭제되었습니다.');
                location.reload();
            },
            error: function(xhr, status, error) {
                alert('서버 오류가 발생했습니다.');
            }
        });
        event.preventDefault();
    });

    $(document).ready(function() {
        // 댓글 입력 창이나 등록 버튼을 클릭했을 때의 이벤트 핸들러
        $('#commentContent, #commentForm button[type="submit"]').click(function(event) {
            // 로그인 상태 확인
            if (!isLoggedIn) {
                alert("권한이 없습니다.");

                // 로그인 페이지로 이동
                location.href = '/member/login';
                event.preventDefault();
            }
        });
    });

    $(document).on('click', '.reply', function(event) {
        var commentId = $(this).attr('id').split('-')[1];
        var replyForm = $('#replyForm-' + commentId);
        replyForm.toggle();
        event.preventDefault();
    });