const goToAdd = () => {
  location.href = '/manager/muscle/add';
}

const goToModify = (muscleId) => {
  location.href = '/manager/muscle/modify/' + muscleId;
}

const goToDelete = (muscleId) => {
  $.ajax({
    url: '/manager/muscle/remove',
    method: 'DELETE',
    data: {"muscleId": muscleId}
  }).done(function(data,textStatus,xhr){
    if(xhr.status == 200) {
      location.href = '/manager/muscle';
    } else {
      alert("오류 발생 ! : " +xhr.status);
      location.href = '/manager/muscle';
    }
  })
}
