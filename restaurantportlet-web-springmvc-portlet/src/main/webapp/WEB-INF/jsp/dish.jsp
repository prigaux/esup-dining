<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<p>
		<portlet:renderURL var="viewRestaurant">
			<portlet:param name="action" value="viewRestaurant"/>
			<portlet:param name="id" value="${restaurantId}"/>
		</portlet:renderURL>
		<a href="${viewRestaurant}" class="icn-fam icn-fam-back">
			<spring:message code="restaurant.link.back"/>
		</a>
		 |
		<portlet:renderURL var="viewMeals">
			<portlet:param name="action" value="viewMeals" />
			<portlet:param name="id" value="${restaurantId}" />
		</portlet:renderURL>
		<a href="${viewMeals}" class="icn-fam icn-fam-back">
			<spring:message code="restaurant.link.viewMeals"/>
		</a>

	</p>
	
	<h1>
		${name}
	</h1>
	
	<c:if test="${not empty code}">
		<c:forEach var="codeNumber" items="${code}">
			<img src="<%=renderRequest.getContextPath()%><spring:message code="meal.code.${fn:trim(codeNumber)}.img" />"
			     alt="<spring:message code="meal.code.${fn:trim(codeNumber)}.description" />"
				 title="<spring:message code="meal.code.${fn:trim(codeNumber)}.name" />"
			/>								
		</c:forEach>
	</c:if>

	<c:if test="${not empty ingredients}">
		<p>
			<strong><spring:message code="meal.ingredients"/> :</strong>
			${ingredients}
		</p>
	</c:if>
	<c:if test="${not empty nutritionitems}">
		<p>
			<strong><spring:message code="meal.nutritionitems"/> :</strong>
			<ul>
			<c:forEach var="nutritionitem" items="${nutritionitems}">
				<li>
					<strong>${nutritionitem.name}</strong> : ${nutritionitem.value}
				</li>
			</c:forEach>
			</ul>
		</p>
	</c:if>
	
	<c:if test="${not empty nutritionPrefs}">
		
		<c:forEach var="userCode" items="${nutritionPrefs}">
		
			<c:forEach var="codeNumber" items="${code}">
				
				<c:if test="${(fn:trim(userCode) == fn:trim(codeNumber)) && fn:trim(codeNumber) != '15'}">
				
					<div class="alert alert-warning">
					
						<h2><spring:message code="meal.code.${fn:trim(codeNumber)}.name"/></h2>
						<p>
							<spring:message code="meal.code.${fn:trim(codeNumber)}.description"/>
						</p>
					
					</div>
				
				</c:if>
				
			</c:forEach>		
		</c:forEach>		
		
	</c:if>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>	