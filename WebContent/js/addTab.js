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
		
		
	}
		
	}
