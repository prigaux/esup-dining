<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />

<div class="restaurant-portlet">

${nothingToDisplay}

<c:if test="${empty nothingToDisplay}">
	<h1>Restaurant de la zone : ${area}</h1>

	<c:if test="${not empty restaurantList}">
		<c:forEach var="restaurant" items="${restaurantList}">
			<li>
				<portlet:renderURL var="showRestaurant">
				  <portlet:param name="action" value="showRestaurant"/>
				  <portlet:param name="id" value="${restaurant.id}"/>
				</portlet:renderURL>			
				<a href="${showRestaurant}">
					${restaurant.title}
				</a>
			</li>
		</c:forEach>
	</c:if>
	
	<c:if test="${empty restaurantList}">
		<spring:message code="restaurants.empty"/>
	</c:if>
	
	
	<c:if test="${not empty favList}">
		<h2>LISTE DES ID DES RU FAVORIS</h2>
		<ul>
		<c:forEach var="favId" items="${favList}">
			<li>${favId}</li>
		</c:forEach>
		</ul>
	</c:if>
</c:if>
</div>

