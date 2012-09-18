function addTab(title) {
	if(title == '实时示功图查询') {
		var tab = $('#index_tabs').tabs('getTab','实时示功图查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '实时示功图查询',
			href : 'sgt/realtime_sgt_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','实时示功图查询');
		}	
	} else if(title == '历史示功图查询') {
		var tab = $('#index_tabs').tabs('getTab','历史示功图查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '历史示功图查询',
			href : 'sgt/history_sgt_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','历史示功图查询');
		}
		
	} else if(title == '多井示功图对比') {
		var tab = $('#index_tabs').tabs('getTab','多井示功图对比');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '多井示功图对比',
			href : 'sgt/sgt_comparison_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','多井示功图对比');
		}
		
	} else if(title == '示功图时间对比') {
		var tab = $('#index_tabs').tabs('getTab','示功图时间对比');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '示功图时间对比',
			href : 'sgt/sgt_comparison_singlewell_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','示功图时间对比');
		}
		
	}  else if(title == '日产量查询') {
		var tab = $('#index_tabs').tabs('getTab','日产量查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '日产量查询',
			href : 'oil_product/day_product_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','日产量查询');
		}
		
	} else if(title == '时间段产量查询') {
		var tab = $('#index_tabs').tabs('getTab','时间段产量查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '时间段产量查询',
			href : 'oil_product/custom_day_product_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','时间段产量查询');
		}
		
	} else if(title == '分离器对比') {
		var tab = $('#index_tabs').tabs('getTab','分离器对比');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '分离器对比',
			href : 'oil_product/flq_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','分离器对比');
		}
		
	} else if(title == '示功图电功图对比') {
		var tab = $('#index_tabs').tabs('getTab','示功图电功图对比');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '示功图电功图对比',
			href : 'sgt_dgt/sgt_dgt_comparison_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','示功图电功图对比');
		}
		
	} else if(title == '实时电功图查询') {
		var tab = $('#index_tabs').tabs('getTab','实时电功图查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '实时电功图查询',
			href : 'dgt/realtime_dgt_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','实时电功图查询');
		}	
	} else if(title == '历史电功图查询') {
		var tab = $('#index_tabs').tabs('getTab','历史电功图查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '历史电功图查询',
			href : 'dgt/history_dgt_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','历史电功图查询');
		}
		
	} else if(title == '多井电功图对比') {
		var tab = $('#index_tabs').tabs('getTab','多井电功图对比');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '多井电功图对比',
			href : 'dgt/dgt_comparison_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','多井电功图对比');
		}
		
	} else if(title == '电功图时间对比') {
		var tab = $('#index_tabs').tabs('getTab','电功图时间对比');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '电功图时间对比',
			href : 'dgt/dgt_comparison_singlewell_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','电功图时间对比');
		}
		
	} else if(title == '井矿诊断') {
		var tab = $('#index_tabs').tabs('getTab','井矿诊断');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '井矿诊断',
			href : 'sys/gzzd_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','井矿诊断');
		}
		
	} else if(title == '月产量统计') {
		var tab = $('#index_tabs').tabs('getTab','月产量统计');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '月产量统计',
			href : 'oil_product/product_month.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','月产量统计');
		}
		
	} else if(title == '年产量统计') {
		var tab = $('#index_tabs').tabs('getTab','年产量统计');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '年产量统计',
			href : 'oil_product/product_year.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','年产量统计');
		}
		
	} else if(title == '电能数据查询') {
		var tab = $('#index_tabs').tabs('getTab','电能数据查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '电能数据查询',
			href : 'elec/energy_data_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','电能数据查询');
		}
		
	} else if(title == '电力数据查询') {
		var tab = $('#index_tabs').tabs('getTab','电力数据查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '电力数据查询',
			href : 'elec/elec_data_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','电力数据查询');
		}
		
	} else if(title == 'DTU状态') {
		var tab = $('#index_tabs').tabs('getTab','DTU状态');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : 'DTU状态',
			href : 'dtu/elec_data_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','DTU状态');
		}
		
	}  else if(title == '故障历史记录') {
		var tab = $('#index_tabs').tabs('getTab','故障历史记录');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '故障历史记录',
			href : 'sys/gzzd_history_record.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','故障历史记录');
		}
		
	}
}
