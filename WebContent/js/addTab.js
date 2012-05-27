function addTab(title) {
	if(title == '实时示功图查询') {
		var tab = $('#index_tabs').tabs('getTab','实时示功图查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '实时示功图查询',
			href : 'tabpanel/realtime_sgt_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','实时示功图查询');
		}	
	} else if(title == '日产量查询') {
		var tab = $('#index_tabs').tabs('getTab','日产量查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '日产量查询',
			href : 'tabpanel/day_product_query.htm',
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
			href : 'tabpanel/custom_day_product_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','时间段产量查询');
		}
		
	} else if(title == '历史示功图查询') {
		var tab = $('#index_tabs').tabs('getTab','历史示功图查询');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '历史示功图查询',
			href : 'tabpanel/history_sgt_query.htm',
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
			href : 'tabpanel/sgt_comparison_query.htm',
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
			href : 'tabpanel/sgt_comparison_singlewell_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','示功图时间对比');
		}
		
	} else if(title == '示功图电功图对比') {
		var tab = $('#index_tabs').tabs('getTab','示功图电功图对比');
		if(typeof(tab) == 'undefined' || tab == null) {
			$('#index_tabs').tabs('add', {
			title : '示功图电功图对比',
			href : 'tabpanel/sgt_dgt_comparison_query.htm',
			closable : true
			});
		} else {
			$('#index_tabs').tabs('select','示功图电功图对比');
		}
		
	}
}