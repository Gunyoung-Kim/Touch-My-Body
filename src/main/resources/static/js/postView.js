const postId = $('#forPostId').val();

const addPostLike = () => {
  $.ajax({
    url: '/community/post/'+postId+'/addLike',
    method: 'POST'
  }).done(function(data, textStatus, xhr) {
    if(xhr.status == 200) {
      location.href = '/community/post/'+postId;
    } else {
      alert('에러 발생!' + xhr.status);
    }
  });
}

const addCommentLike = (commentId) => {
  $.ajax({
      url: '/community/post/'+postId+'/comment/addlike',
      method: 'POST',
      data: {"commentId":commentId}
  }).done(function(data,textStatus, xhr) {
    if(xhr.status == 200) {
      location.href = '/community/post/'+postId;
    } else {
      alert('에러 발생!' + xhr.status);
    }
  })
}

const needLogin = () => {
  alert('로그인이 필요합니다.');
}
