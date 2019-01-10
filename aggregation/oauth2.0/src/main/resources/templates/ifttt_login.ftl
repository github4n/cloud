<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="description" content="">
  <meta name="keywords" content="">
  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <title>Arnoo Sign In</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <link rel="stylesheet" href="../css/amazeui.min.css">
  <link rel="stylesheet" href="../css/app.css">
</head>
<body>
<div class="am-g myapp-login">
	<div class="myapp-login-logo-block">
		<div class="myapp-login-logo-text">
			<div class="myapp-login-logo-text">
				Arnoo<span>Sync</span> <i class="am-icon-skyatlas"></i>
			</div>
		</div>

		<div class="am-u-sm-10 login-am-center">
			<form class="am-form" id="formId">
				<input type="hidden" name="state" value="${state!}" id="state"/>
			    <input type="hidden" name="redirectUrl" value="${redirectUrl!}" id="redirectUrl"/>
			    <input type="hidden" name="tenantId" value="${tenantId!}" id="tenantId"/>
				<fieldset>
					<div class="am-form-group">
						<input type="email" class="" id="userName" required="required" placeholder="enter email" name="userName">
					</div>
					<div class="am-form-group">
						<input type="password" class="" id="password" required="required" placeholder="enter password" name="password">
					</div>
					<p><input type="checkbox" name="check"> <span style="color:#FFF;font-size:14px">Authorize IFTTT control of connected smart home and accept your devices information transfer to IFTTT</span></p>
					<p><button type="button" class="am-btn am-btn-default" id="button" onclick="dologin()">SIGN IN</button></p>
				</fieldset>

			</form>
		</div>
	</div>
</div>
<!--[if (gte IE 9)|!(IE)]><!-->
<script src="../js/jquery.min.js"></script>
<script src="../js/md5.js"></script>
<!--<![endif]-->
<!--[if lte IE 8 ]>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<script src="assets/js/amazeui.ie8polyfill.min.js"></script>
<![endif]-->
<script type="text/javascript">
	function dologin(){
		var name = document.getElementById("userName").value;
		var pwd = document.getElementById("password").value;
		var state = document.getElementById("state").value;
		var redirectUrl = document.getElementById("redirectUrl").value;
		var tenantId = document.getElementById("tenantId").value;
		var checkArray = document.getElementsByName('check');
        var value = checkArray[0].checked;
        var flag = 0;
        if(value){
            flag = 1;
        }
		pwd = hex_md5(pwd);
		window.location.href = '/oauth2/login?userName='+name+'&password='+pwd+'&state='+state+'&redirectUrl='+redirectUrl+'&flag='+flag+'&tenantId='+tenantId;
	}
</script>
</body>
</html>