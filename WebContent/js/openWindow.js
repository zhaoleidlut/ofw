function openWin(winTitle) {
	if(winTitle == '实时诊断状态') {
		$('#gzzd_real_status').window('open');
		$('#gzzd_real_status_grid').datagrid({

			//iconCls : 'icon-save',
			autoRowHeight : true,
			nowrap : true,
			//striped: true,
			collapsible : true,
			url : 'getGzzdRealtimeStatus.html',
			//sortName : 'time',
			remoteSort : false,
			toolbar : '#tb',
			//sortName : '123',
			//sortOrder : '456',
			//queryParams : {wellNum:wellNum},

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
				width : 65
			}, {
				field : 'faultFlagValue',
				title : '故障状态',
				width : 100,
				sortable : true
			}, {
				field : 'hasConfirmValue',
				title : '是否确认',
				width : 60,
				sortable : true
			}, {
				field : 'gzzdTime',
				title : '诊断时间',
				width : 130
			}, {
				field : 'deviceTime',
				title : '设备时间',
				width : 130
			} ] ],
			pageSize : 15,
			pageList : [ 15, 20, 30, 40, 50 ],
			pagination : true,
			rownumbers : true,
			toolbar : [ {		
				text : '刷新',
				iconCls : 'icon-reload',
				handler : function() {
					$('#gzzd_real_status_grid').datagrid('reload', {});
				}
			} ]
		});
	}
}