
// CSRF 토큰을 가져오는 함수
function getCookie(name) {
    let cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        const cookies = document.cookie.split(';');
        for (let i = 0; i < cookies.length; i++) {
            const cookie = cookies[i].trim();
            if (cookie.substring(0, name.length + 1) === name + '=') {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}

const csrftoken = getCookie('csrftoken');

// 문의를 서버로 전송하는 함수
function submitinquiry(event) {
    event.preventDefault();
    const inquirySubject = $('#inquiryheader_area').val();
    const inquiryBody = $('#inquiry_txt').val();
    console.log('*** 문의제목 = ' + inquirySubject);
    console.log('*** 문의내용 = ' + inquiryBody);

    if (!inquirySubject) {
        alert('문의 제목을 입력하십시오.');
        $('#inquiryheader_area').focus();
        return false;
    }
    if (!inquiryBody) {
        alert('문의 내용을 입력하십시오.');
        $('#inquiry_txt').focus();
        return false;
    }

    // URL 인코딩된 폼 데이터로 보내기
    $.ajax({
        type: 'POST',
        url: '/mypage/submit_inquiry',
        data: {
            inquiry_title: inquirySubject,
            inquiry_content: inquiryBody
        },
        success: function (response) {
            if (response==='redirect'){

                            window.location.href='/loginPage';
                        }

                        if(response==='submit'){

                            alert('문의 등록이 완료 되었습니다.');
                            window.location.href = '/mypage/inquiry_list';
                        }

                        if (response==='error'){

                            alert('문의 등록이 완료 되었습니다.');
                            window.location.href = '/mypage/inquiry_list';
                        }
        },
        error: function (xhr, status, error) {
            console.error('문의 제출에 실패했습니다:', error);
        },
    });
}



document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('button.like-button').forEach((button) => {
        let logId = button
            .getAttribute('onclick')
            .match(/toggleLike\(this, (\d+)\)/)[1];
        toggleButtonState(logId);
    });
});

// 댓글 가져오기
document.addEventListener('DOMContentLoaded', function () {
    const log_id = document.getElementById('logId').value;
    fetchComments(log_id);
});

function fetchComments(log_id) {
    $.ajax({
        url: '/comments',
        type: 'GET',
        data: {
            log_id: log_id,
        },
        success: function (response) {
            const commentsList = document.getElementById('comments-list');
            commentsList.innerHTML = '';
            response.forEach((comment) => {
                const commentElement = createCommentElement(comment);
                commentsList.appendChild(commentElement);
                fetchReplies(comment.comment_id);
            });
        },
        error: function (error) {
            console.error('Error fetching comments:', error);
        },
    });
}

function fetchReplies(parent_id) {
    $.ajax({
        url: '/comments/replies',
        type: 'GET',
        data: {
            parent_id: parent_id,
        },
        success: function (response) {
            response.forEach((reply) => {
                const replyElement = createCommentElement(reply, true);
                document
                    .getElementById(`comment-body-${parent_id}`)
                    .appendChild(replyElement);
            });
        },
        error: function (error) {
            console.error('Error fetching replies:', error);
        },
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
                        ${
        comment.child_id === 0 && comment.is_deleted === 0
            ? `<a data-toggle="collapse" href="#recomment-${comment.comment_id}" aria-expanded="false" aria-controls="recomment-${comment.comment_id}" onclick='toggleReplyForm(${comment.comment_id})'><span><i class="fa fa-reply"></i> reply</span></a>`
            : ''
    }
                        ${
        comment.user_id === currentUserId &&
        comment.is_deleted === 0
            ? `
                        <input data-idx="${comment.comment_id}" src="/images/pencil_line_icon.png" type="image" class="edit" value="수정" onclick="editComment(${comment.comment_id}, ${isReply})">
                        <input data-idx="${comment.comment_id}" src="/images/trash_outline_icon.png" type="image" class="delete" value="삭제" onclick="deleteComment(${comment.comment_id})">`
            : ''
    }
                    </div>
                </div>
            </div>
            <div class="user_inquiry_comment">${
        comment.is_deleted === 1
            ? '이미 삭제된 댓글입니다.'
            : comment.comment_content
    }</div>
            ${
        comment.child_id === 0
            ? `
            <div class="bg-light p-2 collapse" id="recomment-${comment.comment_id}" style="display: none;">
                <div class="d-flex flex-row align-items-start" style="padding-left:60px">
                    <img class="rounded-circle" src="/images/icon_car_role.png" width="40">
                    <textarea id="reComment-${comment.comment_id}" class="form-control ml-1 shadow-none textarea" placeholder="대댓글을 남겨주세요." style="font-size: 14px; width:800px" data-comment-type="reply" required></textarea>
                </div>
                <div class="mt-2 text-right"><button class="btn btn-primary btn-sm shadow-none" type="button" onclick='addComment(${comment.comment_id}, 1)'>Post comment</button></div>
            </div>
            `
            : ''
    }
            <div class="collapse mt-2" id="editCommentForm-${
        comment.comment_id
    }"></div>
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
            child_id: commentType === 'reply' ? 1 : 0,
        },
        success: function (response) {
            const newComment = response;
            const commentElement = createCommentElement(newComment, !!parent_id);
            if (parent_id) {
                document
                    .getElementById(`comment-body-${parent_id}`)
                    .appendChild(commentElement);
                document.getElementById(`reComment-${parent_id}`).value = ''; // 대댓글 textarea 초기화
                toggleReplyForm(parent_id); // 대댓글 작성 창 접기
            } else {
                document.getElementById('comments-list').appendChild(commentElement); // 댓글 textarea 초기화
                document.getElementById('commentwritearea').value = ''; // 댓글 textarea 초기화
            }
            location.reload();
        },
        error: function (error) {
            console.error('Error adding comment:', error);
        },
    });
}

function editComment(comment_id, isReply) {
    const commentBody = document.getElementById(`comment-body-${comment_id}`);
    const editForm = document.getElementById(`editCommentForm-${comment_id}`);
    console.log('commentbody = ' + commentBody);
    console.log('editForm = ' + editForm);

    // 기존 내용 가져오기
    const currentContent = commentBody.querySelector(
        '.user_inquiry_comment'
    ).innerText;

    console.log('currentContent = ' + currentContent);

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
    commentBody.querySelector('.user_inquiry_comment').style.display = 'none';
}

function updateComment(comment_id, isReply) {
    const newContent = document.getElementById(`editComment-${comment_id}`).value;

    $.ajax({
        url: '/comments/update',
        type: 'POST',

        data: {
            comment_id: comment_id,
            comment_content: newContent,
        },
        success: function (response) {
            document
                .getElementById(`comment-${comment_id}`)
                .querySelector('.user_inquiry_comment').innerText = newContent;
            cancelEdit(comment_id);
            // 페이지 새로고침
            window.location.reload(); // AJAX 요청 완료 후에 페이지를 새로 고침
        },
        error: function (error) {
            console.error('Error updating comment:', error);
        },
    });
}

function cancelEdit(comment_id) {
    const editForm = document.getElementById(`editCommentForm-${comment_id}`);
    editForm.style.display = 'none';
    document
        .getElementById(`comment-${comment_id}`)
        .querySelector('.user_inquiry_comment').style.display = 'block';
}

function deleteComment(comment_id) {
    if (confirm('정말로 삭제하시겠습니까?')) {
        $.ajax({
            url: '/comments/delete',
            type: 'POST',

            data: {
                comment_id: comment_id,
            },
            success: function (response) {
                const commentElement = document.getElementById(`comment-${comment_id}`);
                commentElement.querySelector('.user_inquiry_comment').innerText =
                    '이미 삭제된 댓글입니다.';
                commentElement
                    .querySelectorAll('.edit, .delete, .reply')
                    .forEach((btn) => (btn.style.display = 'none'));
                location.reload();
            },
            error: function (error) {
                console.error('Error deleting comment:', error);
            },
        });
    }
}

// 문의 삭제 모달창
function handleDeleteinquiry(event) {
    event.preventDefault();

    const password = document.getElementById('delete_pwd').value;
    const inquiryId = document.getElementById('inquiry_id').value;
    console.log("password : " + password);
    console.log("inquiryId : " + inquiryId);

    $.ajax({
        url: '/mypage/inquiries/delete',
        type: 'POST',
        data: {
            password: password,
            inquiry_id: inquiryId,
        },
        success: function (response) {
            if (response === 'success') {
                alert('문의가 삭제되었습니다.');
                window.location.href = '/mypage/inquiry_list';
            } else {
                alert('비밀번호가 일치하지 않습니다.');
            }
        },
        error: function (error) {
            console.error('Error deleting inquiry:', error);
            alert('문의 삭제 중 오류가 발생했습니다.');
        },
    });
}

// 문의 수정 모달창
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('editModal');
    const modalDel = document.querySelector('.modal_del');
    const modalCloseDel = document.querySelector('.del_close_btn');
    const modalOpenDel = document.querySelector('.inquiry_btn_del');
    const modalOpen = document.querySelector('.inquiry_btn_rep');
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

// 수정 문의 폼 제출 및 db 저장
function submitEditinquiryForm(event) {
    event.preventDefault(); // 기본 폼 제출 동작 방지

    const inquiryId = document.getElementById('inquiry_id').value;
    const inquirySubject = document.getElementById('inquiryheader_area').value;
    const inquiryContent = document.getElementById('inquiry_txt').value;

    console.log('inquiryId = ' + inquiryId);
    console.log('inquirySubject = ' + inquirySubject);
    console.log('inquiryContent = ' + inquiryContent);

    $.ajax({
        url: '/mypage/inquiries/update',
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        // contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: JSON.stringify({
            inquiry_id: inquiryId,
            inquiry_title: inquirySubject,
            inquiry_content: inquiryContent,
        }),
        success: function (response) {
            alert('수정이 완료되었습니다.');
            window.location.href = '/mypage/inquiry_detail?inquiry_id=' + encodeURIComponent(inquiryId);
        },
        error: function (error) {
            console.error('Error updating inquiry:', error);
            alert('수정에 실패하였습니다.');
        },
    });
}
