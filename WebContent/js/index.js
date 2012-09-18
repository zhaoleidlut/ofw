$(function() {
	$('#north_region').hide();
	$('#west_region').hide();
	$('#center_region').hide();
	$('#south_region').hide();

	$('#login-form').form(
			{
				url : 'login.html',
				success : function(data) {
					var jsonData = eval('(' + data + ')');// 转换成json对象

					if (jsonData.success) {
						$.messager.alert('信息', '登录成功！欢迎您' + jsonData.username
								+ '!', 'info');
						$('#login').dialog('close');

						$('#north_region').show();
						$('#west_region').show();
						$('#center_region').show();
						$('#south_region').show();

						$('#center_region').attr("title",
								jsonData.username + "  欢迎使用本系统！");

						setInterval(function() {
							gzzdRealtime();
							}, 1000 * 600);
						

						$('body').layout();

					} else {
						$.messager.alert('信息', "用户名或密码错误！", 'info');
						$('#login').find('[name=password]').val('');
						$('#login').find('[name=password]').focus();
					}
				}
			});

	// var user = '<%=session.getAttribute("user")%>';
	// alert(user);
	var user = $("#user").val();
	// alert(user);

	if (user != 'null' && user != '') {
		$('#login').dialog('close');

		$('#north_region').show();
		$('#west_region').show();
		$('#center_region').show();
		$('#south_region').show();

		$('#center_region').attr("title", user + "  欢迎使用本系统！");

		setInterval(function() {gzzdRealtime();}, 1000 * 600);

		$('body').layout();
	}
	


});

function confirmGzzd(index) {
	var wellNum = $('#gzzd_thm_grid').datagrid('getData').rows[index].wellNum;
	
	$.ajax({
		async : false,
		url : 'confirmGzzd.html',
		type : 'post',
		cache : false,
		dataType : 'json',
		data : 'wellNum=' + wellNum,
		success : function(data) {
			$('#gzzd_thm_grid').datagrid('reload');
		}
	});

}

function gzzdRealtime() {
	$('#gzzd_thm_grid').datagrid({
		// iconCls : 'icon-save',
		autoRowHeight : true,
		nowrap : true,
		// striped: true,
		collapsible : true,
		url : 'getRealtimeFaultStatus.html',
		// sortName : 'time',
		remoteSort : false,
		
		// sortName : '123',
		// sortOrder : '456',
		// queryParams : {wellNum:wellNum},
		columns : [ [ {
			field : 'wellNum',
			title : '井号',
			width : 70
		}, {
			field : 'dtuNum',
			title : 'DTU号',
			width : 65
		}, {
			field : 'faultCodeValue',
			title : '故障类型',
			width : 80
		}, {
			field : 'faultLevel',
			title : '故障程度',
			width : 58
		}, {
			field : 'gzzdTime',
			title : '诊断时间',
			width : 130
		}, {
			field : 'deviceTime',
			title : '设备时间',
			width : 130
		}, {	
			field : 'confirm',
			title : '确认操作',
			width : 65,
			formatter: function(value,row,index){
				var c = '<input type="button" value="确认" onClick="confirmGzzd('+index+')"></input>';
				return c;
			}
		} ] ],
		pageSize : 10,
		pageList : [ 15, 20, 30, 40, 50 ],
		pagination : true,
		rownumbers : true
	});
	var idata = $('#gzzd_thm_grid').datagrid('getData');

	if (idata != null && idata.total > 0) {
		$('#gzzd_thm').window('open');
	}
}





