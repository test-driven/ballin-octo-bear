<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>${pageTitle}</title>
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
            <li><a href="../home" id="home">Home</a></li>
            <li><a href="../newspapers" id="newspapers">Newspapers</a></li>
            <li><a href="../advertisements" id="advertisements">Advertisements</a></li>
            <li><a href="../aboutus" id="aboutus">About Us</a></li>
            <li><a href="../contact" id="contact">Contact</a></li>
        </ul>
    </div>
</div>

<div id="content">
    <div class="scroll">
        <div class="scrollContainer">
            <div class="panel" id="aboutus">
                <h2>Newspaper Details</h2>

                <div id="formContainer">
                    <h2 id="formHeader">${formTitle}</h2>
                    <form:form commandName="newspaper" id="newspapersForm" method="post" class="acmeform">
                        <fieldset>
                            <p>
                                <label for="name">Name</label>
                                <form:input path="name" id="name" name="name" type="text" class="text"/>
                            </p>

                            <p>
                                <label for="code">Code</label>
                                <form:input path="code" id="code" name="code" type="text" class="text"/>
                            </p>

                            <p>
                                <label for="language">Language</label>
                                <form:select path="language" id="language" name="language" class="text">
                                    <form:option value="NONE" label="-- Select One --"/>
                                    <form:options items="${languageList}"/>
                                </form:select>
                            </p>

                            <p>
                                <label for="price">Price</label>
                                <form:input path="ratePerUnit" id="ratePerUnit" name="ratePerUnit" type="text"
                                            class="text"/>
                            </p>

                            <p class="buttonPanel">
                                <input type="submit" value="Submit"/>
                                <input type="reset" value="Reset"/>
                            </p>
                        </fieldset>
                    </form:form>

                    <ul id="errorList">
                        <c:forEach var="errormessage" items="${errors}">
                            <li>${errormessage}</li>
                        </c:forEach>
                    </ul>
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