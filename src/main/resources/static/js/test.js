$('.star_rating > .star').click(function () {
  $(this).parent().children('span').removeClass('on');
  $(this).addClass('on').prevAll('span').addClass('on');
});

$('.comment-content').keyup(function (e) {
  var content = $(this).val();

  //글자수 세기
  if (content.length == 0 || content == '') {
    $('.comment_length').text('0');
  } else {
    $('.comment_length').text(content.length);
  }

  if (content.length > 500) {
    // 500자 부터는 타이핑 되지 않도록
    $(this).val($(this).val().substring(0, 200));
    // 500자 넘으면 알림창 뜨도록
  }
});

$(document).ready(function () {
  getCommentList();
});

// DB 연결하고 주석 풀기
// function getCommentList() {
//   var url = '/board/commentList.do?postno=' + $('#postno').val();
//   var id = '';

//   $.ajax({
//     url: '/board/loginCheck.do', // 서버 요청 주소
//     type: 'GET',
//     dataType: 'json',
//     success: function (data) {
//       id = data.loginId; // 서버에서 반환한 세션 값
//     },
//     error: function () {
//       alert('세션 값 가져오기 실패');
//     },
//   });

$.ajax({
  url: url,
  type: 'GET',
  dataType: 'json',
  success: function (result) {
    var comments = '';
    if (result.length < 1) {
      comments = '등록된 댓글이 없습니다.';
    } else {
      $(result).each(function () {
        if (this.depth == 2) {
          comments += '<div class="d-flex commentList">';
        }
        if (this.depth == 3) {
          comments += '<div class="d-flex commentList ms-5" >';
        }
        comments += '<div class="flex-shrink-0">';
        comments +=
          '<img class="rounded-circle" id="commenterImg" src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png" style="width: 50px;" />';
        comments += '</div>';
        comments += '<div class="ms-3">';
        comments +=
          '<input type="hidden"id="commentStep" class="commentStep" value="' +
          this.step +
          '">';
        comments +=
          '<input type="hidden"id="commentIn" class="commentNo" value="' +
          this.postno +
          '">';
        comments +=
          '<div id="commentNo" style="display: none;">' +
          this.postno +
          '</div>';
        comments +=
          '<div class="fw-bold" style="float: left;">' + this.id + '</div>';
        comments +=
          '<div class="mx-3 opacity-50" style="float: left;">' +
          this.wrtdate +
          '</div>';
        if (this.depth == 2) {
          comments +=
            '<button class="btn btn-outline-primary btn-sm border-0" style="font-size: 10px;" onclick="reCommentForm(event)">답글달기</button>';
        }
        if (id == this.id) {
          comments +=
            '<button class="btn btn-outline-success btn-sm mx-1" style="font-size: 10px; " onclick="modifyCommentForm(event)">수정</button>';
          comments +=
            '<button class="btn btn-outline-danger btn-sm" style=" font-size: 10px;" onclick="deleteComment(event)"><i class="bi bi-trash3"></i></button>';
        }
        comments += '<div class="comment mb-3">' + this.content + '</div>';
        comments += '</div></div>';

        // 댓글 수정 폼
        comments +=
          '<div class="modifyCForm mt-2 ms-5" style="display: none; width: 50%;">';
        comments +=
          '<div class=commentNo style="display: none;">' +
          this.postno +
          '</div>';
        comments +=
          '<input type="hidden" name="depth" id="hDepth" value=2><input type="hidden" id="hPgroup"  value="" " name="pgroup"><input type="hidden" id="hPostno" value="' +
          this.postno +
          '" name="postno">';
        comments +=
          '<textarea id="modifyarea" class="form-control" rows="1" name="content" placeholder=" ' +
          this.content +
          ' " required></textarea>';
        comments += '<div class="text-end mt-2">';
        comments +=
          '<button class="btn btn-outline-dark btn-sm" type="button" style="font-size: 10px;" onclick="modifyComment(event)"><i class="bi bi-send"></i></button>';
        comments +=
          '<button type="button" class="re-btn btn btn-secondary btn-sm" style="font-size: 10px;" onclick="cancelModifyC(event)"><i class="bi bi-x-lg"></i></button>';
        comments += '</div></div></div>';

        // 대댓글 작성 폼
        comments +=
          '<div class="reCommentForm mt-2 ms-5" style="display: none; width: 50%;">';
        comments +=
          '<input type="hidden"id="commentStep" class="commentStep" value="' +
          this.step +
          '">';
        comments += '<input type="hidden" name="reDepth" id="reDepth" value=3>';
        comments +=
          '<input type="hidden" name="id" id="hReStep" value=" ' + id + ' ">';
        if (id != null) {
          comments +=
            '<textarea id="reComment" name="comment" rows="1" class="form-control" placeholder="대댓글을 남겨주세요." required></textarea>';
          comments += '<div class="text-end mt-2">';
          comments +=
            '<button class="btn btn-outline-dark btn-sm" type="button" style="font-size: 10px;" onclick="insertReComment(event)"><i class="bi bi-send"></i></button>';
          comments += '</div>';
        }
        if (id == null) {
          comments +=
            '<textarea id="reComment" name="content" rows="1" class="form-control mb-3" placeholder="로그인 후 이용가능합니다." readonly></textarea>';
        }
        comments += '</div></div>';
      });
    }
    $('#commentList').html(comments);
  },
});

$.ajax({
  url: '/board/commentCnt.do?postno=' + $('#postno').val(),
  type: 'GET',
  dataType: 'json',
  success: function (commentCnt) {
    var cnt =
      '<span><i class="bi bi-chat-dots"></i></span> <span>' +
      commentCnt +
      '</span>';
    $('#commentCnt').html(cnt);
  },
});

// 댓글 작성 ajax
function insertComment(event) {
  if (!$('#comment').val()) {
    alert('내용을 입력하세요.');
  } else {
    var bDto = {
      pgroup: $('#pgroup').val(),
      depth: $('#depth').val(),
      content: $('#comment').val(),
    };
    $.ajax({
      url: '/board/addC.do',
      data: bDto,
      type: 'POST',
      success: function (result) {
        getCommentList();
        $('#comment').val('');
      },
    });
  }
}

// 대댓글 작성창
function reCommentForm(event) {
  var reCommentForm = event.target.closest('.commentList').nextElementSibling;
  while (reCommentForm) {
    if (reCommentForm.classList.contains('reCommentForm')) {
      break;
    }
    reCommentForm = reCommentForm.nextElementSibling;
  }
  if (reCommentForm) {
    reCommentForm.style.display =
      reCommentForm.style.display === 'none' ? 'block' : 'none';
  }
}

// 대댓글 작성 ajax
function insertReComment(event) {
  var commentStep = parseInt(
    $(event.target).closest('.reCommentForm').find('#commentStep').val()
  );

  /* 		alert("그룹" + $('#pgroup').val())
  alert("스텝" + commentStep)
  alert("뎁스" + $('#reDepth').val())
  alert("내용" + $(event.target).closest('.reCommentForm').find('#reComment').val())		
*/
  var bDto = {
    pgroup: $('#pgroup').val(),
    step: commentStep,
    depth: $('#reDepth').val(),
    content: $(event.target).closest('.reCommentForm').find('#reComment').val(),
  };

  $.ajax({
    url: '/board/addC.do',
    data: bDto,
    type: 'POST',
    success: function (result) {
      getCommentList();
      $('#reComment').val('');
    },
  });
}

// 댓글 삭제
function deleteComment(event) {
  var postno = $(event.target).closest('.ms-3').find('#commentNo').text();
  var url = '/board/ajaxdelete.do?postno=' + postno;

  $.ajax({
    url: url,
    type: 'POST',
    success: function (result) {
      getCommentList();
      $('#comment').val('');
    },
    error: function () {
      alert('댓글 삭제 실패');
    },
  });
}
// 댓글 수정
function modifyComment(event) {
  var postno = $(event.target)
    .closest('.modifyCForm')
    .find('.commentNo')
    .text();
  var content = $(event.target)
    .closest('.modifyCForm')
    .find('#modifyarea')
    .text();
  var bDto = {
    depth: $(event.target).closest('.modifyCForm').find('#hDepth').val(),
    pgroup: $(event.target).closest('.modifyCForm').find('#hPgroup').val(),
    content: $(event.target).closest('.modifyCForm').find('#modifyarea').val(),
    postno: $(event.target).closest('.modifyCForm').find('#hPostno').val(),
  };
  $.ajax({
    url: '/board/ajaxupdate.do?postno=' + postno,
    type: 'POST',
    data: bDto,
    success: function (result) {
      getCommentList();
      $('#modifyarea').val('');
    },
    error: function () {
      alert('댓글 수정 실패');
    },
  });
}

// 댓글 수정 창
function modifyCommentForm(event) {
  var modifyCForm = event.target.closest('.commentList').nextElementSibling;
  while (modifyCForm) {
    if (modifyCForm.classList.contains('modifyCForm')) {
      break;
    }
    modifyCForm = modifyCForm.nextElementSibling;
  }
  if (modifyCForm) {
    modifyCForm.style.display =
      modifyCForm.style.display === 'none' ? 'block' : 'none';
  }
}
function cancelModifyC(event) {
  var modifyCForm = event.target.closest('.modifyCForm');
  modifyCForm.style.display = 'none';
}
