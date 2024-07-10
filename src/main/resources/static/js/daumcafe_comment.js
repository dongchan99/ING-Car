(function () {
  window.commentApi = {
    removeList: function (fldid, dataid, commentList) {
      // 추후 dwr 제거시 해당 부분만 삭제 할 수 있도록 처리
      return {
        done: function (callback) {
          ShortComment.removeList.call(
            window.commentApi,
            fldid,
            dataid,
            commentList,
            {
              callback: callback,
            }
          );
        },
      };
    },
    spamList: function (fldid, dataid, commentList) {
      return {
        done: function (callback) {
          ShortComment.removeListForSpam.call(
            window.commentApi,
            fldid,
            dataid,
            commentList,
            {
              callback: callback,
            }
          );
        },
      };
    },
    getList: function (commentContext, _isImsi) {
      var isImsi = _isImsi || false;
      if (
        commentContext.tobePage !== commentContext.lastPage &&
        commentContext.currentPage === commentContext.tobePage
      ) {
        // 기존 로직 참고. 같은 페이지를 로딩하게 되는 경우 첫페이지에서 해당 페이지로 이동하는 형태로 동작해야함.
        return {
          done: function (callback) {
            ShortComment.getList.call(
              window.commentApi,
              commentContext.fldid,
              commentContext.dataid,
              commentContext.currentPage,
              null,
              null,
              null,
              isImsi,
              false,
              {
                callback: function (result) {
                  commentContext.currentPage = commentContext.lastPage;
                  var cdepth_match = result.match(
                    /name="F_CDEPTH"(\s*)value="(.*)"/gi
                  );
                  if (!cdepth_match) return;
                  commentContext.cdepth = cdepth_match[0].replace(
                    /(.*)value="(.*)"/gi,
                    '$2'
                  );
                  window.commentApi
                    .getList(commentContext, _isImsi)
                    .done(callback);
                },
              }
            );
          },
        };
      }
      return {
        done: function (callback) {
          ShortComment.getList.call(
            window.commentApi,
            commentContext.fldid,
            commentContext.dataid,
            commentContext.currentPage,
            commentContext.tobePage,
            commentContext.cdepth,
            null,
            isImsi,
            false,
            {
              callback: callback,
            }
          );
        },
      };
    },
    searchList: function (searchContext, _isImsi) {
      var isImsi = _isImsi || false;
      var query = '';
      var nickname = '';
      if (searchContext.item === 'cmtContents') {
        query = searchContext.searchText;
      } else {
        nickname = searchContext.searchText;
      }
      return {
        done: function (callback) {
          ShortComment.search.call(
            window.commentApi,
            {
              fldid: searchContext.fldid,
              dataid: searchContext.dataid,
              item: searchContext.item,
              query: query,
              nickname: nickname,
              open: false,
              imsi: isImsi,
              pagenum: searchContext.page,
            },
            { callback: callback }
          );
        },
      };
    },
    write: function (commentData) {
      var commentWriteData = {
        fldid: commentData.fldid,
        dataid: commentData.dataid,
        pardataRegdttm: commentData.articleRegdt,
        comment: commentData.comment,
        texticon: false,
        hidden: commentData.isHidden,
        imageURL: commentData.imageUrl || null,
        imageName: commentData.imageName || null,
        imageSize: commentData.imageSize || null,
        seq: commentData.seq || null,
        parseq: commentData.parseq || null,
        feedbackseq: commentData.feedbackseq || null,
      };
      return {
        done: function (callback) {
          ShortComment.write.call(window.commentApi, commentWriteData, {
            callback: callback,
          });
        },
      };
    },
    modify: function (commentData) {
      var commentModifyData = {
        fldid: commentData.fldid,
        dataid: commentData.dataid,
        comment: commentData.comment,
        texticon: false,
        hidden: commentData.isHidden,
        imageURL: commentData.imageUrl || null,
        imageName: commentData.imageName || null,
        imageSize: commentData.imageSize || null,
        seq: commentData.seq,
        parseq: commentData.parseq || null,
      };
      return {
        done: function (callback) {
          ShortComment.modify.call(window.commentApi, commentModifyData, {
            callback: callback,
          });
        },
      };
    },
    remove: function (fldid, dataid, seq) {
      var commentRemoveData = {
        fldid: fldid,
        dataid: dataid,
        seq: seq,
      };
      return {
        done: function (callback) {
          ShortComment.remove.call(window.commentApi, commentRemoveData, {
            callback: callback,
          });
        },
      };
    },
  };

  (function ($) {
    var tagRegex = /<\s?[^>]*\/?\s?>/gi;
    var linkRegex =
      /https?:\/\/(((([a-z가-힣\d](-*[a-z가-힣\d])*)\.)+[a-z가-힣]{2,}|((\d{1,3}\.){3}\d{1,3}))(:\d+)?(\/[-a-z가-힣\d%_.~+]*)*(\?[;&a-z가-힣\d%_.~+=-]*)?(#[-a-z가-힣\d_]*)?)/gi;
    window.makeLink = function (elementList) {
      $(elementList).each(function (index, element) {
        var htmlText = $(element).html();
        var tags = htmlText.match(tagRegex);
        $(tags).each(function (index, matchTag) {
          // 텍스티콘 예외 처리
          htmlText = htmlText.replace(matchTag, '{' + index + '}');
        });
        htmlText = htmlText.replace(
          linkRegex,
          '<a href="https://$1" target="_blank">$&</a>'
        );
        $(tags).each(function (index, matchTag) {
          // 텍스티콘 복구 처리
          htmlText = htmlText.replace('{' + index + '}', matchTag);
        });
        $(element).html(htmlText);
      });
    };
  })(jQuery);

  (function ($) {
    var listComment = $('.list_comment');
    var simplePaging;
    var isSearching = false;
    var commentCount = $('#comment-btn .txt_num');

    var getCommonCommentList = function (toPage, callback) {
      callback = callback || function () {};
      var currentPage = $('#comment-result-info').data('pagingCurrent');
      var isImsi = $('input[name="IS_IMSI"]').val() || false;
      var cdepth = $('input[name="F_CDEPTH"]').val();

      if (currentPage < toPage) {
        cdepth = $('input[name="L_CDEPTH"]').val();
      }

      window.commentApi
        .getList(
          {
            fldid: CAFEAPP.ui.FLDID,
            dataid: CAFEAPP.ui.DATAID,
            currentPage: currentPage,
            tobePage: toPage,
            cdepth: cdepth,
            lastPage: $('#comment-result-info').data().pagingTotal,
          },
          isImsi
        )
        .done(function (html) {
          hide();
          listComment.html(html);
          window.makeLink(listComment.find('.original_comment'));
          if (!isEmptyList()) {
            show();
          }
          commentCount.text($('#comment-result-info').data('resultCount'));
          window.commentSearch.hideSearchProcess();
          kakaoEmoticonUpdate();
          callback();
        });
    };

    var getSearchCommentList = function (toPage, callback) {
      callback = callback || function () {};
      var isImsi = $('input[name="IS_IMSI"]').val() || false;

      window.commentApi
        .searchList(
          $.extend(
            {},
            {
              fldid: CAFEAPP.ui.FLDID,
              dataid: CAFEAPP.ui.DATAID,
              page: toPage || 0,
            },
            window.commentSearch.getOption()
          ),
          isImsi
        )
        .done(function (html) {
          hide();
          listComment.html(html);
          window.makeLink(listComment.find('.original_comment'));
          if (!isEmptyList()) {
            show();
          }
          window.commentSearch.showSearchProcess();
          kakaoEmoticonUpdate();
          callback();
        });
    };

    var isEmptyList = function () {
      return listComment.find('li').length <= 1;
    };

    var show = function () {
      simplePaging.show();
    };

    var hide = function () {
      simplePaging.hide();
    };

    var scrollToPaging = function () {
      $('html').scrollTop(simplePaging.offset().top - $(window).height() / 2);
    };

    var scrollToCommentTop = function () {
      $('html').scrollTop($('#comment-list').offset().top);
    };

    window.commentPaging = (function () {
      return {
        init: function (selector, currentPage, totalPage, _isSearching) {
          simplePaging = $(selector);
          simplePaging.pagination({
            currentPage: currentPage,
            pages: totalPage,
            prevText:
              '<span class="ico_prev"><span class="sr_only">이전 목록으로 이동</span></span>',
            nextText:
              '<span class="ico_next"><span class="sr_only">다음 목록으로 이동</span></span>',
            displayedPages: '9',
            edges: 1,
            ellipsePageSet: false,
            onPageClick: function (toPage) {
              window.commentPaging.getCommentList(toPage, scrollToPaging);
            },
          });
          isSearching = !!_isSearching;

          hide();
          if (!isEmptyList()) {
            show();
          }
        },
        activateSearching: function () {
          isSearching = true;
          scrollToCommentTop();
          window.commentPaging.getCommentList(1);
        },
        activateCommon: function () {
          isSearching = false;
          scrollToCommentTop();
          window.commentPaging.getCommentList(null);
        },
        getCommentList: function (toPage, callback) {
          if (isSearching) {
            getSearchCommentList(toPage, callback);
            return;
          }
          getCommonCommentList(toPage, callback);
        },
        setPagingInfo: function (currentPage, totalPage) {
          simplePaging.pagination('setPagesCount', totalPage);
          simplePaging.pagination('drawPage', currentPage);
        },
        refresh: function (isScrollToPaging) {
          if (isScrollToPaging) {
            window.commentPaging.getCommentList(
              simplePaging.pagination('getCurrentPage'),
              scrollToPaging
            );
            return;
          }
          window.commentPaging.getCommentList(
            simplePaging.pagination('getCurrentPage')
          );
        },
        refreshByLastPage: function () {
          window.commentPaging.getCommentList(null, scrollToPaging);
        },
      };
    })();
  })(jQuery);

  (function ($) {
    // 운영자 기능
    var commentList = $('.list_comment');
    var allCheck = $('#all_check');

    allCheck.change(function (e) {
      var currentState = $(e.currentTarget).prop('checked');
      var label = commentList.find('.lab_choice');

      commentList.find('.inp_chk').prop('checked', currentState);
      allCheck.closest('.lab_choice').removeClass('choice_checked');
      label.removeClass('choice_checked');
      if (currentState) {
        allCheck.closest('.lab_choice').addClass('choice_checked');
        label.addClass('choice_checked');
      }
    });

    commentList.on('click', '.inp_chk', function (e) {
      var currentTarget = $(e.currentTarget);
      var currentState = $(e.currentTarget).prop('checked');
      var label = currentTarget.closest('.lab_choice');

      allCheck.prop('checked', false);
      allCheck.closest('.lab_choice').removeClass('choice_checked');
      label.removeClass('choice_checked');
      if (currentState) {
        label.addClass('choice_checked');
      }
    });

    $('#remove-check-all').click(function () {
      var checkedList = commentList.find('.inp_chk:checked');
      if (checkedList.length === 0) {
        alert('삭제할 댓글을 선택해 주세요.');
        return;
      }

      if (confirm('정말 삭제 하시겠습니까?')) {
        window.commentApi
          .removeList(
            CAFEAPP.ui.FLDID,
            CAFEAPP.ui.DATAID,
            $.map(checkedList, function (checkedInput) {
              return $(checkedInput).closest('li').data('seq') + '';
            })
          )
          .done(function () {
            window.commentPaging.refresh();
          });
      }
    });
    (function ($, Handlebars) {
      var editorCount = 0;
      cafe.handlebars.helper.registerThumbnail();
      var editorTemplate = Handlebars.compile(
        $('#handlebars-comment-editor').html()
      );
      var previewOriginalTemplate = $(
        '#handlebars-comment-editor-preview-image'
      ).html();
      var previewTemplate = Handlebars.compile(previewOriginalTemplate);
      Handlebars.registerPartial('editorPreviewImage', previewOriginalTemplate);

      var emoticonLayer = $('#keyboardLayer');
      $(function () {
        // 이모티콘 업로더 설정. 하나만 생성할 수 있으므로 하나를 생성해서 같이 사용해야한다.
        Kakao.Emoticon.init({
          needConnectKakao: true,
          callback: {
            hideKeyboard: function () {
              $('.keyboard_btn').removeClass('on');
              $('#comment-paging').after(emoticonLayer);
            },
            connectKakao: function () {
              window.open(
                'https://member.daum.net/kakaoaccount/linkform.daum',
                '_blank',
                'width=1010, height=676'
              );
            },
          },
        });
      });

      window.commentEditor = function () {
        var _editorNumber;
        var editorElement;
        var submit;
        var textCounter;

        var init = function (_submit) {
          if (submit) {
            // 두번 init 되는 것을 막음.
            return editor;
          }
          _editorNumber = editorCount++;
          setSubmit(_submit);
          return editor;
        };

        var setSubmit = function (_submit) {
          submit = function () {
            if (ableConfirm()) {
              _submit();
            } else {
              alert('내용을 입력해주세요.');
            }
          };
          return editor;
        };

        var remove = function () {
          if (editorElement) {
            editorElement.remove();
          }
        };

        var insertAfterEditor = function (beforeElement, context) {
          remove();
          context.editorNumber = _editorNumber;
          editorElement = $(editorTemplate(context));
          editorElement.insertAfter(beforeElement);
        };

        var showAfter = function (beforeElement, commentContext) {
          insertAfterEditor(beforeElement, commentContext || {});
          activate();
          if (commentContext.isEmoticon) {
            Kakao.Emoticon.update();
          }

          return editor;
        };

        var toggleHidden = function (e) {
          var currentTarget = $(e.currentTarget);
          var isHidden = currentTarget.data('isHidden');
          isHidden = !isHidden;
          currentTarget.toggleClass('ico_on');
          currentTarget.data('isHidden', isHidden);
        };

        var renderPreviewImage = function (data, isEmoticon) {
          editorElement.find('.preview_image_area').remove();
          if (isEmoticon) {
            editorElement
              .find('textarea')
              .after(
                previewTemplate({
                  isEmoticon: isEmoticon,
                  emoticon: data.emoticon,
                })
              );
            return;
          }

          if (data && data.image) {
            editorElement
              .find('textarea')
              .after(previewTemplate({ imageUrl: data.image }));
          }
        };

        var setEmoticon = function (emoticonData) {
          if (!emoticonData) {
            renderPreviewImage();
            return;
          }

          renderPreviewImage(
            {
              emoticon: {
                id: emoticonData.item_id,
                idx: emoticonData.resource_id,
                type: emoticonData.item_sub_type,
                version: emoticonData.item_version,
              },
            },
            true
          );
          Kakao.Emoticon.update();
        };

        var attachImage = function (data) {
          var imageUploadButton = editorElement.find('.image_upload_button');
          imageUploadButton
            .data('imageUrl', data.image || '')
            .data('imageName', data.filename || '')
            .data('imageSize', data.filesize || '');
          renderPreviewImage(data);
        };

        var ableConfirm = function () {
          var hasPermission = editorElement.find('textarea').length > 0;
          var notEmptyText =
            hasPermission &&
            editorElement.find('textarea').val().trim().length > 0;
          var existImage =
            editorElement.find('.image_upload_button').data('imageUrl') !== '';
          var existEmoticon = editorElement.find('.kakao_emoticon').length > 0;

          return hasPermission && (notEmptyText || existImage || existEmoticon);
        };

        var editorHeightSetting = function (e) {
          // 유동적으로 늘어나는 에디터 스펙
          var textarea = editorElement.find('textarea');
          if (textarea.length === 0) {
            return;
          }

          var baseHeight = 32;
          var increaseHeightGap = 18;
          var minHeight = baseHeight + increaseHeightGap * 3;
          var maxHeight = baseHeight + increaseHeightGap * 9;

          var text = textarea.val();
          if (e.key === 'Enter') {
            text += '\n';
          }
          var regExpMatchArray = text.match(/\n/g) || []; // 개행문자 갯수 파악
          var currentShowLine = Math.min(
            Math.max(regExpMatchArray.length, 3),
            9
          ); // 최소 4줄. 최대 10줄
          var heightByEnter = baseHeight + increaseHeightGap * currentShowLine;

          var currentHeight = Math.min(
            Math.max(minHeight, textarea.prop('scrollHeight') - 5),
            maxHeight
          );
          textarea.height(Math.max(heightByEnter, currentHeight));
        };

        var toggleEmoticonLayer = function (e) {
          $('.keyboard_btn').removeClass('on');
          var currentButton = $(e.currentTarget);
          currentButton.addClass('on');
          currentButton.after(emoticonLayer);
          Kakao.Emoticon.toggleKeyboard(emoticonLayer.get(0));
          Kakao.Emoticon.emoticon.listeners.selectEmoticon = [
            function (obj) {
              setEmoticon(obj);
              Kakao.Emoticon.toggleKeyboard(emoticonLayer.get(0)); // 선택후 레이어 닫기
            },
          ];
        };

        var activate = function () {
          var textarea = editorElement.find('textarea');
          if (textarea.length > 0) {
            textCounter = cafe.ui.textCounter();
            textCounter.init(
              textarea,
              editorElement.find('.num_count'),
              600,
              'empty'
            );
          }
          editorElement
            .find('.image_upload_button')
            .attr('id', 'attach_uploader' + _editorNumber); // 에디터는 여러개 생성 될 수 있음

          // new AttachImageUploader().init({
          //   grpid: CAFEAPP.GRPID,
          //   useExpire: true,
          //   uploadBtnId: 'attach_uploader' + _editorNumber,
          //   limitFilesize: {
          //     value: 1024 * 1024 * 20,
          //     callback: function () {
          //       alert('20MB 이하의 이미지만 등록할 수 있습니다.');
          //     },
          //   },
          //   success: function (data) {
          //     argosInspection(CAFEAPP.GRPID, 'comment').inspectionImage(
          //       data,
          //       attachImage,
          //       function () {
          //         alert('이미지 업로드에 실패했습니다.');
          //       }
          //     );
          //   },
          //   fail: function () {
          //     alert('이미지 업로드에 실패했습니다.');
          //   },
          // });
          editorHeightSetting({ key: '' }); // 초기 높이 설정

          var flagForMacBug = false; // 맥에서 작성중인 한글이 있을때 탭키 누르면 발생하는 오류 수정용 플래그. 발생하지 않는 경우도 있음.
          var tempText = '';
          editorElement
            .on('click', '.secret_button', toggleHidden) // 비밀 댓글 토글
            .on('click', '.confirm_button', submit) // 댓글 등록 버튼
            .on('click', '.keyboard_btn', toggleEmoticonLayer) // 이모티콘 레이어 토글
            .on('click', '.btn_delete', function () {
              // 이미지 삭제
              attachImage({});
              setEmoticon();
            })
            .on('keydown', 'textarea', editorHeightSetting) // 입력한 내용에 따른 높이 조정
            .on('keydown', 'textarea', function (e) {
              // 편의 기능. keydown 사용시 맥에서 한글의 마지막 글자가 반복해서 찍히는 현상이 생김. 주의!!
              if (e.key === 'Enter' && (e.ctrlKey || e.metaKey)) {
                // ctrl + enter & command + enter
                submit();
              }
              if (e.key === 'Tab') {
                // 등록 버튼 바로 앞으로 포커싱해서 등록버튼으로 포커싱이 이동. ie 에서 preventDefault해도 포커싱이 그냥 움직임.
                if (flagForMacBug) {
                  textarea.val(tempText);
                  editorElement.find('.secret_button').focus();
                  return;
                }
                flagForMacBug = true;
                tempText = textarea.val();
                editorElement.find('.secret_button').focus();
                setTimeout(function () {
                  flagForMacBug = false;
                }, 200);
              }
            });
          textarea.trigger('keydown'); // 초기 텍스트 길이 세팅
        };

        var reset = function () {
          var textarea = editorElement.find('textarea');
          if (textarea.length > 0) {
            textarea.val('');
          }
          attachImage({});
          setEmoticon();
          textCounter.resetCounter(0);
          textarea.trigger('keyup');
          editorHeightSetting({ key: '' });
        };

        var editor = {
          init: init,
          setSubmit: setSubmit,
          remove: remove,
          showAfter: showAfter,
          getCommentData: getCommentData,
          reset: reset,
        };

        return editor;
      };
    })(jQuery, Handlebars);

    (function ($) {
      var makeReplyContext = function (commentContext) {
        var context = $.extend(
          {},
          {
            fldid: CAFEAPP.ui.FLDID,
            dataid: CAFEAPP.ui.DATAID,
            articleRegdt: CAFEAPP.ui.PLAIN_REGDT,
            parseq: null,
            feedbackseq: null,
          },
          commentContext
        );

        if (context.parseq !== 0) {
          context.feedbackseq = context.seq;
        } else {
          context.parseq = context.seq;
          context.feedbackseq = null;
        }
        context.seq = 1; // 답댓글에선 1로 세팅함.

        return context;
      };
      var makeModifyContext = function (commentElement, commentContext) {
        var mentionNickname = commentElement
          .find('.comment_post .mention_nickname')
          .text();
        var commentText = commentElement
          .find('.original_comment')
          .html()
          .replace(/<br>/gi, '\n')
          .replace(/(<([^>]+)>)/gi, '')
          .replace(/&lt;/gi, '<')
          .replace(/&gt;/gi, '>')
          .replace(/&amp;/gi, '&');
        var context = $.extend(
          {},
          {
            isFull: false,
            hasNotPermission:
              $('input[name="HAS_PERMISSION"]').val() !== 'true',
            isNotLogin: !CAFEAPP.CAFE_ENCRYPT_LOGIN_USERID,
            fldid: CAFEAPP.ui.FLDID,
            dataid: CAFEAPP.ui.DATAID,
            comment: commentText,
            memberRole: CAFEAPP.MEMBER_ROLENAME,
            permissionRole: CAFEAPP.FLD_SHRTCMTPERMROLENAME,
          },
          commentContext
        );
        if (commentContext.isEmoticon) {
          context.emoticon = commentElement
            .find('.box_post .kakao_emoticon')
            .data();
        }
        context.nickname = mentionNickname.slice(1);
        return context;
      };

      var commonCommentEditor = window.commentEditor().init(function () {
        var commonCommentData = {
          fldid: CAFEAPP.ui.FLDID,
          dataid: CAFEAPP.ui.DATAID,
          articleRegdt: CAFEAPP.ui.PLAIN_REGDT,
        };
        window.commentApi
          .write(
            $.extend(
              {},
              commonCommentData,
              commonCommentEditor.getCommentData()
            )
          )
          .done(function () {
            commonCommentEditor.reset();
            window.commentPaging.refreshByLastPage();
          });
      });
      commonCommentEditor.showAfter($('.simple_paging'), {
        isFull: $('input[name="COMMENT_FLOODED"]').val() === 'true',
        hasNotPermission: $('input[name="HAS_PERMISSION"]').val() !== 'true',
        isNotLogin: !CAFEAPP.CAFE_ENCRYPT_LOGIN_USERID,
        isLogin: !!CAFEAPP.CAFE_ENCRYPT_LOGIN_USERID,
        memberRole: CAFEAPP.MEMBER_ROLENAME,
        permissionRole: CAFEAPP.FLD_SHRTCMTPERMROLENAME,
      });

      var hasReplyEditor = function (writeButton) {
        if (!writeButton) {
          return false;
        }
        return writeButton.text() === '답글 접기';
      };

      var toggleText = function (allTarget, currentTarget) {
        var hasReplyEditorCase = hasReplyEditor(currentTarget);
        allTarget.text('답글');
        if (!hasReplyEditorCase && currentTarget) {
          currentTarget.text('답글 접기');
        }
      };

      var replyCommentEditor = window.commentEditor().init(function () {});
      $('.comment_view').on('click', '.link_write', function (e) {
        var currentTarget = $(e.currentTarget);
        var hasReplyEditorCase = hasReplyEditor(currentTarget); // toggleText 로 인해 정보가 변경 되므로 먼저 여부를 알아둬야함.
        toggleText($('.comment_view .link_write'), currentTarget);
        if (hasReplyEditorCase) {
          // 답글 접기를 눌렀을 경우
          replyCommentEditor.remove();
          return;
        }

        var commentPost = currentTarget.closest('.comment_post');
        var commentContext = currentTarget.closest('li').data();
        var writeContext = makeReplyContext(commentContext);
        replyCommentEditor.setSubmit(function () {
          window.commentApi
            .write(
              $.extend({}, writeContext, replyCommentEditor.getCommentData())
            )
            .done(function () {
              window.commentPaging.refresh();
            });
        });
        replyCommentEditor.showAfter(commentPost, {
          isFull: $('input[name="COMMENT_FLOODED"]').val() === 'true',
          hasNotPermission: $('input[name="HAS_PERMISSION"]').val() !== 'true',
          isNotLogin: !CAFEAPP.CAFE_ENCRYPT_LOGIN_USERID,
          isLogin: !!CAFEAPP.CAFE_ENCRYPT_LOGIN_USERID,
          isHidden: writeContext.isHidden,
          memberRole: CAFEAPP.MEMBER_ROLENAME,
          permissionRole: CAFEAPP.FLD_SHRTCMTPERMROLENAME,
          nickname: writeContext.feedbackseq ? commentContext.nickname : '',
        });
      });

      $('#more-menu-container').on('click', '.modify_comment', function (e) {
        window.MoreMenuLayer.hide();
        toggleText($('.comment_view .link_write'));

        var currentTarget = $(e.currentTarget);
        var commentElement = $(
          '#' + currentTarget.closest('.comment_type').data('targetElementId')
        );
        var commentPost = commentElement.find('.comment_post');
        var commentContext = commentElement.data();
        var modifyContext = makeModifyContext(commentElement, commentContext);
        replyCommentEditor.setSubmit(function () {
          window.commentApi
            .modify(
              $.extend({}, modifyContext, replyCommentEditor.getCommentData())
            )
            .done(function () {
              window.commentPaging.refresh();
            });
        });
        replyCommentEditor.showAfter(commentPost, modifyContext);
        if (modifyContext.isEmoticon) {
          Kakao.Emoticon.update();
        }
      });
    })(jQuery);


      $('#search-process').on('click', '#show-common-comment', function () {
        window.commentPaging.activateCommon();
      });

      $('#comment-search-text')
        .on('keydown', 'input', function (e) {
          if (e.key === 'Enter') {
            window.commentPaging.activateSearching();
          }
        })
        .on('click', 'button', function () {
          window.commentPaging.activateSearching();
        });

      var searchTemplate = Handlebars.compile(
        $('#handlebars-search-result').html()
      );
      window.commentSearch = (function () {
        return {
          getOption: function () {
            return {
              item: commentSearchOption.data('value'),
              searchText: $('#comment-search-text input').val() || '',
            };
          },
          showSearchProcess: function () {
            var commentInfo = $('#comment-result-info').data();
            $('#admin-process').hide();
            $('#search-process')
              .show()
              .html(
                searchTemplate({
                  searchResultCount: commentInfo.resultCount,
                  searchWord: commentInfo.searchWord,
                })
              );
            $('#comment_view').addClass('comment_search_result_view');
            window.commentPaging.setPagingInfo(
              commentInfo.pagingCurrent,
              commentInfo.pagingTotal
            );
          },
          hideSearchProcess: function () {
            var pagingInfo = $('#comment-result-info').data();
            $('#search-process').hide();
            $('#comment_view').removeClass('comment_search_result_view');
            $('#admin-process').show();
            window.commentPaging.setPagingInfo(
              pagingInfo.pagingCurrent,
              pagingInfo.pagingTotal
            );
          },
        };
      })();
    })(jQuery, Handlebars);

    (function ($) {
      // 추가 기능 관련 내용
      $('#comment-list')
        .on('click', '.comment_more .btn_more_menu', function (e) {
          e.stopPropagation();
          var context = $(e.currentTarget).data();
          context.spamable =
            !context.modifiable &&
            context.removable &&
            parseInt(context.commentRolecode) <
              parseInt(CAFEAPP.MEMBER_ROLECODE);
          if (
            !context.modifiable &&
            !context.removable &&
            !context.reportable
          ) {
            // check 필요. 아무것도 할 수 없는 경우가 있는가?
            return;
          }
          window.MoreMenuLayer.show(
            window.MoreMenuLayer.types.COMMENT,
            $(e.target).offset(),
            context
          );
        })
        .on('click', '.btn_reload', function () {
          window.commentPaging.refreshByLastPage();
        })
        .on('click', '.show_deleted_comment', function (e) {
          $(e.currentTarget)
            .closest('.box_post')
            .find('.restrict_comment')
            .show();
        });

      $('#more-menu-container').on('click', '.delete_comment', function (e) {
        if (!confirm('정말 삭제 하시겠습니까?')) {
          return;
        }
        var currentTarget = $(e.currentTarget);
        var commentElement = $(
          '#' + currentTarget.closest('.comment_type').data('targetElementId')
        );
        var commentContext = commentElement.data();
        window.commentApi
          .remove(CAFEAPP.FLDID, CAFEAPP.ui.DATAID, commentContext.seq)
          .done(function () {
            window.commentPaging.refresh();
          });
      });

      window
        .open(
          '/_c21_/bridge/spam_report?service_code=1&platformCode=CAFE&contentType=COMMENT&siteId=' +
            CAFEAPP.GRPID +
            '&bbsId=' +
            CAFEAPP.FLDID +
            '&articleId=' +
            CAFEAPP.ui.DATAID +
            '&commentId=' +
            commentContext.seq +
            '&viewUrl=' +
            encodeURIComponent(
              'https://cafe.daum.net/_c21_/bbs_read?grpid=' +
                CAFEAPP.GRPID +
                '&fldid=' +
                CAFEAPP.FLDID +
                '&datanum=' +
                CAFEAPP.ui.DATAID +
                '&cmtdataid=' +
                commentContext.seq
            ),
          'spam_popup',
          'width=522, height=602, resizable=yes, scrollbars=no'
        )
        .focus();
    });
  })(jQuery);

  (function ($) {
    window.scrollToComment = function (selector) {
      $(window).scrollTop($(selector).position().top);
    };
  })(jQuery);
});
