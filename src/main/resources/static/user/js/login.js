$(function() {
	$("#check").click (function() {
		if($('#inputEmail').val()=='atbybiu@163.com'&& $('#inputPassword').val()=='123'){
	 		alert('登陆成功！');
 
			return true;
	 	}else {
	 		$('#inputEmail').val('');//清空输入框
	 		$('#inputPassword').val('');
	 		alert('登陆失败！');
 
	 		return false;
	 	}
	 });
	 });