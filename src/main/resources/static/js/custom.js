//스크롤로 차량검색 나오게 or 안 나오게
$(document).ready(function() {
    var $filterWrap = $('.filter-wrap');
    
    $filterWrap.hide(); //새로고침 시 부드럽게 표시, 숨김 방지


    var topLimit = $('.hero-wrap').outerHeight(); // 영웅 섹션의 높이를 가져옵니다.
    var bottomLimit = $('footer').offset().top - $(window).height(); // 푸터가 화면 하단에 보이기 시작하는 위치

    function adjustFilterVisibility() {
        var scrollTop = $(window).scrollTop();

        if (scrollTop > topLimit && scrollTop < bottomLimit) {
            if ($filterWrap.is(':hidden')) { // filter-wrap이 현재 숨겨져 있는 경우
                $filterWrap.fadeIn('slow'); // 부드럽게 표시
            }
        } else {
            if ($filterWrap.is(':visible')) { // filter-wrap이 현재 보이는 경우
                $filterWrap.fadeOut('slow'); // 부드럽게 숨김
            }
        }
    }

    $(window).scroll(adjustFilterVisibility); // 스크롤할 때 함수 호출
    adjustFilterVisibility(); // 초기 로드 시에도 함수 호출
});