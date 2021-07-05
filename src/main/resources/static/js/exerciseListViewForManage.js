const goToAdd = () => {
    location.href = '/manager/exercise/add';
}

const goToModify = (exerciseId) => {
    location.href = '/manager/exercise/modify/' + exerciseId;
}

const goToDelete = (exerciseId) => {
  $.ajax({
    url: '/manager/exercise/remove',
    method: 'DELETE',
    data: {"exerciseId": exerciseId}
  }).done(function(data,textStatus,xhr) {
    if(xhr.status == 200) {
      location.href = '/manager/exercise';
    } else {
      alert("오류 발생 ! : " +xhr.status);
      location.href = '/manager/exercise';
    }
  })
}
