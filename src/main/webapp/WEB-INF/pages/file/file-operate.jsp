<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../_head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>文件操作</title>
</head>
<body>

	<form action="${ctx}/file/upload" method="post" enctype="multipart/form-data">
		选择文件:<input type="file" name="file" width="120px"> <input type="submit" value="上传">
	</form>
	<hr>
	<form action="${ctx}/file/download" method="post">
		<input type="hidden" name="fileName" value="testFile.txt">
		<input type="submit" value="下载">
	</form>

</body>
</html>