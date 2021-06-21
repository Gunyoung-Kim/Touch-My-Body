var emailData;
var nickNameData;
$(document).ready(function() {
  $('#submitButton').attr("disabled",true);
  emailVeriPleaseMessage();
  nickNameVeriPleaseMessage();
});

function emailChange() {
  $('#submitButton').attr("disabled",true);
  emailVeriPleaseMessage();
}

function emailCheck() {
  email = $("#userEmail").val();
  if(email == "") {
    alert("이메일을 입력해주세요.");
    return;
  }
  $.ajax({
    url: 'join/emailverification?email='+email,
    dataType: 'text',
    contentType: 'text/plain; charset=utf-8;',

    success:function(data){
      emailData = data;
      if(data == 'true') {
        alert("이미 등록된 이메일입니다.");
      } else {
        alert("사용 가능한 이메일입니다.");
        emailVeriCompleteMessage();
        if(emailData == 'false' && nickNameData == 'false') {
          $('#submitButton').attr("disabled",false);
        }
      }
    },
    error:function() {
      console.log("error");
    }
  });
}

function emailVeriPleaseMessage() {
  $('#emailHelp').text("이메일 중복확인 해주세요.");
}

function emailVeriCompleteMessage() {
  $('#emailHelp').text("이메일 중복확인 완료.");
}

//닉네임 확인

function nickNameChange() {
  $('#submitButton').attr("disabled",true);
  nickNameVeriPleaseMessage();
}

function nickNameCheck() {
  nickName = $("#userNickName").val();
  if(nickName == "") {
    alert("닉네임을 입력해주세요.");
    return;
  }
  $.ajax({
    url: 'join/nickNameverification?nickName='+nickName,
    dataType: 'text',
    contentType: 'text/plain; charset=utf-8;',

    success:function(data){
      nickNameData = data;
      if(data == 'true') {
        alert("이미 등록된 닉네임입니다.");
      } else {
        alert("사용 가능한 닉네임입니다.");
        nickNameVeriCompleteMessage();
        if(emailData == 'false' && nickNameData == 'false') {
          $('#submitButton').attr("disabled",false);
        }
      }
    },
    error:function() {
      console.log("error");
    }
  });
}

function nickNameVeriPleaseMessage() {
  $('#nickNameHelp').text("닉네임 중복확인 해주세요.");
}

function nickNameVeriCompleteMessage() {
  $('#nickNameHelp').text("닉네임 중복확인 완료.");
}
