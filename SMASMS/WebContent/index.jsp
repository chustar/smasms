<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
    /*
     * AWS Elastic Beanstalk checks your application's health by periodically
     * sending an HTTP HEAD request to a resource in your application. By
     * default, this is the root or default resource in your application,
     * but can be configured for each environment.
     *
     * Here, we report success as long as the app server is up, but skip
     * generating the whole page since this is a HEAD request only. You
     * can employ more sophisticated health checks in your application.
     */
    if (request.getMethod().equals("HEAD")) return;
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>SMASMS - Stock Mood Analysis using Social Media Streams</title>
    <link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
</head>
<body>
	<img class="centered-block" src="/images/smasms.png" alt="SMASMS - Stock Mood Analysis using Social Media Streams" style="width: 432px; height: 73px"/>
</body>
</html>