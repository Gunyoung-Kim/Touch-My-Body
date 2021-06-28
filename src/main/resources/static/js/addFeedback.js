$('#content-area').keyup(function (e){
var content = $(this).val();
$('#counter').html("("+content.length+" / 최대 2500자)");

if (content.length > 2500){
    alert("최대 2500자까지 입력 가능합니다.");
    $(this).val(content.substring(0, 2500));
    $('#counter').html("(2500 / 최대 2500자)");
}
});
