// 모달로 진행하기 전에 폼 입력을 검증하는 함수
function validateForm() {
    // 입력 요소와 해당 오류 메시지 요소를 가져옴
    var car_id = document.getElementById('car_id');
    var car_year = document.getElementById('year');
    var car_mileage = document.getElementById('mileage');
    var car_price = document.getElementById('price');
    var car_description = document.getElementById('description');

    var errorCar_id = document.getElementById('error-car_id');
    var errorCar_year = document.getElementById('error-year');
    var errorCar_mileage = document.getElementById('error-mileage');
    var errorCar_price = document.getElementById('error-price');
    var errorCar_description = document.getElementById('error-description');

    // 초기에는 모든 오류 메시지를 숨김
    errorCar_id.style.display = 'none';
    errorCar_year.style.display = 'none';
    errorCar_mileage.style.display = 'none';
    errorCar_price.style.display = 'none';
    errorCar_description.style.display = 'none';

    // 검증 상태를 나타내는 플래그 초기화
    var isValid = true;

    // 차량 ID 검증
    if (car_id.value === '') {
        errorCar_id.style.display = 'block';
        isValid = false;
    }

    // 연도 검증 (적절한 범위 내인지 확인)
    var yearValue = parseInt(car_year.value);
    if (car_year.value === '' || yearValue < 1900 || yearValue > 2024) {
        errorCar_year.style.display = 'block';
        isValid = false;
    }

    // 주행거리 검증 (양수 및 빈 값이 아님)
    var mileageValue = parseInt(car_mileage.value);
    if (car_mileage.value === '' || mileageValue < 0) {
        errorCar_mileage.style.display = 'block';
        isValid = false;
    }

    // 가격 검증 (양수이며 정수)
    var priceValue = parseInt(car_price.value);
    if (priceValue <= 0 || isNaN(priceValue) || priceValue >= 10000000000) {
        errorCar_price.style.display = 'block';
        isValid = false;
    }

    // 설명 검증 (빈 값이 아니며 900자 이내)
    if (car_description.value.length > 900) {
        errorCar_description.style.display = 'block';
        isValid = false;
    }

    // 검증에 실패하면 진행하지 않음
    if (!isValid) {
        return false;
    }

    // 검증이 통과되면 수동으로 모달을 실행
    $('#confirmModal').modal('show');
}

// 모달이 표시될 때마다 이벤트 핸들러를 제거하고 다시 등록하는 대신, 모달이 처음 로드될 때 한 번만 이벤트 핸들러를 등록
$(document).ready(function () {
    $('#confirmModal').on('click', '.btn-primary-2', function () {
        submitForm();
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

function submitForm() {
    // 이벤트 핸들러가 중복으로 등록되는 것을 방지하기 위해 버튼을 비활성화
    $('.btn-primary-2').prop('disabled', true);

    // 가격 변환
    var rawPrice = $('#price').val();
    var formattedPrice = formatKoreanCurrency(parseInt(rawPrice, 10));

    var formData = {
        car_id: $('#car_id').val(),
        car_year: $('#year').val(),
        car_mileage: $('#mileage').val(),
        car_brand: $('#brand').val(),
        car_model: $('#model').val(),
        car_color: $('#model-color').val(),
        car_fuel_type: $('#fuel_type').val(),
        car_price: $('#price').val(),
        car_price2: formattedPrice,  // 변환된 가격을 사용
        car_accident_history: $('#accident_history').val(),
        car_description: $('#description').val(),
        user_id:"hk"
    };

    $.ajax({
        type: "POST",
        url: "/api/cars/register",
        contentType: "application/json",
        data: JSON.stringify(formData),
        success: function(response) {
            alert("차량이 성공적으로 등록되었습니다.");
            $('#confirmModal').modal('hide');
            // 버튼을 다시 활성화
            $('.btn-primary-2').prop('disabled', false);
        },
        error: function(xhr, status, error) {
            alert("제출 실패: " + xhr.responseText);
            // 버튼을 다시 활성화
            $('.btn-primary-2').prop('disabled', false);
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    updateModels(); // 초기 설정을 위해 호출
});

function updateModels() {
    var brand = document.getElementById('brand').value;
    var modelSelect = document.getElementById('model');
    modelSelect.innerHTML = ''; // 기존 옵션 제거

    // 브랜드와 모델에 대한 데이터
    var models = {
        '아우디': ['R8', 'RSE-tronGT'],
        '벤츠': ['GT43AMG', 'SL63AMG'],
        '페라리': ['F8', 'Roma'],
        '람보르기니': ['Huracan', 'Urus'],
        '포르쉐': ['911', 'BoxsterGTS'],
        '기타': ['모델1', '모델2', '모델3']
    };

    // 선택한 브랜드의 모델을 추가
    if (models[brand]) {
        models[brand].forEach(function(model) {
            var option = new Option(model, model);
            modelSelect.options.add(option);
        });
    }

    // 모델이 추가된 후 첫 번째 모델을 선택
    if (modelSelect.options.length > 0) {
        modelSelect.selectedIndex = 0;
    }

    // 첫 번째 모델에 대한 색상 업데이트
    updateColors(); // 모델이 업데이트된 후 호출
}

document.getElementById('model').addEventListener('change', updateColors);

function updateColors() {
    var model = document.getElementById('model').value;
    var colorSelect = document.getElementById('model-color');
    colorSelect.innerHTML = ''; // 기존 옵션 제거

    // 모델과 색상에 대한 데이터
    var colors = {
        'R8': ['흰색', '기타'],
        'RSE-tronGT': ['검정색', '기타'],
        'GT43AMG': ['노란색', '기타'],
        'SL63AMG': ['빨간색', '기타'],
        'F8': ['빨간색', '기타'],
        'Roma': ['검정색', '기타'],
        'Huracan': ['검정색', '기타'],
        'Urus': ['노란색', '기타'],
        '911': ['흰색', '기타'],
        'BoxsterGTS': ['노란색', '기타'],
        '모델1': ['검정색', '흰색', '노란색'],
        '모델2': ['검정색', '파란색', '녹색'],
        '모델3': ['흰색', '파란색', '분홍색']
    };

    // 선택한 모델의 색상을 추가
    if (colors[model]) {
        colors[model].forEach(function(color) {
            var option = new Option(color, color);
            colorSelect.options.add(option);
        });
    }
}