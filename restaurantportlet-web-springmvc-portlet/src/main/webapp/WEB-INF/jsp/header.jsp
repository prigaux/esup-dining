<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:defineObjects/>

<c:set var="n"><portlet:namespace/></c:set>

<%-- Inclusion CSS --%>
<link type="text/css" rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/bootstrap.3.0.1.min.css">
<link type="text/css" rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/layout.css">
<link type="text/css" rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/theme.css">
<link type="text/css" rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/state.css">

<%-- Inclusion JS --%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=true"></script>
<link type="text/css" rel="stylesheet" href="${pageContext.servletContext.contextPath}/js/bootstrap.3.0.1.min.js">

<%-- Portlet container --%>
<div class="restaurant-portlet">