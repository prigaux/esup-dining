<%@ include file="/WEB-INF/jsp/header.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />
	
	<h1>Paramètres</h1>

	<a href="<portlet:renderURL portletMode="edit"/>" class="icn-fam icn-fam-back">
		<spring:message code="edit.admin.back"/>
	</a> - 	
	<a href="<portlet:renderURL portletMode="view"/>" class="icn-fam icn-fam-back">
		<spring:message code="go.back.home"/>
	</a>
	
	<!-- On met une négation dans le test pour faire les tests en local -->
	<c:if test="${!user.admin}">
<br/>
${nothingToDisplay}
<br/>
<c:if test="${empty nothingToDisplay}">	
		<portlet:actionURL var="forceFeedUpdate">
		  <portlet:param name="action" value="forceFeedUpdate"/>
		</portlet:actionURL>

		<h3>
			<a href="${forceFeedUpdate}">
				<spring:message code="edit.admin.forceupdate"/>
			</a>
		</h3>
		<c:if test="${not empty updateFeed}">
			${updateFeed}
		</c:if>

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
					<label style="color: #00FF00; font-weight: bold;">
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
				<c:if test="${urlError == 'true'}">
					<label style="color: #FF0000; font-weight: bold;">
						<spring:message code="edit.msg.urlerror"/>
					</label>
				</c:if>				
				<c:if test="${urlError == 'false'}">
					<label style="color: #00FF00; font-weight: bold;">
						<spring:message code="edit.msg.success"/>
					</label>
				</c:if>
				<input type="submit" value="Valider" />
			</fieldset>
		</form>
	</c:if>
	<c:if test="${!user.admin}">
		Vous n'avez pas accès à cette page car vous n'êtes pas administrateur
	</c:if>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>