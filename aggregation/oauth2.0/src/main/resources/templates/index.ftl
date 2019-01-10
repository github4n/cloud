<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="description" content="">
  <meta name="keywords" content="">
  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <title>${companyName!}</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <link rel="stylesheet" href="../oauth/css/amazeui.min.css">
  <link rel="stylesheet" href="../oauth/css/app.css">
</head>
<body>
<div class="am-g myapp-login">
	<div class="myapp-login-logo-block">
		<div class="myapp-login-logo-text">
			<div class="myapp-login-logo-text">
				${companyName!}<span>Sync</span> <i class="am-icon-skyatlas"></i>
			</div>
		</div>

		<div class="am-u-sm-10 login-am-center">
			<form class="am-form" id="formId" action="/oauth/login" method="post">
				<input type="hidden" name="uuid" value="${uuid!}"/>
				<fieldset>
					<div class="am-form-group">
						<input type="email" class="" id="doc-ipt-email-1" required="required" placeholder="enter email address" name="userName">
					</div>
					<div class="am-form-group">
						<input type="password" class="" id="doc-ipt-pwd-1" required="required" placeholder="enter password" name="password">
					</div>
					<p><button type="submit" class="am-btn am-btn-default" id="button">SIGN IN</button></p>
				</fieldset>
				
			</form>
		</div>
	</div>
</div>
<!--[if (gte IE 9)|!(IE)]><!-->
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
<!--<![endif]-->
<!--[if lte IE 8 ]>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<script src="assets/js/amazeui.ie8polyfill.min.js"></script>
<![endif]-->
</body>
</html>