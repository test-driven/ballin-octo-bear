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
                <h2>Advertisement Details</h2>

                <div id="formContainer">
                    <h2 id="formHeader">${formTitle}</h2>
                    <form:form commandName="advertisement" id="advertisementForm" method="post" class="acmeform">
                        <fieldset>
                            <p>
                                <label for="title">Title</label>
                                <form:input id="title" path="title" name="title" type="text" class="text"/>
                            </p>

                            <p>
                                <label for="description">Description</label>
                                <form:input id="description" path="description" name="description" type="text"
                                            class="text"/>
                            </p>

                            <p>
                                <label for="referenceCode">Reference Code</label>
                                <form:input id="referenceCode" path="referenceCode" name="referenceCode" type="text"
                                            class="text"/>
                            </p>

                            <p>
                                <label for="units">Units</label>
                                <form:input id="units" path="numberOfUnits" name="numberOfUnits" type="number"
                                            class="text"/>
                            </p>

                            <p>
                                <label for="newspaper">Newspapers</label>

                                <form:select path="selectedNewspapers" multiple="true" id="newspapers" name="newspapers"
                                             class="text" size="2">
                                    <form:option value="-1" label="-- Select --"/>
                                    <form:options items="${newspaperList}" itemValue="id" itemLabel="name"/>
                                </form:select>
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