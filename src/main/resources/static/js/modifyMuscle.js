const modifyMuscle = (muscleId) => {
  let name = $('#muscleName').val();
  var category = $('#muscleCategory option:selected').val();
  $.ajax({
    url: '/manager/muscle/modify/' + muscleId,
    method: 'PUT',
    data: {"name" : name, "category" : category},
    error: function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data,textStatus,xhr){
    if(xhr.status == 200) {
      alert('수정 완료!');
    }

    location.href = '/manager/muscle';
  })
}
