// CSRF 토큰을 가져오는 함수
function getCookie(name) {
  let cookieValue = null;
  if (document.cookie && document.cookie !== '') {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i].trim();
      if (cookie.substring(0, name.length + 1) === (name + '=')) {
        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
        break;
      }
    }
  }
  return cookieValue;
}

const csrftoken = getCookie('csrftoken');

// 후기 작성 화면 내 별점
$('#review_write_fir > .star_rating > .star').click(function () {
  $(this).parent().children('span').removeClass('on');
  $(this).addClass('on').prevAll('span').addClass('on');
  const boardStar = $(this).attr("data-value");
  console.log("후기 작성 화면 내 별점:", boardStar);

  // 클릭한 별점의 값을 submitReview 함수 내에서 사용할 수 있도록 설정
  $("#submitReviewBtn").data("boardStar", boardStar);
});

// 후기 수정 화면 내 별점
$('#review_re > .star_rating > .star').click(function () {
  $(this).parent().children('span').removeClass('on');
  $(this).addClass('on').prevAll('span').addClass('on');
  const boardStar = $(this).attr("data-value");
  console.log("후기 수정 화면 내 별점:", boardStar);
  // 클릭한 별점의 값을 submitReview 함수 내에서 사용할 수 있도록 설정
  $("#submitReviewBtn").data("boardStar", boardStar);
});

// 별점과 리뷰를 서버로 전송하는 함수
function submitReview() {
  // 선택된 별점 가져오기
  const boardStar = $("#submitReviewBtn").data("boardStar");
  console.log("submitReview 별점:" + boardStar);

  // 리뷰 제목과 내용 가져오기
  const reviewSubject = $("#reviewheader_area").val();
  const reviewBody = $("#review_txt").val();
  const log_id = $("#log_id").val();
  console.log("*** 제목 = " + reviewSubject);
  console.log("*** 내용 = " + reviewBody);

  // 유효성 검사
  if (!reviewSubject) {
    alert("제목을 입력하십시오.");
    $("#reviewheader_area").focus();
    return false;
  }
  if (!reviewBody) {
    alert("후기 내용을 입력하십시오.");
    $("#review_txt").focus();
    return false;
  }
  if (!boardStar) {
    alert("별점을 선택하십시오.");
    return false;
  }

  // AJAX 요청 보내기
  $.ajax({
    type: "POST",
    url: "/submit_review",

    data: {
      reviewSubject: reviewSubject,
      reviewBody: reviewBody,
      boardStar: boardStar,
      log_id: log_id
    },
    success: function (response) {
      // 요청이 성공했을 때 실행되는 코드
      alert("후기 등록이 완료 되었습니다.");
      // review_main 페이지로 리디렉션
      window.location.href = "/review_main";
    },
    error: function (xhr, status, error) {
      // 요청이 실패했을 때 실행되는 코드
      console.error("리뷰 제출에 실패했습니다:", error);
    }
  });
}

// 제목, 내용 클릭시 조회수 증가
function handleReviewClick(logId, boardSubject) {
  window.location.href = '/review_detail?log_id=' + logId + '&board_subject=' + boardSubject;
}

// 좋아요 상태 확인
function toggleButtonState(logId) {
  $.ajax({
    url: '/isLiked',
    type: 'GET',
    data: {
      log_id: logId
    },
    success: function(response) {
      let button = document.querySelector(`button[onclick*='${logId}']`);
      if (response.isLiked) {
        button.classList.add("on");
      } else {
        button.classList.remove("on");
      }
    },
    error: function(error) {
      console.error('Error checking like state:', error);
    }
  });
}

// 좋아요 클릭
function updateLike(logId, increment) {
  console.log(`updateLike: logId=${logId}, increment=${increment}`);
  $.ajax({
    url: '/updateLike',
    type: 'POST',

    data: {
      log_id: logId,
      increment: increment
    },
    success: function(response) {
      console.log('Like update response:', response);
      // 좋아요 수 업데이트
      document.getElementById('likeCount-' + logId).innerText = response.newLikeCount;
    },
    error: function(error) {
      console.error('Error updating like count:', error);
    }
  });
}

document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('button.like-button').forEach(button => {
    let logId = button.getAttribute('onclick').match(/toggleLike\(this, (\d+)\)/)[1];
    toggleButtonState(logId);
  });
});

// 댓글 가져오기
document.addEventListener('DOMContentLoaded', function() {
  const log_id = document.getElementById('logId').value;
  fetchComments(log_id);
});

function fetchComments(log_id) {
  $.ajax({
    url: '/comments',
    type: 'GET',
    data: {
      log_id: log_id
    },
    success: function(response) {
      const commentsList = document.getElementById('comments-list');
      commentsList.innerHTML = '';
      response.forEach(comment => {
        const commentElement = createCommentElement(comment);
        commentsList.appendChild(commentElement);
        fetchReplies(comment.comment_id);
      });
    },
    error: function(error) {
      console.error('Error fetching comments:', error);
    }
  });
}

function fetchReplies(parent_id) {
  $.ajax({
    url: '/comments/replies',
    type: 'GET',
    data: {
      parent_id: parent_id
    },
    success: function(response) {
      response.forEach(reply => {
        const replyElement = createCommentElement(reply, true);
        document.getElementById(`comment-body-${parent_id}`).appendChild(replyElement);
      });
    },
    error: function(error) {
      console.error('Error fetching replies:', error);
    }
  });
}

function createCommentElement(comment, isReply = false) {
  const div = document.createElement('div');
  div.className = isReply ? 'media mt-4' : 'media';
  div.id = `comment-${comment.comment_id}`;

  // 스타일을 개별적으로 설정
  if (isReply) {
    div.style.setProperty('margin-top', '5px', 'important');
  } else {
    div.style.paddingTop = '5px';
  }

  const formattedDate = new Date(comment.comment_date).toLocaleString(); // 날짜 형식 포맷팅

  div.innerHTML = `
        <img class="mr-3 rounded-circle" src="/images/icon_car_role.png" width="40">
        <div class="media-body" id="comment-body-${comment.comment_id}">
            <div class="row">
                <div class="col-8 d-flex">
                    <h6 style="margin-right: 10px;">${comment.user_id}</h6>
                    <span class="write_date">${formattedDate}</span>
                </div>
                <div class="col-4">
                    <div class="pull-right reply">
                        ${comment.child_id === 0 && comment.is_deleted === 0 ? `<a data-toggle="collapse" href="#recomment-${comment.comment_id}" aria-expanded="false" aria-controls="recomment-${comment.comment_id}" onclick='toggleReplyForm(${comment.comment_id})'><span><i class="fa fa-reply"></i> reply</span></a>` : ''}
                        ${comment.user_id === currentUserId && comment.is_deleted === 0 ? `
                        <input data-idx="${comment.comment_id}" src="/images/pencil_line_icon.png" type="image" class="edit" value="수정" onclick="editComment(${comment.comment_id}, ${isReply})">
                        <input data-idx="${comment.comment_id}" src="/images/trash_outline_icon.png" type="image" class="delete" value="삭제" onclick="deleteComment(${comment.comment_id})">` : ''}
                    </div>
                </div>
            </div>
            <div class="user_review_comment">${comment.is_deleted === 1 ? '이미 삭제된 댓글입니다.' : comment.comment_content}</div>
            ${comment.child_id === 0 ? `
            <div class="bg-light p-2 collapse" id="recomment-${comment.comment_id}" style="display: none;">
                <div class="d-flex flex-row align-items-start" style="padding-left:60px">
                    <img class="rounded-circle" src="/images/icon_car_role.png" width="40">
                    <textarea id="reComment-${comment.comment_id}" class="form-control ml-1 shadow-none textarea" placeholder="대댓글을 남겨주세요." style="font-size: 14px; width:800px" data-comment-type="reply" required></textarea>
                </div>
                <div class="mt-2 text-right"><button class="btn btn-primary btn-sm shadow-none" type="button" onclick='addComment(${comment.comment_id}, 1)'>Post comment</button></div>
            </div>
            ` : ''}
            <div class="collapse mt-2" id="editCommentForm-${comment.comment_id}"></div>
        </div>
    `;
  return div;
}

function toggleReplyForm(comment_id) {
  const replyForm = document.getElementById(`recomment-${comment_id}`);
  if (replyForm.style.display === 'none') {
    replyForm.style.display = 'block';
  } else {
    replyForm.style.display = 'none';
  }
}

function addComment(parent_id, child_id = 0) {
  const log_id = document.getElementById('logId').value;
  let content;
  let commentType;

  if (parent_id) {
    const replyTextarea = document.getElementById(`reComment-${parent_id}`);
    content = replyTextarea.value;
    commentType = replyTextarea.getAttribute('data-comment-type');
  } else {
    const commentTextarea = document.getElementById('commentwritearea');
    content = commentTextarea.value;
    commentType = commentTextarea.getAttribute('data-comment-type');
  }

  $.ajax({
    url: '/comments/add',
    type: 'POST',

    data: {
      log_id: log_id,
      comment_content: content,
      parent_id: commentType === 'reply' ? parent_id : 0,
      child_id: commentType === 'reply' ? 1 : 0
    },
    success: function(response) {
      const newComment = response;
      const commentElement = createCommentElement(newComment, !!parent_id);
      if (parent_id) {
        document.getElementById(`comment-body-${parent_id}`).appendChild(commentElement);
        document.getElementById(`reComment-${parent_id}`).value = ''; // 대댓글 textarea 초기화
        toggleReplyForm(parent_id); // 대댓글 작성 창 접기
      } else {
        document.getElementById('comments-list').appendChild(commentElement); // 댓글 textarea 초기화
        document.getElementById('commentwritearea').value = ''; // 댓글 textarea 초기화
      }
      location.reload();
    },
    error: function(error) {
      console.error('Error adding comment:', error);
    }
  });
}

function editComment(comment_id, isReply) {
  const commentBody = document.getElementById(`comment-body-${comment_id}`);
  const editForm = document.getElementById(`editCommentForm-${comment_id}`);
  console.log("commentbody = " + commentBody);
  console.log("editForm = " + editForm);

  // 기존 내용 가져오기
  const currentContent = commentBody.querySelector('.user_review_comment').innerText;

  console.log("currentContent = " + currentContent);

  editForm.innerHTML = `
    <div class="d-flex flex-row align-items-start">
      <textarea id="editComment-${comment_id}" class="form-control ml-1 shadow-none textarea" style="font-size: 14px; width:800px" required>${currentContent}</textarea>
    </div>
    <div class="mt-2 text-right">
      <button class="btn btn-primary btn-sm shadow-none" type="button" onclick="updateComment(${comment_id}, ${isReply})">Update comment</button>
      <button class="btn btn-secondary btn-sm shadow-none" type="button" onclick="cancelEdit(${comment_id})">Cancel</button>
    </div>
  `;
  editForm.style.display = 'block';
  commentBody.querySelector('.user_review_comment').style.display = 'none';
}

function updateComment(comment_id, isReply) {
  const newContent = document.getElementById(`editComment-${comment_id}`).value;

  $.ajax({
    url: '/comments/update',
    type: 'POST',

    data: {
      comment_id: comment_id,
      comment_content: newContent
    },
    success: function(response) {
      document.getElementById(`comment-${comment_id}`).querySelector('.user_review_comment').innerText = newContent;
      cancelEdit(comment_id);
      // 페이지 새로고침
      window.location.reload(); // AJAX 요청 완료 후에 페이지를 새로 고침
    },
    error: function(error) {
      console.error('Error updating comment:', error);
    }
  });
}

function cancelEdit(comment_id) {
  const editForm = document.getElementById(`editCommentForm-${comment_id}`);
  editForm.style.display = 'none';
  document.getElementById(`comment-${comment_id}`).querySelector('.user_review_comment').style.display = 'block';
}

function deleteComment(comment_id) {
  if (confirm('정말로 삭제하시겠습니까?')) {
    $.ajax({
      url: '/comments/delete',
      type: 'POST',

      data: {
        comment_id: comment_id
      },
      success: function(response) {
        const commentElement = document.getElementById(`comment-${comment_id}`);
        commentElement.querySelector('.user_review_comment').innerText = '이미 삭제된 댓글입니다.';
        commentElement.querySelectorAll('.edit, .delete, .reply').forEach(btn => btn.style.display = 'none');
        location.reload();
      },
      error: function(error) {
        console.error('Error deleting comment:', error);
      }
    });
  }
}

// 후기 삭제 모달창
function handleDeleteReview(event) {
  event.preventDefault();
  const password = document.getElementById('delete_pwd').value;
  const logId = document.getElementById('logId').value;

  $.ajax({
    url: '/reviews/delete',
    type: 'POST',
    data: {
      password: password,
      log_id: logId
    },
    success: function(response) {
      if (response === 'success') {
        alert('후기가 삭제되었습니다.');
        window.location.href = '/review_main';
      } else {
        alert('비밀번호가 일치하지 않습니다.');
      }
    },
    error: function(error) {
      console.error('Error deleting review:', error);
      alert('후기 삭제 중 오류가 발생했습니다.');
    }
  });
}

// 후기 수정 모달창
document.addEventListener('DOMContentLoaded', function () {
  const modal = document.getElementById('editModal');
  const modalDel = document.querySelector('.modal_del');
  const modalCloseDel = document.querySelector('.del_close_btn');
  const modalOpenDel = document.querySelector('.review_btn_del');
  const modalOpen = document.querySelector('.review_btn_rep');
  const modalClose = document.querySelector('.close_btn');

  // 삭제 버튼 눌렀을 때 모달 팝업
  modalOpenDel.addEventListener('click', function () {
    modalDel.classList.add('on');
  });

  // 수정 버튼을 눌렀을 때 모달팝업이 열림
  modalOpen.addEventListener('click', function () {
    modal.classList.add('on');
  });

  // 닫기 버튼을 눌렀을 때 모달팝업이 닫힘
  modalClose.addEventListener('click', function () {
    modal.classList.remove('on');
  });

  // 삭제 모달창 - 닫기 버튼을 눌렀을 때 모달팝업이 닫힘
  modalCloseDel.addEventListener('click', function () {
    modalDel.classList.remove('on');
  });
});

// 수정 후기 폼 제출 및 db 저장
function submitEditReviewForm(event) {
  event.preventDefault(); // 기본 폼 제출 동작 방지

  const logId = document.getElementById('logId').value;
  const reviewSubject = document.getElementById('reviewheader_area').value;
  const reviewContent = document.getElementById('review_txt').value;
  const reviewStars = Array.from(document.querySelectorAll('#review_re .star_rating .star.on')).length;

  console.log("logId = " + logId);
  console.log("reviewSubject = " + reviewSubject);
  console.log("reviewContent = " + reviewContent);
  console.log("reviewStars = " + reviewStars);

  $.ajax({
    url: '/reviews/update',
    type: 'POST',

    data: {
      log_id: logId,
      reviewSubject: reviewSubject,
      reviewContent: reviewContent,
      reviewStars: reviewStars
    },
    success: function (response) {
      alert('수정이 완료되었습니다.');
      window.location.href = '/review_detail?log_id=' + logId + '&board_subject=' + reviewSubject;
    },
    error: function (error) {
      console.error('Error updating review:', error);
      alert('수정에 실패하였습니다.');
    }
  });
}
