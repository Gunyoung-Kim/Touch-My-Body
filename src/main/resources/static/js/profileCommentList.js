const deleteComment = (commentId) => {
  $.ajax({
    url: '/user/profile/mycomments/remove',
    method: 'DELETE',
    data: {"commentId" : commentId},
    error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data, textStatus, xhr) {
    if(xhr.status == 200) {
      alert("댓글 삭제 완료!");
    } else {
    }

    location.href = '/manager/usermanage/'+ userId+'/comments';
  })
}
