
 //로그인 성공 화면 스크롤 컨트롤 함수
 window.onload = function () {
    // 페이지 로드 완료 후에 실행될 코드
    console.log("작동됨")
    // 이동할 섹션의 위치를 가져옴
    var targetSection = document.getElementById('targetSection');

    // 섹션이 존재하는 경우에만 스크롤 이동
    if (targetSection) {
        // 해당 섹션으로 스크롤 이동
        targetSection.scrollIntoView({ behavior: 'auto', block: 'center' });
    }
};

//모달 지정 함수
function openModal(modalId) {
    $('#' + modalId).modal('show');
}

// 비밀번호 찾기 모달이 열릴 때 호출되는 함수
$('#forgotPasswordModal').on('show.bs.modal', function (e) {
    $("#findBtn").off('click').on('click', function () {
        let find_id = $("#find_id").val();
        let find_email = $("#find_email").val(); // 여기서 인코딩을 제거
        console.log(find_id, find_email);
        $.ajax({
            type: "POST",
            url: "/check/findPw",
            data: {
                "user_id": find_id,
                "user_email": find_email
            },
            success: function (res) {
                if (res.match) {
                    $('#forgotPasswordModal').modal('hide');
                    Swal.fire({
                        title: "임시 비밀번호 전송 완료!",
                        text: "임시 비밀번호가 이메일로 전송되었습니다.",
                        icon: "success"
                    });
                } else {
                    $('#checkMsg').html('<p style="color:red">일치하는 정보가 없습니다. 유저 ID와 이메일을 다시 확인해주세요.</p>');
                }
            },
            error: function (xhr, status, error) {
                console.error("Error details:", xhr.responseText);
                $('#checkMsg').html('<p style="color:red">서버와의 통신 중 오류가 발생했습니다. 관리자에게 문의하세요.</p>');
            }
        });
    });
});



//$('#resetPasswordModal').on('show.bs.modal', function (e) {
//    // 생략
//    $("#resetBtn").off('click').on('click', function () {
//        let new_password = $("#new_password").val();
//        let confirm_password = $("#confirm_new_password").val();
//
//        if (new_password !== confirm_password) {
//            $('#passwordCheckMsg').html('<p style="color:red">비밀번호가 일치하지 않습니다.</p>');
//            return;
//        }
//
//        $.ajax({
//            type: "POST",
//            url: "/check/resetPassword",
//            data: {
//                "userId": $("#find_id").val(),
//                "newPassword": new_password
//            },
//            success: function (res) {
//                if (res['message'] === 'Password updated successfully') {
//                    $('#resetPasswordModal').modal('hide');
//                    Swal.fire({
//                        title: "비밀번호 재설정 완료!",
//                        text: "새 비밀번호가 성공적으로 설정되었습니다.",
//                        icon: "success"
//                    }).then(() => {
//                        window.location = "/loginPage";
//                    });
//                } else {
//                    $('#passwordCheckMsg').html('<p style="color:red">비밀번호 재설정에 실패했습니다.</p>');
//                }
//            },
//            error: function () {
//                $('#passwordCheckMsg').html('<p style="color:red">서버와의 통신 중 오류가 발생했습니다.</p>');
//            }
//        });
//    });
//});

$("#checkDuplicateBtn").on("click", function() {
    var user_id = $("#user_id").val();
    $.ajax({
        type: "GET",
        url: "/check/checkUserId",
        data: { user_id: user_id },
        success: function(response) {
            if (response.exists) {
                $("#SuccessMessage").addClass('hide')
                $("#FailMessage").removeClass('hide')
            } else {
                $("#SuccessMessage").removeClass('hide')
                $("#FailMessage").addClass('hide')
            }
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
});


// 회원가입 모달이 열릴 때 호출되는 함수
$('#SignupModal').on('show.bs.modal', function (e) {
    // 회원가입 모달이 열릴 때 모든 입력 필드를 비움
    $('#user_id').val('');
    $('#user_password').val('');
    $('#confirmPassword').val('');
    $('#user_name').val('');
    $('#user_birth').val('');
    $('#user_email').val('');
    $('#phone').val('');
    $('#region').val('');
    $('#male').prop('checked', false);
    $('#female').prop('checked', false);
    $('#agreement').prop('checked', false);


    // 1. 아이디 입력창 정보 가져오기
let elInputUsername = document.querySelector('#user_id'); // input#username
// 3. 실패 메시지 정보 가져오기 (글자수 제한 6~15글자)
let elFailureMessage = document.querySelector('.failure-message'); // div.failure-message.hide
// 4. 실패 메시지2 정보 가져오기 (영어 또는 숫자)
let elFailureMessageTwo = document.querySelector('.failure-message2'); // div.failure-message2.hide

// 1. 비밀번호 입력창 정보 가져오기
let elInputPassword = document.querySelector('#user_password'); // input#password
// 2. 비밀번호 확인 입력창 정보 가져오기
let elInputPasswordRetype = document.querySelector('#confirmPassword'); // input#password-retype
// 3. 실패 메시지 정보 가져오기 (비밀번호 불일치)
let elMismatchMessage = document.querySelector('.mismatch-message'); // div.mismatch-message.hide
// 4. 실패 메시지 정보 가져오기 (글자수 제한 8~15)
let elPasswordRangeMessage = document.querySelector('.password-range-message'); // div.strongPassword-message.hide
//5. 실패 메시지 정보 가져오기
let elPasswordSpecialMessage = document.querySelector('.password-special-message');

//1. 이름 입력창 정보 가져오기
let elInputName = document.querySelector('#user_name');
//2. 실패 메시지 정보 가져오기 (2글자 이상 / 한글 이외 입력)
let elNotKorMessage = document.querySelector('.not-kor-message');

// 1. 생년월일 입력창 정보 가져오기
let elInputBirthdate = document.querySelector('#user_birth'); // input#password
// 2. 실패 메시지 정보 가져오기 (성인이 아님)
let elNotAdultMessage = document.querySelector('.not-adult-message'); // div.mismatch-message.hide
// 3. 실패 메시지 정보 가져오기 (형식 불일치)
let elFalseTypeMessage = document.querySelector('.false-type-message'); // div.strongPassword-message.hide

//1. 이메일 입력창 정보 가져오기
let elInputEmail = document.querySelector('#user_email');
//2. 실패 메시지 정보 가져오기 (이메일 형식 불일치)
let elNotEmailMessage = document.querySelector('.not-email-message');
//3. 실패 메시지 정보 가져오기 (인증번호 불일치)
let elInputVerifyCode = document.querySelector('#verification_code');
let elNotMatchVerifyCode = document.querySelector('.not-matched-message');

//1. 핸드폰 번호 입력창 정보 가져오기
let elInputPhone = document.querySelector('#phone');
//2. 핸드폰 번호 실패 메세지 가져오기 (핸드폰 번호 형식 불일치)
let elNotPhoneMessage = document.querySelector('.not-phone-message');

//1. 지역 셀렉트 박스 정보 가져오기
let elInputRegion = document.querySelector('#region');
//2. 지역 셀렉트 박스 실패 메세지 가져오기(미선택)
let elNotCheckedRegionMessage = document.querySelector('.not-checked-region-message');

//1. 동의 체크박스 정보 가져오기
let elInputAgree = document.querySelector('#agree');
//2. 동의 체크박스 실패 메세지 가져오기(미선택)
let elNotCheckedAgreeMessage = document.querySelector('.not-checked-agree-message');

//1. 전송 버튼 정보 가져오기
let submitButton = document.querySelector('#submitBtn');

$('#emailSendBtn').click(function() {

            let user_email = document.querySelector('#user_email').value;
            let authenticationNumber = generateRandomNumber();
            let subject = "이메일 인증 번호입니다";
            let text = "귀하의 이메일 인증 번호는 " + authenticationNumber + "입니다.";

            console.log("user_email=",user_email);
            console.log("authenticationNumber=",authenticationNumber)




            $.ajax({
                type: "POST",
                url: '/sendCode',
                data: {
                        to: user_email,
                        subject: subject,
                        text: text
                },
                success: function(response) {
                    console.log("이메일 전송에 성공했습니다:", response);
                    if (response === 'verifyCode sended successfully.') {
                        alert('해당 이메일로 인증번호가 전송되었습니다.');
                        sessionStorage.setItem("authNumber", authenticationNumber.toString());
                        console.log("sessionStorage.getItem('authNumber')=",sessionStorage.getItem("authNumber"))

                    } else {
                        alert(response);
                    }
                },
                error: function(xhr, status, error) {
                    console.log("이메일 전송에 실패했습니다:");
                    alert('오류가 발생했습니다. 다시 시도해주세요.');
                }
            });

            function generateRandomNumber() {
                    const min = 100000;
                    const max = 999999;
                    return Math.floor(Math.random() * (max - min + 1)) + min;
                }

    });



function idLength(value) {
    return value.length >= 6 && value.length <= 15
  }

function onlyNumberAndEnglish(str) {
    return /^[A-Za-z0-9][A-Za-z0-9]*$/.test(str);
  }

  function passwordRangeMessage(str) {
    // 길이가 8 이상 15 이하인지 확인
    if (str.length >= 8 && str.length <= 15) {
        return true; // 조건을 만족하면 true 반환
    } else {
        return false; // 조건을 만족하지 않으면 false 반환
    }
}

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
  function isMatch (password1, password2) {
    return password1 === password2;
  }

  function adult(birth){
    if(birth<20050100){
        return true;
    }else{
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

 //이메일 유효성 검사 함수
 function emailValidChk(email) {
            const pattern = /^[A-Za-z0-9]+@[A-Za-z0-9.]+\.[A-Za-z]{2,}$/;
            return pattern.test(email);
        }

        //성별 라디오 버튼 유효성 검사 함수
        function genderValidCheck() {
            // 남성과 여성 중 하나가 선택되었는지 확인
            if (document.getElementById('male').checked || document.getElementById('female').checked) {
                // 선택된 경우 유효하므로 실패 메시지를 숨깁니다.
                document.querySelector('.not-checked-message').classList.add('hide');
            } else {
                // 선택되지 않은 경우 실패 메시지를 표시합니다.
                document.querySelector('.not-checked-message').classList.remove('hide');
            }
        }


elInputUsername.onkeyup = function () {
  // 값을 입력한 경우
  if (elInputUsername.value.length !== 0) {
    // 영어 또는 숫자 외의 값을 입력했을 경우
    if(onlyNumberAndEnglish(elInputUsername.value) === false) {
      elFailureMessage.classList.add('hide');
      elFailureMessageTwo.classList.remove('hide'); // 영어 또는 숫자만 가능합니다
    }
    // 글자 수가 4~12글자가 아닐 경우
    else if(idLength(elInputUsername.value) === false) {
      elFailureMessage.classList.remove('hide'); // 아이디는 4~12글자이어야 합니다
      elFailureMessageTwo.classList.add('hide'); // 실패 메시지2가 가려져야 함
    }
    // 조건을 모두 만족할 경우
    else if(idLength(elInputUsername.value) || onlyNumberAndEnglish(elInputUsername.value)) {
      elFailureMessage.classList.add('hide'); // 실패 메시지가 가려져야 함
      elFailureMessageTwo.classList.add('hide'); // 실패 메시지2가 가려져야 함
    }
  }
  // 값을 입력하지 않은 경우 (지웠을 때)
  // 모든 메시지를 가린다.
  else {
    elFailureMessage.classList.add('hide');
    elFailureMessageTwo.classList.add('hide');
  }
}

  elInputPassword.onkeyup = function () {
    // 값을 입력한 경우
    if (elInputPassword.value.length !== 0) {
        // 영문자와 숫자로만 이루어지지 않은 경우
        if (passwordSpecialMessage(elInputPassword.value) === false) {
            elPasswordRangeMessage.classList.add('hide');
            elPasswordSpecialMessage.classList.remove('hide');
        }
        // 글자 수가 8~15글자가 아닐 경우
        else if (passwordRangeMessage(elInputPassword.value) === false) {
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


  elInputPasswordRetype.onkeyup = function () {

    // console.log(elInputPasswordRetype.value);
    if (elInputPasswordRetype.value.length !== 0) {
      if(isMatch(elInputPassword.value, elInputPasswordRetype.value)) {
        elMismatchMessage.classList.add('hide'); // 실패 메시지가 가려져야 함
      }
      else {
        elMismatchMessage.classList.remove('hide'); // 실패 메시지가 보여야 함
      }
    }
    else {
      elMismatchMessage.classList.add('hide'); // 실패 메시지가 가려져야 함
    }
  };

  elInputName.onkeyup = function () {

// console.log(elInputPassword.value);
// 값을 입력한 경우
if (elInputName.value.length !== 0) {
  if(NotKor(elInputName.value)) {
    elNotKorMessage.classList.add('hide'); // 실패 메시지가 가려져야 함
  }
  else {
    elNotKorMessage.classList.remove('hide'); // 실패 메시지가 보여야 함
  }
}
// 값을 입력하지 않은 경우 (지웠을 때)
// 모든 메시지를 가린다.
else {
  elNotKorMessage.classList.add('hide');
}
};

elInputBirthdate.onkeyup = function () {
    // 값을 입력한 경우
    if (elInputBirthdate.value.length !== 0) {
        // 영문자와 숫자로만 이루어지지 않은 경우
        if (type(elInputBirthdate.value) === false) {
            elNotAdultMessage.classList.add('hide');
            elFalseTypeMessage.classList.remove('hide');
        }
        // 글자 수가 8~15글자가 아닐 경우
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

elInputEmail.onkeyup = function () {
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

        document.querySelectorAll('input[name="gender"]').forEach(function (radio) {
            radio.addEventListener('change', genderValidCheck);
        });



//핸드폰 번호 유효성 검사
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

//지역
elInputRegion.addEventListener('change', function() {
    // 선택된 옵션의 값(value)을 가져옴
    
    // 옵션이 선택되지 않았을 경우
    if (!elInputRegion.value) {
        // 오류 메시지를 표시
        elNotCheckedRegionMessage.classList.remove('hide');
    } else {
        // 옵션이 선택된 경우, 오류 메시지를 숨김
        elNotCheckedRegionMessage.classList.add('hide');
    }
});

elInputAgree.addEventListener('change', function() {
    // 동의가 이루어지지 않았을 경우
    if (!elInputAgree.checked) {
        // 오류 메시지를 표시
        elNotCheckedAgreeMessage.classList.remove('hide');
    } else {
        // 동의가 이루어진 경우, 오류 메시지를 숨김
        elNotCheckedAgreeMessage.classList.add('hide');
    }
});


// 제출 버튼 정보 가져오기


// 제출 버튼 클릭 이벤트 처리 
// 클릭 이벤트 핸들러 추가
submitButton.addEventListener('click', function(event) {
    // 기본 제출 동작 방지
    event.preventDefault();
    let code1 = document.querySelector('#verification_code').value; // 입력 필드에서 값을 가져옵니다.
    let code2 = sessionStorage.getItem("authNumber"); // 세션 스토리지에서 저장된 인증번호를 가져옵니다.

    console.log("code1=",code1)
    console.log("code2=",code2)

    //이메일 인증번호 확인
    if(code1===code2){
        elNotMatchVerifyCode.classList.add('hide');
    }
    else{
        elNotMatchVerifyCode.classList.remove('hide');

    }

    // 여기서 원하는 조건을 만족하면 페이지 이동
    // 예를 들어, 모든 조건을 만족할 때만 페이지 이동하도록 할 수 있습니다.
    if (
        $("#FailMessage").hasClass("hide")&&
        elFailureMessage.classList.contains('hide') &&
        elFailureMessageTwo.classList.contains('hide') &&
        elPasswordRangeMessage.classList.contains('hide') &&
        elPasswordSpecialMessage.classList.contains('hide') &&
        elMismatchMessage.classList.contains('hide') &&
        elNotKorMessage.classList.contains('hide') &&
        elNotAdultMessage.classList.contains('hide') &&
        elFalseTypeMessage.classList.contains('hide') &&
        elNotPhoneMessage.classList.contains('hide') &&
        elNotCheckedAgreeMessage.classList.contains('hide') &&
        elNotCheckedRegionMessage.classList.contains('hide') &&
        elNotEmailMessage.classList.contains('hide') &&
        elNotMatchVerifyCode.classList.contains('hide')
    ) {
        // 모든 조건을 만족하면 login-success.html로 이동
         document.getElementById('register_form').submit();
    } else {
        // 실패 메시지가 하나라도 보이면 경고 메시지 표시
        alert('입력 항목을 다시 확인해주세요.');

    }
});

});


// 버튼 클릭 시 페이지 URL을 복사하는 함수
function copyPageURL() {
    var url = window.location.href; // 현재 페이지의 URL을 가져옴
    navigator.clipboard.writeText(url) // 클립보드에 URL 복사
        .then(function () {
            alert('링크가 복사되었습니다');
        })
        .catch(function (error) {
            console.error('Failed to copy URL: ', error);
            alert('링크 복사에 실패했습니다');
        });
}
// 버튼에 클릭 이벤트 리스너 추가
document.getElementById('copyText').addEventListener('click', copyPageURL);


//prev, next 버튼 구현 함수
$(document).ready(function () {
    var currentImage = 1;
    let car_model = $("#car_model").text();
    console.log(car_model);
    $('#next_button').click(function () {
        currentImage = (currentImage % 5) + 1; // 이미지 번호를 1부터 5까지 순환하도록 설정
        $("#car_origin").css("background-image", "url('/images/"+car_model+"("+currentImage+").jpg')");
    });

    $('#prev_button').click(function () {
        currentImage = (currentImage - 2 + 5) % 5 + 1; // 이전 이미지 번호 계산
        $("#car_origin").css("background-image", "url('/images/"+car_model+"("+currentImage+").jpg')");
    });
});

//하단 이미지 hover시 이미지 교체 함수
function changeImageOnHover(imageElement, imageUrl) {
    let car_model = $("#car_model").text();
    imageElement.hover(
        function () {
            $("#car_origin").css("background-image", "url(" + imageUrl + ")");
        },
        function () {
            $("#car_origin").css("background-image", "url('/images/"+car_model+"(1).jpg')");
        }
    );
}
let car_model = $("#car_model").text();
changeImageOnHover($("#car_img1"), "'/images/"+car_model+"(2).jpg'");
changeImageOnHover($("#car_img2"), "'/images/"+car_model+"(3).jpg'");
changeImageOnHover($("#car_img3"), "'/images/"+car_model+"(4).jpg'");
changeImageOnHover($("#car_img4"), "'/images/"+car_model+"(5).jpg'");

//장바구니 토글 기능
//$('.cart-btn').click(function() {
//    var car_registration_id = parseInt($(this).attr('car_registration_id'));
//    console.log("AJAX 요청을 보냅니다. car_registration_id:", car_registration_id);
//    // Ajax 요청을 보냅니다.
//    $.ajax({
//        type: 'POST',
//        url: '/toggleCartItem',
//        data: { car_registration_id: car_registration_id },
//        success: function(response) {
//            console.log("요청이 성공했습니다:", response); // 성공 시 응답을 로그에 출력
//            if (response === 'added') {
//                alert('상품이 장바구니에 추가되었습니다.');
//            } else if (response === 'removed') {
//                alert('장바구니에서 상품이 삭제되었습니다.');
//            } else {
//                alert('오류가 발생했습니다.');
//            }
//        },
//        error: function(xhr, status, error) {
//
//            console.error("AJAX 요청 오류:");
//            console.error("상태 코드:", xhr.status);
//            console.error("오류 내용:", error);
//            console.error("서버 응답:", xhr.responseText);
//        }
//    });
//});

//장바구니 기능
$(document).ready(function() {
                var image1 = document.getElementById("image1");
                var image2 = document.getElementById("image2");

                var currentImageIndex = 1;

                // 이미지 클릭 시 이벤트 설정
                image1.addEventListener("click", function() {
                    toggleImages(image1, image2, currentImageIndex);
                    currentImageIndex = currentImageIndex === 1 ? 2 : 1;
                });

                image2.addEventListener("click", function() {
                    toggleImages(image1, image2, currentImageIndex);
                    currentImageIndex = currentImageIndex === 1 ? 2 : 1;
                });
            });

            function toggleImages(image1, image2, currentImageIndex) {
                var car_registration_id = parseInt($("#car_registration_id").text().trim(), 10);

                if (isNaN(car_registration_id)) {
                    console.error("Invalid car_registration_id");
                    return;
                }

                if (currentImageIndex === 1) {
                    image1.style.display = "none";
                    image2.style.display = "block";
                } else {
                    image1.style.display = "block";
                    image2.style.display = "none";
                }

                $.ajax({
                    type: "POST",
                    url: "/mypage/mypage_cart_input",
                    data: {
                        car_registration_id: car_registration_id
                    },
                    success: function(response) {
                        // 요청이 성공했을 때 실행되는 코드
                        if (response === 'added') {
                            alert('장바구니에 상품이 추가되었습니다.');
                        } else if (response === 'removed') {
                            alert('장바구니에서 상품이 삭제되었습니다.');
                        } else {
                            alert('오류가 발생했습니다.');
                        }
                    },
                    error: function(xhr, status, error) {
                        // 요청이 실패했을 때 실행되는 코드
                        console.error("리뷰 제출에 실패했습니다:", error);
                    }
                });
            }

//정수 가격 문자열 변환용
function formatKoreanCurrency(amount) {
    const units = ['', '만', '억', '조'];
    let result = '';
    let unitIndex = 0;
    let hasHigherUnit = false;

    while (amount > 0) {
        let part = amount % 10000;
        if (part > 0) {
            if (unitIndex == 0 && !hasHigherUnit) {
                // 천원 이하 단위 생략
            } else {
                result = part + units[unitIndex] + result;
            }
            hasHigherUnit = true;
        }
        amount = Math.floor(amount / 10000);
        unitIndex++;
    }

    return result;
}

$('#reserveBtn').click(function() {
        var car_registration_id = parseInt($(this).attr('car_registration_id'));

        const translog_price_int=document.getElementById('translog_price').value;
        let translog_price = formatKoreanCurrency(parseInt(translog_price_int, 10));

        console.log(translog_price);

        if (translog_price===''){

            alert('가격을 적어주세요');
            return;
        }

        // AJAX 요청을 보냅니다.
        $.ajax({
            type: 'POST', // POST 방식으로 요청합니다.
            url: '/reserveCar', // 요청을 보낼 URL을 지정합니다.
            data: { car_registration_id: car_registration_id , translog_price:translog_price}, // 요청 시 함께 보낼 데이터를 지정합니다. (예: 차량 등록 ID)
            success: function(response) {
            console.log("요청이 성공했습니다:", response); // 성공 시 응답을 로그에 출력
            if (response === 'added') {
                alert('차량이 예약되었습니다.');
            } else if (response === 'exist') {
                alert('이미 예약된 차량입니다.');
            } else {
                alert('오류가 발생했습니다.');
            }
        },
            error: function(xhr, status, error) {
                // 오류 시 실행할 코드를 작성합니다.
                alert('오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });















