<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>认证界面</title>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<link href="CSS/style.css" type="text/css" rel="stylesheet">
</head>
<body>

<div class="container">
    	<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-login">
					<div class="panel-heading">
						<div class="row">
							<div class=" center">
								<a href="forgetPwd" class="active" id="login-form-link">Verify</a>
							</div>
						</div>
						<hr>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								
								<form id="register-form" action="doverify" method="post" role="form">
									<input type="hidden" name="hidden" id="hidden" value="${txt}">
									<h4>请对以下文本进行私钥解密：</h4>
									<h4><span style="white-space:pre-wrap;word-wrap:break-word;color:red">${encrypttxt}</span></h4><br>
								
									<div class="form-group">
										<input type="text" name="information" id="information" tabindex="2" class="form-control" placeholder="输入解密结果" required>
									</div>
									
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="verify-submit" id="verify-submit" tabindex="4" class="form-control btn btn-register" value="Submit">
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>