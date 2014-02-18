<%@ include file="/WEB-INF/jsp/header.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />

<c:if test="${not empty user}">

	<h1><spring:message code="edit.title"/></h1>

	<a href="<portlet:renderURL portletMode="edit"/>" class="icn-fam icn-fam-back">
		<spring:message code="edit.admin.back"/>
	</a> - 	
	<a href="<portlet:renderURL portletMode="view"/>" class="icn-fam icn-fam-back">
		<spring:message code="go.back.home"/>
	</a> - 	
	<portlet:renderURL var="statsAdmin" portletMode="edit">
	  <portlet:param name="action" value="adminStats"/>
	</portlet:renderURL>
	<a href="${statsAdmin}" class="icn-fam icn-fam-stats">
		Stats
	</a>

<br/>

<c:if test="${not empty urlfluxdb}">	
		<portlet:actionURL var="setAreas">
		  <portlet:param name="action" value="setDefaultArea"/>
		</portlet:actionURL>

		<form method="post" action="${setAreas}"> 
		
			<c:forEach var="areaValue" items="${areaList}" varStatus="status">
				<label for="field-areas-${status.index}">
					<input type="checkbox" value="${areaValue}" name="chkArea[]" id="field-areas-${status.index}"
						<c:forEach var="areaDb" items="${defaultArea}">
							<c:if test="${areaDb == areaValue}">checked="checked"</c:if>
						</c:forEach>
					>
					${areaValue}
				</label>
			</c:forEach>
			
			<br/>
			<c:if test="${zoneSubmit}">
				<label class="is-valid icn-fam icn-fam-valid">
					<spring:message code="edit.msg.success"/>
				</label>
			</c:if>
			
			<input type="submit"/>
		</form>
</c:if>

		<portlet:actionURL var="urlFlux">
		  <portlet:param name="action" value="urlFlux"/>
		</portlet:actionURL>
	
		<form method="post" action="${urlFlux}">
			<fieldset>
				<legend><spring:message code="edit.admin.form.legend"/></legend>
				<label for="field-url"><spring:message code="edit.admin.form.label"/></label>
				<input type="text" id="field-url" name="url" value="${urlfluxdb}"/>
				<c:if test="${not empty urlError}">
					<label class="icn-fam <c:if test="${urlError == 'true'}">is-invalid icn-fam-invalid</c:if><c:if test="${urlError == 'false'}">is-valid icn-fam-valid</c:if>">
						<c:choose>
							<c:when test="${urlError == 'true'}">
								<spring:message code="edit.msg.urlerror"/>
							</c:when>
							<c:otherwise>
								<spring:message code="edit.msg.success"/>
							</c:otherwise>
						</c:choose>
					</label>
				</c:if>				
				<input type="submit" value="<spring:message code="edit.form.submit"/>" />
			</fieldset>
		</form>
		
		<portlet:actionURL var="forceFeedUpdate">
		  <portlet:param name="action" value="forceFeedUpdate"/>
		</portlet:actionURL>
		
		<p>
			<a href="${forceFeedUpdate}" class="btn btn-primary">
				<spring:message code="edit.admin.forceupdate"/>
			</a>
		</p>
		<c:if test="${not empty updateFeed}">
			${updateFeed}
		</c:if>
		
</c:if>
<c:if test="${not empty user}">
	<spring:message code="admin.notallowed"/>
</c:if>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>