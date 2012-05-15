$(function(){
	$.parser.onComplete = function() {/* 页面所有easyui组件渲染成功后，隐藏等待信息 */
		window.setTimeout(function() {
			$.messager.progress('close');
		}, 1000);
	};
})
