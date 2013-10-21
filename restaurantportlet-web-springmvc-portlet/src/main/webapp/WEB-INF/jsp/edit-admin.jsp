<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />

<div class="restaurant-portlet">
	
	<h1>Paramètres</h1>
	
	<!-- On met une négation dans le test pour faire les tests en local -->
	<c:if test="${!user.admin}">
		
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
				<input type="submit" value="Valider"/>
			</fieldset>
		</form>

		<portlet:actionURL var="urlFlux">
		  <portlet:param name="action" value="urlFlux"/>
		</portlet:actionURL>
	
		<form method="post" action="${urlFlux}">
			<fieldset>
				<legend>URL du flux</legend>
				<label for="field-url">URL : </label>
				<input type="text" id="field-url" name="url" value="${urlFluxCache}"/>
				<c:if test="${not empty urlError}">
					<label style="color: #FF0000; font-weight: bold;">${urlError}</label>
				</c:if>
				<input type="submit" value="Valider" />
			</fieldset>
		</form>
	</c:if>
	<c:if test="${!user.admin}">
		Vous n'avez pas accès à cette page car vous n'êtes pas administrateur
	</c:if>

	
</div>

