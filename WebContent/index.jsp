<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>油田自动化管理系统</title>


<script type="text/javascript" charset="UTF-8"
	src="jquery-easyui-1.2.6/jquery-1.7.1.js"></script>
<script language="javascript" type="text/javascript" src="jqplot/excanvas.js"></script>

<link id="index_easyuiTheme" rel="stylesheet" type="text/css"
	href="jquery-easyui-1.2.6/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.2.6/themes/icon.css">
<script type="text/javascript" charset="UTF-8"
	src="jquery-easyui-1.2.6/jquery.easyui.min.js"></script>
<script type="text/javascript" charset="UTF-8"
	src="jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>


<script type="text/javascript" src="jqplot/jquery.jqplot.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="jqplot/jquery.jqplot.min.css" />
<script type="text/javascript" src="jqplot/plugins/jqplot.json2.min.js"></script>
<script type="text/javascript" src="jqplot/plugins/jqplot.cursor.min.js"></script>
<script type="text/javascript"
	src="jqplot/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript"
	src="jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>
<script type="text/javascript"
	src="jqplot/plugins/jqplot.highlighter.min.js"></script>

<!-- 	
<script type="text/javascript" charset="UTF-8"
	src="js/util.js"></script>   -->

<script type="text/javascript">
	$(function() {
		$('#north_region').hide();
		$('#west_region').hide();
		$('#center_region').hide();
		$('#south_region').hide();

		$('#login-form').form(
				{
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

							$('body').layout();
						} else {
							$.messager.alert('信息', "用户名或密码错误！", 'info');
							$('#login').find('[name=password]').val('');
							$('#login').find('[name=password]').focus();

						}

					}
				});
	});
</script>

</head>
<body>


	<div region="north" id="north_region" href="layout/north.htm"
		style="height: 50px; overflow: hidden; background-color: lightcyan;"></div>
	<div region="west" id="west_region" href="layout/west.htm"
		iconCls="icon-tip" split="true" title="导航菜单" style="width: 200px;"></div>
	<div region="center" id="center_region" title="欢迎使用本系统！">
		<div id="index_tabs" class="easyui-tabs" fit="true">
			<div id="home" title="首页" href="layout/home.htm"></div>
		</div>
	</div>
	<div region="south" id="south_region"
		style="height: 18px; text-align: center;">版权所有&nbsp;丹东华通测控有限公司</div>


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