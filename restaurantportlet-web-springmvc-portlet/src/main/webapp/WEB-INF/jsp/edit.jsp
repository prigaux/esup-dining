<%@ include file="/WEB-INF/jsp/header.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />
	
	<h1>Paramètres</h1>
	
	<a href="<portlet:renderURL portletMode="view"/>" class="icn-fam icn-fam-back">
		<spring:message code="go.back.home"/>
	</a>
	<c:if test="${user.admin}">
		<h2>
			<portlet:renderURL var="adminSettings">
  				<portlet:param name="action" value="adminSettings"/>
			</portlet:renderURL>
		
			<a href="${adminSettings}">
				<spring:message code="menu.editadmin"/>
			</a>
		</h2>
	</c:if>
${nothingToDisplay}

<c:if test="${empty nothingToDisplay}">
<c:if test="${not empty areas}">
	
	<portlet:actionURL var="setUserArea">
	  <portlet:param name="action" value="setUserArea"/>
	</portlet:actionURL>

	<form method="post" action="${setUserArea}">
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

<c:if test="${not empty listFavRestaurant}">
	<portlet:actionURL var="removeFavorite">
	  <portlet:param name="action" value="removeFavorite"/>
	</portlet:actionURL>
	<form method="POST" action="${removeFavorite}">
	<fieldset>
		<legend>Vos restaurant favoris</legend>
		<c:forEach var="restaurant" items="${listFavRestaurant}">

			<label for="field-restaurant-${restaurant.id}" style="display:block; padding: 10px 0; border-bottom: 1px solid #333;">
				${restaurant.title}
				<input type="radio" name="restaurant-id" id="field-restaurant-${restaurant.id}" value="${restaurant.id}"/>
			</label>

		</c:forEach>
		<input type="submit" value="Supprimer">
	</fieldset>
</form>
</c:if>
</c:if>

<h2>Préférences nutritive</h2>
<portlet:actionURL var="nutritionPreferences">
  <portlet:param name="action" value="nutritionPreferences"/>
</portlet:actionURL>
<form method="POST" action="${nutritionPreferences}">
	<c:forEach var="code" items="${nutritionCodes}">
		<label style="float: none;">
			<spring:message code="meal.code.${fn:trim(code)}.name" />
			<input type="checkbox" name="code-${code}" id="code-${code}"
				
				<c:forEach var="userPrefCode" items="${nutritionPrefs}">
					<c:if test="${userPrefCode == code}">
						checked="checked"
					</c:if>
				</c:forEach>
				
			/>
		</label>	
	</c:forEach>
	<input type="submit"/>
</form>
	
<%@ include file="/WEB-INF/jsp/footer.jsp"%>

