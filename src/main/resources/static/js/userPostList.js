const deletePost = (userId,postId) => {
  $.ajax({
    url: '/manager/usermanage/' + userId + '/posts/remove',
    method: 'DELETE',
    data: {"post_id" : postId},

    error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data,textStatus, xhr) {
    if(xhr.status == 200) {

    }
    location.href = '/manager/usermanage/' + userId + '/posts';
  })
}

const goToPost = (postId) => {
  location.href = "/community/post/" + postId;
}
