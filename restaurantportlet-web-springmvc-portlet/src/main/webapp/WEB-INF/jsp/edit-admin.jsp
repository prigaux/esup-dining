<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />

<div class="restaurant-portlet">
	
	<h1>Paramètres</h1>

	<a href="<portlet:renderURL portletMode="edit"/>">
		<spring:message code="edit.admin.back"/>
	</a> - 	
	<a href="<portlet:renderURL portletMode="view"/>">
		<spring:message code="go.back.home"/>
	</a>
	
	<!-- On met une négation dans le test pour faire les tests en local -->
	<c:if test="${!user.admin}">

${nothingToDisplay}

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

</div>

