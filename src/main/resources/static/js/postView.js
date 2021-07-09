const postId = $('#forPostId').val();

const addPostLike = () => {
  $.ajax({
    url: '/community/post/'+postId+'/addLike',
    method: 'POST',
    error:function(request,status,error){
      if(request.status == 409) {
        alert('해당 게시글에 이미 좋아요 추가했습니다.');
      } else {
        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      }
    }
  }).done(function(data, textStatus, xhr) {
    location.href = '/community/post/'+postId;
  });
}

const addCommentLike = (commentId) => {
  $.ajax({
      url: '/community/post/'+postId+'/comment/addlike',
      method: 'POST',
      data: {"commentId":commentId},
      error:function(request,status,error){
        if(request.status == 409) {
          alert('해당 게시글에 이미 좋아요 추가했습니다.');
        } else {
          alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
      }
  }).done(function(data,textStatus, xhr) {
      location.href = '/community/post/'+postId;
  })
}

const needLogin = () => {
  alert('로그인이 필요합니다.');
}
