var exercisesInfo =[];

function targetChange(target) {
  $('#exercises').empty();

  for(let i=1;i<exercisesInfo[target].length;i++) {
    $('#exercises').append(`<option value="${exercisesInfo[target][i]}">${exercisesInfo[target][i]}</option>`);
  }
}

const getExercisesNameAndTarget = () => {
  $.ajax({
    url: '/user/exercise/calendar/addrecord/getexercises',
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
