<%@ include file="/WEB-INF/jsp/header.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />
	
	<h1>Paramètres</h1>
	
	<a href="<portlet:renderURL portletMode="view"/>" class="icn-fam icn-fam-back">
		<spring:message code="go.back.home"/>
	</a>
	<c:if test="${sessionScope.isAdmin}">
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
<h3>Vos restaurants favoris</h3>

<table class="table table-responsive table-striped">
	<c:forEach var="restaurant" items="${listFavRestaurant}">
		<tr>
			<td>
				${restaurant.title}
			</td>
			<td>
				<portlet:actionURL var="removeFavorite">
				  <portlet:param name="action" value="removeFavorite"/>
				  <portlet:param name="restaurant-id" value="${restaurant.id}"/>
				</portlet:actionURL>
				<a href="${removeFavorite}">
					Supprimer <span class="glyphicon glyphicon-remove"></span>
				</a>
			</td>
		</tr>
	</c:forEach>
</table>

</c:if>
</c:if>

<h3>Préférences nutritive</h3>
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
	
	<c:if test="${nutritSubmit == 'true'}">
		<label class="is-valid icn-fam icn-fam-valid" style="float:none;">
			<spring:message code="edit.msg.success"/>
		</label>
	</c:if>
	
	<input type="submit"/>
</form>
	
<%@ include file="/WEB-INF/jsp/footer.jsp"%>