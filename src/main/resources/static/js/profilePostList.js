const deletePost = (postId) => {
  $.ajax({
    url: '/user/profile/myposts/remove',
    method: 'DELETE',
    data: {"postId": postId},
    error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data,textStatus, xhr) {
    if(xhr.status == 200) {
      alert("게시글 삭제 완료: " +postId);
    } else {
      alert("오류 발생!" + xhr.status);
    }
    location.href = '/manager/usermanage/' + userId + '/posts';
  })
}

const goToPost = (postId) => {
  location.href = "/community/post/" + postId;
}

const modifyPost = (postId) => {
  location.href = '';
}
