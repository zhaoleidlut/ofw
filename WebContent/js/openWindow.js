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
				width : 75
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
			}, {	
				field : 'confirm',
				title : '确认操作',
				width : 65,
				formatter: function(value,row,index){
					var record = $('#gzzd_real_status_grid').datagrid('getData').rows[index];
					var flag = record.faultFlag;
					var sure = record.hasConfirm;
//					alert(flag);
					if(flag && !sure) {
						var c = '<input type="button" value="确认" onClick="confirmGzzdHistory('+index+')"></input>';
						return c;
					} else {
						return '';
					}
				}
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

function confirmGzzdHistory(index) {
	var wellNum = $('#gzzd_real_status_grid').datagrid('getData').rows[index].wellNum;
	
	$.ajax({
		async : false,
		url : 'confirmGzzd.html',
		type : 'post',
		cache : false,
		dataType : 'json',
		data : 'wellNum=' + wellNum,
		success : function(data) {
			$('#gzzd_real_status_grid').datagrid('reload');
		}
	});

}