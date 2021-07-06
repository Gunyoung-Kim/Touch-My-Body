const reflectFeedback = (feedbackId) => {
  $.ajax({
    url: '/manager/exercise/feedback/reflect/' + feedbackId,
    method: 'PATCH',

    error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data,textStatus,xhr) {
    if(xhr.status == 200) {
      alert('반영 완료!')
    }

    location.href = '/manager/exercise';
  })
}
