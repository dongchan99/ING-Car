document.addEventListener('DOMContentLoaded', function() {
    // 1. 비밀번호 입력창 정보 가져오기
    let elInputPassword = document.querySelector('#user_password'); // input#password
    // 2. 비밀번호 확인 입력창 정보 가져오기
    let elInputPasswordRetype = document.querySelector('#confirmPassword'); // input#password-retype
    // 3. 실패 메시지 정보 가져오기 (비밀번호 불일치)
    let elMismatchMessage = document.querySelector('.mismatch-message'); // div.mismatch-message.hide
    // 4. 실패 메시지 정보 가져오기 (글자수 제한 8~15)
    let elPasswordRangeMessage = document.querySelector('.password-range-message'); // div.strongPassword-message.hide
    // 5. 실패 메시지 정보 가져오기
    let elPasswordSpecialMessage = document.querySelector('.password-special-message');

    // 1. 이름 입력창 정보 가져오기
    let elInputName = document.querySelector('#user_name');
    // 2. 실패 메시지 정보 가져오기 (2글자 이상 / 한글 이외 입력)
    let elNotKorMessage = document.querySelector('.not-kor-message');

    // 1. 생년월일 입력창 정보 가져오기
    let elInputBirthdate = document.querySelector('#user_birth'); // input#password
    // 2. 실패 메시지 정보 가져오기 (성인이 아님)
    let elNotAdultMessage = document.querySelector('.not-adult-message'); // div.mismatch-message.hide
    // 3. 실패 메시지 정보 가져오기 (형식 불일치)
    let elFalseTypeMessage = document.querySelector('.false-type-message'); // div.strongPassword-message.hide

    // 1. 이메일 입력창 정보 가져오기
    let elInputEmail = document.querySelector('#user_email');
    // 2. 실패 메시지 정보 가져오기 (이메일 형식 불일치)
    let elNotEmailMessage = document.querySelector('.not-email-message');


    // 1. 핸드폰 번호 입력창 정보 가져오기
    let elInputPhone = document.querySelector('#user_phone');
    // 2. 핸드폰 번호 실패 메세지 가져오기 (핸드폰 번호 형식 불일치)
    let elNotPhoneMessage = document.querySelector('.not-phone-message');

    // 1. 전송 버튼 정보 가져오기
    let submitButton = document.querySelector('#submitBtn');

    function passwordSpecialMessage(str) {
        // 영문자와 숫자로만 이루어져 있는지 확인하는 정규표현식
        const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]*$/;

        // 정규표현식을 사용하여 조건을 만족하는지 확인하여 반환
        return regex.test(str);
    }

    function NotKor(str) {
        // 정규표현식을 사용하여 한글 이외의 문자가 있는지 확인
        const nonKoreanRegex = /[^\uAC00-\uD7AF\u1100-\u11FF\u3130-\u318F\uA960-\uA97F\uAC00-\uD7FF\uFFA0-\uFFDC]/;

        // 입력된 문자열에서 한글 이외의 문자를 모두 찾아냄
        const nonKoreanCharacters = str.match(nonKoreanRegex);

        // 만약 찾아낸 문자가 있거나, 입력된 문자열이 한글이여도 2글자 미만이면 false 반환
        if (nonKoreanCharacters || (str.length > 0 && str.length < 2)) {
            return false;
        } else {
            return true;
        }
    }

    function isMatch(password1, password2) {
        return password1 === password2;
    }

    function adult(birth) {
        if (birth < 20050100) {
            return true;
        } else {
            return false;
        }
    }

    function type(birth) {
        // 생년월일이 유효한지 확인하기 위한 정규표현식
        const regex = /^[0-9]{8}$/;

        // 정규표현식을 사용하여 생년월일이 유효한지 검사
        if (regex.test(birth)) {
            // 생년월일이 YYYYMMDD 형식에 맞다면
            const year = birth.substr(0, 4);
            const month = birth.substr(4, 2);
            const day = birth.substr(6, 2);

            // 유효한 날짜인지 검사
            const date = new Date(year, month - 1, day);
            const isValid = date.getFullYear() == year && date.getMonth() + 1 == month && date.getDate() == day;

            return isValid;
        } else {
            // 생년월일이 형식에 맞지 않는다면
            return false;
        }
    }

    // 이메일 유효성 검사 함수
    function emailValidChk(email) {
        const pattern = /^[A-Za-z0-9]+@[A-Za-z0-9.]+\.[A-Za-z]{2,}$/;
        return pattern.test(email);
    }

    elInputPassword.onkeyup = function() {
        // 값을 입력한 경우
        if (elInputPassword.value.length !== 0) {
            // 영문자와 숫자로만 이루어지지 않은 경우
            if (passwordSpecialMessage(elInputPassword.value) === false) {
                elPasswordRangeMessage.classList.add('hide');
                elPasswordSpecialMessage.classList.remove('hide');
            }
            // 글자 수가 8~15글자가 아닐 경우
            else if (elInputPassword.value.length < 8 || elInputPassword.value.length > 15) {
                elPasswordSpecialMessage.classList.add('hide');
                elPasswordRangeMessage.classList.remove('hide'); // 영어 또는 숫자만 가능합니다
            }
            // 조건을 모두 만족할 경우
            else {
                elPasswordRangeMessage.classList.add('hide');
                elPasswordSpecialMessage.classList.add('hide');
            }
        }
        // 값을 입력하지 않은 경우 (지웠을 때)
        // 모든 메시지를 가린다.
        else {
            elPasswordRangeMessage.classList.add('hide');
            elPasswordSpecialMessage.classList.add('hide');
        }
    }

    elInputPasswordRetype.onkeyup = function() {
        // 값을 입력한 경우
        if (elInputPasswordRetype.value.length !== 0) {
            if (isMatch(elInputPassword.value, elInputPasswordRetype.value)) {
                elMismatchMessage.classList.add('hide'); // 실패 메시지가 가려져야 함
            } else {
                elMismatchMessage.classList.remove('hide'); // 실패 메시지가 보여야 함
            }
        } else {
            elMismatchMessage.classList.add('hide'); // 실패 메시지가 가려져야 함
        }
    };

    elInputName.onkeyup = function() {
        // 값을 입력한 경우
        if (elInputName.value.length !== 0) {
            if (NotKor(elInputName.value)) {
                elNotKorMessage.classList.add('hide'); // 실패 메시지가 가려져야 함
            } else {
                elNotKorMessage.classList.remove('hide'); // 실패 메시지가 보여야 함
            }
        }
        // 값을 입력하지 않은 경우 (지웠을 때)
        // 모든 메시지를 가린다.
        else {
            elNotKorMessage.classList.add('hide');
        }
    };

    elInputBirthdate.onkeyup = function() {
        // 값을 입력한 경우
        if (elInputBirthdate.value.length !== 0) {
            // 생년월일 형식이 유효하지 않은 경우
            if (type(elInputBirthdate.value) === false) {
                elNotAdultMessage.classList.add('hide');
                elFalseTypeMessage.classList.remove('hide');
            }
            // 성인이 아닌 경우
            else if (adult(elInputBirthdate.value) === false) {
                elNotAdultMessage.classList.remove('hide');
                elFalseTypeMessage.classList.add('hide');
            }
            // 조건을 모두 만족할 경우
            else {
                elNotAdultMessage.classList.add('hide');
                elFalseTypeMessage.classList.add('hide');
            }
        }
        // 값을 입력하지 않은 경우 (지웠을 때)
        // 모든 메시지를 가린다.
        else {
            elNotAdultMessage.classList.remove('hide');
            elFalseTypeMessage.classList.add('hide');
        }
    }

    elInputEmail.onkeyup = function() {
        if (elInputEmail.value.length !== 0) {
            if (emailValidChk(elInputEmail.value)) {
                elNotEmailMessage.classList.add('hide');
            } else {
                elNotEmailMessage.classList.remove('hide');
            }
        } else {
            elNotEmailMessage.classList.add('hide');
        }
    }

    // 핸드폰 번호 유효성 검사
    elInputPhone.addEventListener('input', function(event) {
        let phoneNumber = event.target.value.replace(/\D/g, ''); // 숫자 이외의 문자는 제거합니다.

        // 010 다음에 하이픈을 추가합니다.
        if (phoneNumber.length >= 3 && phoneNumber.length < 7) {
            phoneNumber = phoneNumber.replace(/(\d{3})(\d{0,4})/, '$1-$2');
        }
        // 010-2222 다음에 하이픈을 추가합니다.
        else if (phoneNumber.length >= 7) {
            phoneNumber = phoneNumber.replace(/(\d{3})(\d{4})(\d{0,4})/, '$1-$2-$3');
        }

        // 번호가 완전한 형태인지 확인합니다.
        if (phoneNumber.length >= 13) { // 010-XXXX-XXXX의 형태
            // 번호가 010으로 시작하는지 확인합니다.
            if (!phoneNumber.startsWith('010')) {
                // 010으로 시작하지 않으면 오류 메시지를 출력합니다.
                elNotPhoneMessage.classList.remove('hide');
            } else {
                elNotPhoneMessage.classList.add('hide');
            }
        } else {
            elNotPhoneMessage.classList.remove('hide');
        }
        // 입력된 값이 없는 경우
        if (phoneNumber.length === 0) {
            // 오류 메시지를 숨깁니다.
            elNotPhoneMessage.classList.add('hide');
        }

        event.target.value = phoneNumber;
    });

    // 제출 버튼 클릭 이벤트 처리
    submitButton.addEventListener('click', function(event) {
        // 기본 제출 동작 방지
        event.preventDefault();

        // HTML5의 기본 폼 유효성 검사 수행
            if (!document.getElementById('updateUserForm').checkValidity()) {
                // 폼이 유효하지 않은 경우 경고 메시지 표시
                alert('비밀번호를 다시 확인해주세요.');
                return;
            }

        // 여기서 원하는 조건을 만족하면 페이지 이동
        // 예를 들어, 모든 조건을 만족할 때만 페이지 이동하도록 할 수 있습니다.
        if (
            elPasswordRangeMessage.classList.contains('hide') &&
            elPasswordSpecialMessage.classList.contains('hide') &&
            elMismatchMessage.classList.contains('hide') &&
            elNotKorMessage.classList.contains('hide') &&
            elNotAdultMessage.classList.contains('hide') &&
            elFalseTypeMessage.classList.contains('hide') &&
            elNotPhoneMessage.classList.contains('hide') &&
            elNotEmailMessage.classList.contains('hide')
        ) {
           // 모든 조건을 만족하면 Swal 확인 창과 Ajax 요청을 실행
                       Swal.fire({
                           title: '확인',
                           text: "정말로 정보를 수정하시겠습니까?",
                           icon: 'warning',
                           showCancelButton: true,
                           confirmButtonColor: '#3085d6',
                           cancelButtonColor: '#d33',
                           confirmButtonText: '수정',
                           cancelButtonText: '취소'
                       }).then((result) => {
                           if (result.isConfirmed) {
                               var formData = new FormData(document.getElementById('updateUserForm'));
                               $.ajax({
                                   url: document.getElementById('updateUserForm').action,
                                   type: 'POST',
                                   data: formData,
                                   processData: false,
                                   contentType: false,
                                   success: function(response) {
                                       Swal.fire(
                                           '성공!',
                                           '사용자 정보가 성공적으로 업데이트되었습니다.',
                                           'success'
                                       ).then((result) => {
                                           if (result.isConfirmed) {
                                               window.location.href = '/admin/users/details?user_id=' + $('#user_id').val();
                                           }
                                       });
                                   },
                                   error: function(xhr) {
                                       Swal.fire(
                                           '실패!',
                                           '업데이트를 완료하지 못했습니다. 다시 시도해주세요.',
                                           'error'
                                       );
                                   }
                               });
                           }
                       });
                   } else {
                       // 실패 메시지가 하나라도 보이면 경고 메시지 표시
                       alert('입력 항목을 다시 확인해주세요.');
                   }
               });
           });