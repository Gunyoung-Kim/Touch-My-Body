const deleteComment = (userId,commentId) => {
  $.ajax({
    url: '/manager/usermanage/'+ userId+'/comments/remove',
    method: 'DELETE',
    data: {"comment_id": commentId},

    error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data, textStatus, xhr) {
    if(xhr.status == 200) {
      alert("댓글 삭제 완료");
    }

    location.href = '/manager/usermanage/'+ userId+'/comments';
  })
}
