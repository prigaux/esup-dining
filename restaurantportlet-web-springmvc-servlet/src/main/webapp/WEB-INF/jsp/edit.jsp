<%@ include file="/WEB-INF/jsp/header.jsp"%>


<h1>
	<spring:message code="menu.edit"/>
</h1>

<h2>
<!-- A des fin de test on rajoute une négation à l'expression
	pour voir le lien quand on est pas administrateur -->
<c:if test="${!user.admin}">
	<a href="/edit/admin">
		<spring:message code="menu.editadmin"/>
	</a>
</c:if>
</h2>
<form method="post" action="/edit">
	<fieldset>
		<legend>Choisir une zone par défaut</legend>
		<label for="field-zone">Zones : </label>
		<select id="field-zone" name="zone">
			<c:forEach var="area" items="${areas}">
				<option value="${area}"
					<c:if test="${area == userArea}">
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

<c:if test="${not empty favList}">

<form method="POST" action="/edit">
	<fieldset>
		<legend>Vos restaurant favoris</legend>

		<c:forEach var="restaurant" items="${favList}">

			<label for="field-restaurant-${restaurant.id}" style="display:block; padding: 10px 0; border-bottom: 1px solid #333;">
				${restaurant.title}
				<input type="radio" name="id" id="field-restaurant-${restaurant.id}" value="${restaurant.id}"/>
			</label>

		</c:forEach>

		<input type="hidden" name="removeFromFavorite"/>
		<input type="submit" value="Supprimer">
	</fieldset>
</form>

</c:if>




<%@ include file="/WEB-INF/jsp/footer.jsp"%>
