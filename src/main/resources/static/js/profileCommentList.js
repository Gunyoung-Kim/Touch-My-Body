const deleteComment = (commentId) => {
  $.ajax({
    url: '/user/profile/mycomments/remove',
    method: 'DELETE',
    data: {"commentId" : commentId}
  }).done(function(data, textStatus, xhr) {
    if(xhr.status == 200) {
      location.href = '/manager/usermanage/'+ userId+'/comments';
    } else {
      alert("오류 발생!" + xhr.status);
      location.href = '/manager/usermanage/'+ userId+'/comments';
    }
  })
}
