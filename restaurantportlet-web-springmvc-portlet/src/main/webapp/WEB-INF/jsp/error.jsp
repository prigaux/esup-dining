<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<h1><spring:message code="error.page.title"/></h1>

	<p>
		<spring:message code="error.page.message"/> 
		<br/>
		<a href="<portlet:renderURL portletMode="edit"/>" class="icn-fam icn-fam-back">
			<spring:message code="menu.edit"/>
		</a>
	</p>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>