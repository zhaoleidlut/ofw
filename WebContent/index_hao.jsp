<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>油井监测管理系统</title>
<jsp:include page="jsp/include.jsp" flush="true"/>

<script type="text/javascript" charset="UTF-8" src="js/index.js"></script>
<script type="text/javascript" charset="UTF-8" src="js/util.js"></script>
</head>

<body>
	<div region="north" id="north_region" href="layout/north.htm"
		style="height: 50px; overflow: hidden; background-color: lightcyan;"></div>
	<div region="west" id="west_region" href="layout/west.htm"
		iconCls="icon-tip" split="true" title="导航菜单" style="width: 190px;"></div>
	<div region="center" id="center_region" title="欢迎使用本系统！">
		<div id="index_tabs" class="easyui-tabs" fit="true">
			<div id="home" title="首页" href="layout/home.htm"></div>
		</div>
	</div>
	<div region="south" id="south_region"
		style="height: 18px; text-align: center;">版权所有&nbsp;丹东华通测控有限公司</div>

	<div id="login" class="easyui-dialog" title="登录" buttons="#dlg-buttons"
		closable="false">
		<form id="login-form" method="post">
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
	
	<input type="hidden" name="user" id="user" value="${user}">

</body>
</html>