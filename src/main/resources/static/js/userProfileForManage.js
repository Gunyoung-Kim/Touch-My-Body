const goToUserComments = (userId) => {
  location.href = '/manager/usermanage/' + userId +'/comments';
}

const goToUserPosts = (userId) => {
  location.href = '/manager/usermanage/' + userId + '/posts';
}

const modifyUserProfile = (userId) => {
  let role = $('#roleSelect option:selected').val();

  $.ajax({
    url: '/manager/usermanage/' + userId,
    method: 'PUT',
    data: {"role": role},
    error: function(request,status,error) {
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data,textStatus,xhr) {
    if(xhr.status== 200) {
      alert('변경 완료.');
    }

    location.href= '/manager/usermanage/'+userId;
  })
}
