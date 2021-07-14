var muscleInfo = [];

var inputNum = 1;

const getMuscleNameAndTarget = () => {
  $.ajax({
    url: '/manager/exercise/getmuscles',
    method: 'GET',

    success:function(data) {
      if(data.length == 0) {
        console.log("결과가 없습니다.");
      } else {
        let i=0;
        $.each(data,function() {
          muscleInfo[i] = [this.target];
          const muscles = this.muscleNames;
          muscleInfo[i] = muscleInfo[i].concat(muscles);
          console.log(muscleInfo[i]);
          i++;
        })
      }
    }
  })
}

const addMuscle = () => {
  var newInput = $(`<div id="inputMuscle${inputNum}"></div>`);

  var isMainSelect = $('<select></select>',{id:`isMain${inputNum}`});
  isMainSelect.append('<option selected disabled>자극의 비중</option>');
  isMainSelect.append('<option value="true">주 자극 근육</option>');
  isMainSelect.append('<option value="false">부 자극 근육</option>');

  var targetSelect = $('<select ></select>',{id: `target${inputNum}`,onchange: `targetChange(${inputNum},this.value)`});
  let i =0;
  targetSelect.append('<option selected disabled>운동 부위 선택</option>');
  $.each(muscleInfo,function() {
    targetSelect.append(`<option value="${i}">${this[0]}</option>`);
    i++;
  })

  var muscleSelect = $('<select></select>',{id: `muscle${inputNum}`});

  muscleSelect.append('<option selected disabled>운동 종류 선택</option>');
  var deleteBtn = $(`<button type="button" onclick="deleteSelect(${inputNum})" class="btn">삭제</button>`)
  inputNum++;

  newInput.append(isMainSelect);
  newInput.append(targetSelect);
  newInput.append(muscleSelect);
  newInput.append(deleteBtn);

  $('#muscleInput').append(newInput);
}

const targetChange = (inputNum, value) => {
  $(`#muscle${inputNum}`).empty();

  for(let i=1;i<muscleInfo[value].length;i++) {
    $(`#muscle${inputNum}`).append(`<option value="${muscleInfo[value][i]}">${muscleInfo[value][i]}</option>`)
  }
}

const deleteSelect = (deleteNum) => {
  $(`#inputMuscle${deleteNum}`).remove();
}

const modifyExercise = (exerciseId) => {
  let name = $('#name').val();
  let description = $('#description').val();
  let movement = $('#movement').val();
  let caution = $('#caution').val();
  let target = $('#target').val();

  let mainMuscles = [];
  let subMuscles = [];

  for(let i=1 ; i <inputNum;i++) {
    let muscleName = $(`#muscle${i}`).val();
    if(muscleName != "") {
      if($(`#isMain${i}`).val() == "true") {
        mainMuscles.push(muscleName);
      } else {
        subMuscles.push(muscleName);
      }
    }
  }

  $.ajax({
    url: '/manager/exercise/modify/' +exerciseId,
    method: 'PUT',
    data: {"name": name, "description" : description, "movement": movement,"caution" :caution, "target" : target,
        "mainMuscles": mainMuscles, "subMuscles":subMuscles},

    error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).done(function(data, textStatus, xhr) {
    if(xhr.status == 200) {
      alert('운동 정보 수정 완료되었습니다.');
    } else {
      alert('운동 정보 수정 에러 발생: ' +xhr.status);
    }

    location.href = '/manager/exercise';
  })

}


  $(document).ready(getMuscleNameAndTarget());
