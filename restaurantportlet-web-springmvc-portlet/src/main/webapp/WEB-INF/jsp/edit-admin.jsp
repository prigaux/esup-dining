<%@ include file="/WEB-INF/jsp/header.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />

<c:if test="${not empty user}">

	<h1>Paramètres</h1>

	<a href="<portlet:renderURL portletMode="edit"/>" class="icn-fam icn-fam-back">
		<spring:message code="edit.admin.back"/>
	</a> - 	
	<a href="<portlet:renderURL portletMode="view"/>" class="icn-fam icn-fam-back">
		<spring:message code="go.back.home"/>
	</a>
	
	<!-- On met une négation dans le test pour faire les tests en local -->
<br/>
${nothingToDisplay}
<br/>
<c:if test="${empty nothingToDisplay}">	
		<portlet:actionURL var="setDefaultArea">
		  <portlet:param name="action" value="setDefaultArea"/>
		</portlet:actionURL>
		
		<form method="post" action="${setDefaultArea}">
			<fieldset>
				<legend>Choisir une zone par défaut</legend>
				<label for="field-zone">Zones : </label>
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
				<c:if test="${zoneSubmit == 'true'}">
					<label class="is-valid icn-fam icn-fam-valid">
						<spring:message code="edit.msg.success"/>
					</label>
				</c:if>
				<input type="submit" value="Valider"/>
			</fieldset>
		</form>
</c:if>

		<portlet:actionURL var="urlFlux">
		  <portlet:param name="action" value="urlFlux"/>
		</portlet:actionURL>
	
		<form method="post" action="${urlFlux}">
			<fieldset>
				<legend>URL du flux</legend>
				<label for="field-url">URL : </label>
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
				<input type="submit" value="Valider" />
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