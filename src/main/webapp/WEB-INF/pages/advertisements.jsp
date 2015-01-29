<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ACME-Advertisements</title>
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
            <li><a href="newspapers" id="newspapers">Newspapers</a></li>
            <li><a href="advertisements" id="advertisements" class="selected">Advertisements</a></li>
            <li><a href="aboutus" id="aboutus">About Us</a></li>
            <li><a href="contact" id="contact">Contact</a></li>
        </ul>
    </div>
</div>

<div id="content">
    <div class="scroll">
        <div class="scrollContainer">
            <div class="panel" id="advertisements">
                <h2 id="subHeading">Advertisements</h2>

                <div class="btnContainer"><a class="btn" id="addAdvt" href="advertisements/new">+Advertisement</a></div>
                <div id="tableContainer">
                    <table class="bordered" id="advtTable">
                        <thead>
                        <tr>
                            <th>Title</th>
                            <th>Description</th>
                            <th>created date</th>
                            <th>No: of Units</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <c:forEach var="advertisement" items="${advertisementList}" varStatus="status">
                            <tr>
                                <!-- <td>${status.index + 1}</td> -->
                                <td>${advertisement.title}</td>
                                <td>${advertisement.description}</td>
                                <td><fmt:formatDate type="date" value="${advertisement.createdDate}"/></td>
                                <td>${advertisement.numberOfUnits}</td>
                                <td><a class="editbtn" href="advertisements/${advertisement.id}">Edit</a></td>
                            </tr>
                        </c:forEach>

                        <%-- <c:forEach var="AdvertisementDto" items="${advertisementList}" varStatus="status">
                          <tr>
                            <!-- <td>${status.index + 1}</td> -->
                            <td>${AdvertisementDto.title}</td>
                            <td>${AdvertisementDto.description}</td>
                            <td> <fmt:formatDate type="date" value="${AdvertisementDto.createdDate}" /></td>
                            <td>${AdvertisementDto.numberOfUnits}</td>
                            <td><a class="editbtn" href="advertisements/${AdvertisementDto.id}">Edit</a></td>
                          </tr>
                        </c:forEach>  --%>
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