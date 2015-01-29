<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ACME-500</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div id="header_wrapper">
    <div id="header">
        <h1><a href="#" target="_parent">ACME</a></h1>
    </div>
</div>

<div id="sidebar">
    <div id="menu">
        <ul class="navigation">
            <li><a href="<%=request.getContextPath()%>/home" id="home">Home</a></li>
            <li><a href="<%=request.getContextPath()%>/newspapers" id="newspapers">Newspapers</a></li>
            <li><a href="<%=request.getContextPath()%>/advertisements" id="advertisements">Advertisements</a></li>
            <li><a href="<%=request.getContextPath()%>/aboutus" id="aboutus">About Us</a></li>
            <li><a href="<%=request.getContextPath()%>/contact" id="contact">Contact</a></li>
        </ul>
    </div>
</div>

<div id="content">
    <div class="scroll">
        <div class="scrollContainer">
            <div class="panel" id="aboutus">
                <h2>Oops...</h2>

                <div class="simple_message"> Something Went Terribly Wrong !!!</div>
            </div>
        </div>
    </div>
    <div class="cleaner"></div>
</div>

<div id="footer">
    Copyright &copy; 2014 <a href="#">ACME</a><br/>

    <div class="cleaner"></div>
</div>
</body>
</html>