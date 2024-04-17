$(document).ready(function() {
    $('#loginForm').submit(function(event) {
        event.preventDefault();

        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            url: '/member/login',
            type: 'post',
            data: {
                username: $('#username').val(),
                password: $('#password').val(),
            },
            success: function(response) {
                window.location.href = response;
            },
            error: function(xhr, status, error) {
                alert('아이디 혹은 비밀번호를 확인해 주세요.');
            }
        });
    });
});