<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经纬度转换</title>
<script src="http://libs.useso.com/js/jquery/2.1.1-rc2/jquery.min.js"></script>
<style type="text/css">
html body {
	font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;
}
.tab{
	float:left;
	min-width:200px;
	border:1px solid #aaa;
	padding:10px;
	text-align:center;
}
.tab:hover {
	background-color: #ccc;
}
.tab.active {
	background-color: #aaa;
}
.container {	
	margin-top:10px;
}
.geo_text {
	padding:5px;
	font-size:13px;
	width:50%;
}
.geo_button {
	padding:5px 20px;
	font-size:13px;
	text-align:center;
	background-color: #aaa;
	color:#000;
	margin-top:10px;
	margin-bottom:10px;
	display:inline-block;
}
.geo_button:hover {
	background-color:#ccc;
}
.geo_result_container {
	border:1px solid #ccc;
	min-height:500px;
	margin-top: 10px;
}
.geo_result_status {
    background-color: #e60000;
    padding: 5px;
    color: #fff;
    font-size: 13px;
}
.geo_result {
	padding:10px;
}
</style>
</head>
<body>
<div class="tabs">
	<a class="tab" data-type="2">GPS -> Baidu</a>
	<a class="tab active" data-type="1">Baidu -> GPS</a>
</div>

<div style="clear:both"></div>

<div class="container">
	<input class="geo_text" type="text" id="coords" placeholder="请输入经纬度，格式：114.21892734521,29.575429778924;114.21892734521,29.575429778924">
	<div class="geo_button" id="convert">运&nbsp;行</div>
	<div class="geo_result_container">	
		<div class="geo_result_status" id="geo_result_status"></div>
		<div class="geo_result" id="geo_result"></div>
	</div>
</div>
<script type="text/javascript">
	$("#geo_result_status").hide();

	$("#convert").click(function() {
		$("#geo_result").text("");
		var type  = $(".tab.active").attr("data-type");
		$.ajax({
			type: 'POST',
			url: "IndexServlet",
			dataType: "json",
			data: {coords: $("#coords").val(), type: type},
			success: function(data) {
				//$("#geo_result").text(data.result);
				if(data.status != 0) {
					$("#geo_result_status").hide();
					$("#geo_result_status").text("转换错误：" + data.message);
					$("#geo_result_status").show(600);
				} else {
					$("#geo_result_status").hide();
				}
				$.each(data.result,function(item){
					$("#geo_result").append("X: " + this.x + " \tY: " + this.y);
					$("#geo_result").append("<br>");
				});
			},
			complete: function() {
				// alert("complete");
			}
		});
	});
	
	$(".tab").click(function() {
		$(".tab").removeClass("active");
		$(this).addClass("active");
	});
</script>
</body>
</html>