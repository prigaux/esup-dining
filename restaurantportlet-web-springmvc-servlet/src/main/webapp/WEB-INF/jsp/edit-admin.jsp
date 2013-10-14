<%@ include file="/WEB-INF/jsp/header.jsp"%>

<!-- 
	Dans le portlet prototyping, l'utilisateur n'est pas admin 
	On fait donc la négation du test ci-dessous
-->
<c:choose>
	<c:when test="${!user.admin}">
		
		<h1>
			<spring:message code="menu.editadmin"/>
		</h1>

		<h2>#TODO : Rendre ça fonctionnel...</h2>

		<form method="post" action="edit/admin">
			<fieldset>
				<legend>Ajouter le flux URL de la portlet</legend>
				<label for="field-url">URL du flux :</label>
				<input type="text" id="field-url" name="url-flux">
				<input type="submit" value="Valider"/>
			</fieldset>
		</form>

	</c:when>
	<c:otherwise>
		<h1>
			<spring:message code="exception.title"/>
		</h1>
		<p>
			<spring:message code="admin.error"/>
		</p>
	</c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
