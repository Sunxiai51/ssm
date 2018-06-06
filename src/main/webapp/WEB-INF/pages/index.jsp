<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./_head.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
<title>首页</title>
</head>
<body>
	<div>
		<h3>通过链接访问示例</h3>
		<ul>
			<li><h4><a href="${ctx}/user/list" target="_blank">列表&#38;分页</a>&nbsp;<small>通过设置pageSize的值调整每页个数</small></h4></li>
			<li><h4><a href="${ctx}/file/operate" target="_blank">文件操作</a>&nbsp;<small>上传与下载</small></h4></li>
		</ul>
	</div>
</body>
</html>