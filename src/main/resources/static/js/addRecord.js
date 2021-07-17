var exercisesInfo =[];

function targetChange(target) {
  $('#exerciseName').empty();

  for(let i=1;i<exercisesInfo[target].length;i++) {
    $('#exerciseName').append(`<option value="${exercisesInfo[target][i]}">${exercisesInfo[target][i]}</option>`);
  }
}

const getExercisesNameAndTarget = () => {
  $.ajax({
    url: '/user/exercise/getexercises',
    method: 'GET',

    success:function(data) {
      console.log(data);
      if(data.length==0) {
        console.log("결과가 없습니다.");
      } else {
        let i = 0;
        $.each(data, function() {
          exercisesInfo[i] = [this.target];
          $('#target').append(`<option value="${i}">${this.target}</option>`);
          const exercises = this.exerciseNames;
          exercisesInfo[i] = exercisesInfo[i].concat(exercises);
          console.log(exercisesInfo[i]);
          i++;
        })
      }
    }
  })
}

$(document).ready(getExercisesNameAndTarget());

const add = () => {
  let exerciseName = $('#exerciseName option:selected').val();
  let sets = $('#sets').val();
  let laps = $('#laps').val();
  let weight = $('#weight').val();
  let description = $('#description').val();
  let date = $('#date').val();

  $.ajax({
      url: '/user/exercise/calendar/addrecord',
      method: 'POST',
      data: {"laps":laps , "sets":sets, "weight":weight, "description":description, "date":date,
        "exerciseName":exerciseName},
      error:function(request,status,error){
        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      }
  }).done(function(data,textStatus,xhr) {
    if(xhr.status == 200) {
      alert('운동 기록 추가 완료되었습니다.');
    }

    location.href = '/user/exercise/calendar/addrecord';
  })
}
