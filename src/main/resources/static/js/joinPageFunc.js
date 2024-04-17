$(document).ready(function() {
    var prevUrl = sessionStorage.getItem('prevUrl');

    // 회원가입 활성화 상태
    var joinActivation = true;

    // 회원가입 버튼 요소
    var joinButton = document.getElementById('joinButton');

    // 회원가입 버튼의 클릭 이벤트 핸들러 추가
    $('form').on('submit', function(event) {
        event.preventDefault();

        // 가입 완료 버튼이 활성화되어 있을 경우
        if (joinActivation) {
            var username = $('#username').val();
            var password = $('#password').val();
            var nickname = $('#nickname').val();

            $.ajax({
                url: '/member/join',
                type: 'post',
                data: {
                    username: username,
                    password: password,
                    nickname: nickname
                },
                success: function(response) {
                    window.location.href = response;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert('Error: ' + textStatus + ' ' + errorThrown);
                }
            });
        }
    });

    // joinActivation 업데이트 및 회원가입 버튼 활성화/비활성화 함수
    function updateJoinActivation() {
        // 각 error 가 출력되었는지 확인
        var usernameError = document.getElementById('username_error');
        var nicknameError = document.getElementById('nickname_error');
        var passwordError = document.getElementById('password_error');
        var confirmPasswordError = document.getElementById('confirmPassword_error');

        // 에러 메시지가 출력되었는지 확인하고 joinActivation을 업데이트함
        if (usernameError.innerHTML || nicknameError.innerHTML || passwordError.innerHTML || confirmPasswordError.innerHTML) {
            joinActivation = false; // 한 개라도 에러 메시지가 출력되었다면 회원가입 비활성화
        } else {
            joinActivation = true; // 에러 메시지가 없다면 회원가입 활성화
        }

        // 회원가입 버튼을 활성화 또는 비활성화
        if (joinActivation) {
            joinButton.removeAttribute('disabled'); // 활성화
        } else {
            joinButton.setAttribute('disabled', 'disabled'); // 비활성화
        }
    }

    // 아이디 중복 및 입력 확인
    $('#username').blur(function(){
        var username = $(this).val();
        if (!username) {
            $('#username_error').html('* 아이디: 필수 정보입니다.');
        } else {
            username = $(this).val();
            xhr = $.ajax({
                url: '/member/check-username',
                type: 'post',
                data: {username: username},
                success: function(usernameResponse){
                     if (usernameResponse) {
                         $('#username_error').html(usernameResponse); // 메시지 출력
                     } else {
                         $('#username_error').html(''); // 메시지 비우기
                     }
                     // 에러 메시지가 변경될 때마다 joinActivation 업데이트
                     updateJoinActivation();
                }
            });
        }
    });

    // 패스워드 입력 확인
    $('#password').blur(function(){
        var password = $(this).val();
        if (!password) {
            $('#password_error').html('* 비밀번호: 필수 정보입니다.');
        } else{
            $('#password_error').html('');
        }
        // 에러 메시지가 변경될 때마다 joinActivation 업데이트
        updateJoinActivation();
    });

    // 비밀번호 일치 여부 확인
    $('#confirmPassword').blur(function(){
        var password1 = $('#password').val();
        var password2 = $(this).val();
        if (password1 !== password2) {
            $('#confirmPassword_error').html('* 비밀번호와 일치하지 않습니다.');
        } else {
            $('#confirmPassword_error').html('');
        }
        // 에러 메시지가 변경될 때마다 joinActivation 업데이트
        updateJoinActivation();
    });

    // 닉네임 중복 및 입력 확인
    $('#nickname').blur(function() {
        var nickname = $('#nickname').val();
        if (!nickname) {
            $('#nickname_error').html('* 닉네임: 필수 정보입니다.');
        } else {
            nickname = $('#nickname').val();
            xhr = $.ajax({
                type: 'post',
                url: '/member/check-nickname',
                data: {
                    nickname: nickname
                },
                success: function(nicknameResponse){
                    if (nicknameResponse) {
                        $('#nickname_error').html(nicknameResponse); // 메시지 출력
                    } else {
                        $('#nickname_error').html(''); // 메시지 비우기
                    }
                    // 에러 메시지가 변경될 때마다 joinActivation 업데이트
                    updateJoinActivation();
                }
            });
        }
    });

});
