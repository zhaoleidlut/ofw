<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>

<script type="text/javascript" charset="UTF-8"
	src="../jquery-easyui-1.2.6/jquery-1.7.1.js"></script>

<link id="index_easyuiTheme" rel="stylesheet" type="text/css"
	href="../jquery-easyui-1.2.6/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../jquery-easyui-1.2.6/themes/icon.css">
<script type="text/javascript" charset="UTF-8"
	src="../jquery-easyui-1.2.6/jquery.easyui.min.js"></script>
<script type="text/javascript" charset="UTF-8"
	src="../jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript">
$(function(){
	$('#login-form').form({
		success:function(data){
			$.messager.alert('消息', data.success, 'info');
			$('#login').dialog('close');
		}
	});
});
	</script>

</head>
<body>
	<div id="login" class="easyui-dialog" title="登录" buttons="#dlg-buttons"
		closable="false">
		<form id="login-form" method="post" action="login.html">
			<table style="margin: 10px;">
				<tr>
					<td align="right">用户名：</td>
					<td><input name="username" class="easyui-validatebox"
						required="true" style="width: 150px;" /></td>
				</tr>
				<tr>
					<td align="right">密码：</td>
					<td><input name="password" type="password"
						class="easyui-validatebox" required="true" style="width: 150px;" /></td>
				</tr>
			</table>
		</form>

	</div>

	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok"
			onclick="javascript:$('#login-form').submit()">确定</a>
	</div>

</body>
</html>