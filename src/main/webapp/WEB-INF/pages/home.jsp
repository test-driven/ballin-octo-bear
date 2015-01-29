<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ACME-Home</title>
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
            <li><a href="home" id="home" class="selected">Home</a></li>
            <li><a href="newspapers" id="newspapers">Newspapers</a></li>
            <li><a href="advertisements" id="advertisements">Advertisements</a></li>
            <li><a href="aboutus" id="aboutus">About Us</a></li>
            <li><a href="contact" id="contact">Contact</a></li>
        </ul>
    </div>
</div>

<div id="content">
    <div class="scroll">
        <div class="scrollContainer">
            <div class="panel" id="advertisements">
                <h2 id="subHeading">Published Advertisements</h2>

                <div id="tableContainer">
                    <table class="bordered" id="advtTable">
                        <thead>
                        <tr>
                            <th>Advertisement Title</th>
                            <th>Newspaper Name</th>
                            <th>Newspaper Code</th>
                            <th>Published Date</th>
                        </tr>
                        </thead>

                        <c:forEach var="PublishedAdvtDto" items="${publishedAdvtList}" varStatus="status">
                            <tr>
                                <!-- <td>${status.index + 1}</td> -->
                                <td>${PublishedAdvtDto.title}</td>
                                <td>${PublishedAdvtDto.newspaperName}</td>
                                <td>${PublishedAdvtDto.newspaperCode}</td>
                                <td><fmt:formatDate type="date" value="${PublishedAdvtDto.publishedDate}"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
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