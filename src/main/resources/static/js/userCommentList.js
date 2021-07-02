const deleteComment = (userId,commentId) => {
  $.ajax({
    url: '/manager/usermanage/'+ userId+'/comments/remove',
    method: 'DELETE',
    data: {"comment_id": commentId}
  }).done(function(data, textStatus, xhr) {
    if(xhr.status == 200) {
      location.href = '/manager/usermanage/'+ userId+'/comments';
    } else {
      alert("오류 발생!" + xhr.status);
      location.href = '/manager/usermanage/'+ userId+'/comments';
    }
  })
}
