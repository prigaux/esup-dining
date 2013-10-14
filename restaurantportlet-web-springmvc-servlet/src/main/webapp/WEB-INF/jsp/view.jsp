<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h1>Liste des restaurants de ${area}</h1>

<ul>
	<c:forEach var="restaurant" items="${restaurantList}">
		<li>
			<a href="/restaurant?id=${restaurant.id}">
				${restaurant.title}
			</a>
		</li>
	</c:forEach>
	<c:if test="${empty restaurantList}">
		Aucun restaurant à afficher pour cette zone :-(
	</c:if>
</ul>


<c:if test="${not empty favList}">

	<h1>Vos restaurant favoris</h1>
	<h2>#TODO : Afficher les menus et pas que les titres des RUS Favoris ;)</h2>
	<ul>
		<c:forEach var="restaurant" items="${favList}">
			<li>
				${restaurant.title} (${restaurant.id})
			</li>
		</c:forEach>
	</ul>
</c:if>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
