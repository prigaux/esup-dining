<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />

<div class="restaurant-portlet">

${nothingToDisplay}

<c:if test="${not empty areas}">
<ul>
<c:forEach var="area" items="${areas}">
	<li>
		${area.name}
	</li>
</c:forEach>
</ul>
</c:if>
<c:if test="${not empty dininghalls}">
<ul>
<c:forEach var="dininghall" items="${dininghalls}">
	<li>
		${dininghall.title}
	</li>
</c:forEach>
</ul>
</c:if>
<c:if test="${not empty meals}">
<ul>
<c:forEach var="meal" items="${meals}">
	<li>
		${meal.type} - ${meal.date}
	</li>
</c:forEach>
</ul>
</c:if>
</div>

