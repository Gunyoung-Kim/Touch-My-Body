var exercisesInfo =[];

function targetChange(target) {
  $('#exerciseSelect').empty();

  for(let i=1;i<exercisesInfo[target].length;i++) {
    $('#exerciseSelect').append(`<option value="${exercisesInfo[target][i]}">${exercisesInfo[target][i]}</option>`);
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
          $('#categorySelect').append(`<option value="${i}">${this.target}</option>`);
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


$('#content-area').keyup(function (e){
var content = $(this).val();
$('#counter').html("("+content.length+" / 최대 2500자)");

if (content.length > 2500){
    alert("최대 2500자까지 입력 가능합니다.");
    $(this).val(content.substring(0, 2500));
    $('#counter').html("(2500 / 최대 2500자)");
}
});
