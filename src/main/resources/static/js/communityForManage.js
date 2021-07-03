const goToPost = (postId) => {
  location.href = '/community/post/' + postId;
}

const deletePost = (postId) => {
  $.ajax({
    url: '/manager/community/remove/post',
    method: 'DELETE',
    data: {"postId": postId}
  }).done(function(data,textStatus,xhr) {
    if(xhr.status == 200) {
      alert("삭제완료되었습니다.");
    } else {
      alert("오류 발생! : " +xhr.status);
    }
    location.href = '/manager/community'
  })
}
