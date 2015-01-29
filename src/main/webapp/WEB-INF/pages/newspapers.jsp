<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ACME-Newspapers</title>
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
            <li><a href="home" id="home">Home</a></li>
            <li><a href="newspapers" id="newspapers" class="selected">Newspapers</a></li>
            <li><a href="advertisements" id="advertisements">Advertisements</a></li>
            <li><a href="aboutus" id="aboutus">About Us</a></li>
            <li><a href="contact" id="contact">Contact</a></li>
        </ul>
    </div>
</div>

<div id="content">
    <div class="scroll">
        <div class="scrollContainer">
            <div class="panel" id="newspapers">
                <h2 id="subHeading">Newspapers</h2>

                <div class="btnContainer"><a class="btn" id="addNewspaper" href="newspapers/new">+Newspaper</a></div>
                <div id="tableContainer">
                    <table class="bordered" id="newspaperTable">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Code</th>
                            <th>Language</th>
                            <th>Rate</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <c:forEach var="NewspaperDto" items="${newspaperList}" varStatus="status">
                            <tr>
                                <!-- <td>${status.index + 1}</td> -->
                                <td>${NewspaperDto.name}</td>
                                <td>${NewspaperDto.code}</td>
                                <td>${NewspaperDto.language}</td>
                                <td>${NewspaperDto.ratePerUnit}</td>
                                <td><a class="editbtn" href="newspapers/${NewspaperDto.id}">Edit</a></td>
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