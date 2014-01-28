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
		<portlet:actionURL var="setDefaultArea">
		  <portlet:param name="action" value="setDefaultArea"/>
		</portlet:actionURL>
		
		<form method="post" action="${setDefaultArea}" enctype="multipart/form-data"> 
			<fieldset>
				<legend><spring:message code="edit.form.zone.legend"/></legend>
				<%--<label for="field-zone"><spring:message code="edit.form.zone.label"/></label>
				<select id="field-zone" name="zone">
					<c:forEach var="area" items="${areas}">
						<option value="${area}"
							<c:if test="${area == defaultArea}">
								selected="selected"
							</c:if>
						>
							${area}
						</option>
					</c:forEach>
				</select>

				<c:forEach var="area" items="${areas}" varStatus="status">

					<c:if test="${status.index % 2 == 0}">
						<c:if test="${!status.first}">
							</div>
						</c:if>
						<c:if test="${!status.last}">
							<div class="row">
						</c:if>
					</c:if>

					<label for="area-${status.index}" class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
						<input type="checkbox" id="area-${status.index}" name="areas" value="${area}">
						${area}
					</label>

				</c:forEach>--%>

				<input type="checkbox" id="test" name="test" value="testvalue"/>

				<c:if test="${zoneSubmit == 'true'}">
					<label class="is-valid icn-fam icn-fam-valid">
						<spring:message code="edit.msg.success"/>
					</label>
				</c:if>
				<input type="submit" value="<spring:message code="edit.form.submit"/>"/>
			</fieldset>
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