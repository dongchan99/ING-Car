//차량 수정 페이지 이동
$(document).ready(function() {
    $('#updateBtn').on('click', function() {
        var car_registration_id = $(this).attr('car_registration_id');

        // 서버에 확인 요청 보내기
        $.ajax({
            type: 'GET',
            url: '/check_transLog',
            data: { car_registration_id: car_registration_id },
            success: function(response) {
                if (response === 'OK') {
                    // 수정 페이지로 이동
                    window.location.href = '/car_update?car_registration_id=' + car_registration_id;
                } else {
                    // 경고 메시지 표시
                    alert('거래된 상품이라 수정이 불가합니다.');
                }
            },
            error: function(xhr, status, error) {
                console.error('서버 확인 요청 실패:', status, error);
                alert('서버 확인 요청에 실패했습니다.');
            }
        });
    });
});

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

//수정
function updateCar(event) {
    event.preventDefault();  // 기본 폼 제출 동작을 방지

    var car_price = $('#car_price').val();
    var car_price2 = formatKoreanCurrency(parseInt(car_price, 10));
    var car_registration_id = $('#car_registration_id').val(); // car_registration_id 값을 가져옵니다.

    var formData = {
        car_registration_id: car_registration_id, // car_registration_id를 폼 데이터에 포함시킵니다.
        car_price: car_price,
        car_price2: car_price2 // 변환된 가격을 사용
    };

    $.ajax({
        type: 'POST',
        url: '/car_update',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            alert('차량 정보가 성공적으로 수정되었습니다.');
            window.location.href = '/car_detail?car_registration_id=' + car_registration_id; // 페이지 이동
        },
        error: function(xhr, status, error) {
            console.error('차량 정보 수정 실패:', status, error);
            alert('차량 정보 수정에 실패했습니다.');
        }
    });
}

//삭제하기
document.getElementById('deleteBtn').addEventListener('click', function() {
    var car_registration_id = this.getAttribute('car_registration_id');// car_registration_id 값을 가져옵니다.

    if (confirm('정말로 이 차량을 삭제하시겠습니까?')) {
        $.ajax({
            type: 'POST',
            url: '/car_delete',
            contentType: 'application/json',
            data: JSON.stringify({ car_registration_id: car_registration_id }),
            success: function(response) {
                alert('차량이 성공적으로 삭제되었습니다.');
                window.location.href = '/car_list';
            },
            error: function(xhr, status, error) {
                console.error('차량 삭제 실패:', status, error);
                alert('차량 삭제에 실패했습니다.');
            }
        });
    }
});