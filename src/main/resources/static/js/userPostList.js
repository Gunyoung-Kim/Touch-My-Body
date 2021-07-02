const deletePost = (userId,postId) => {
  $.ajax({
    url: '/manager/usermanage/' + userId + '/posts/remove',
    method: 'DELETE',
    data: {"post_id" : postId}
  }).done(function(data,textStatus, xhr) {
    if(xhr.status == 200) {
      location.href = '/manager/usermanage/' + userId + '/posts';
    } else {
      alert("오류 발생!" + xhr.status);
      location.href = '/manager/usermanage/' + userId + '/posts';
    }
  })
}

const goToPost = (postId) => {
  location.href = "/community/post/" + postId;
}
