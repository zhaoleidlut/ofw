$(function() {
		$('#north_region').hide();
		$('#west_region').hide();
		$('#center_region').hide();
		$('#south_region').hide();
		
		$('#login-form').form({
				url:'login.html',
				success : function(data) {
					var jsonData = eval('(' + data + ')');//转换成json对象

					if (jsonData.success) {
						$.messager.alert('信息', '登录成功！欢迎您'
								+ jsonData.username + '!', 'info');
						$('#login').dialog('close');

						$('#north_region').show();
						$('#west_region').show();
						$('#center_region').show();
						$('#south_region').show();

						$('#center_region').attr("title",
								jsonData.username + "  欢迎使用本系统！");
								
						setInterval(function() {
							alert('hello world');
						}, 1000*600);

						$('body').layout();
						
						
						
					} else {
						$.messager.alert('信息', "用户名或密码错误！", 'info');
						$('#login').find('[name=password]').val('');
						$('#login').find('[name=password]').focus();
					}
				}
		});
		
		
		
		//var user = '<%=session.getAttribute("user")%>';
		//alert(user);
		var user = $("#user").val();
		//alert(user);
		
		if (user != 'null' && user != '') {
			$('#login').dialog('close');

			$('#north_region').show();
			$('#west_region').show();
			$('#center_region').show();
			$('#south_region').show();

			$('#center_region').attr("title", user + "  欢迎使用本系统！");
			
			setInterval(function() {
				alert('hello world111111');
			}, 1000*600);

			$('body').layout();
		}
	});