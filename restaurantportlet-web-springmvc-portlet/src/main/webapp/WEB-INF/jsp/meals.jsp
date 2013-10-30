<%@ include file="/WEB-INF/jsp/include.jsp"%>

<style type="text/css">
	.tab-header {
		display: table;
		padding: 0;
		width: 100%;
	}
	.tab-header li {
		display: table-cell;
		padding: 0;
		margin: 0;
		list-style: none;
		text-align: center;
		background: #DDD;
	}
	.tab-header a {
		padding: .5em 0;
		display: block;
	}
	.tab-header .ui-state-active {
		background: #FFF;
	}
</style>

<div class="restaurant-portlet">
	
	${nothingToDisplay}

	<c:if test="${not empty restaurant}">
		
		<h1>
			<spring:message code="meal.title" arguments="${restaurant.title}"/>
		</h1>


		<portlet:renderURL var="viewRestaurant">
				<portlet:param name="action" value="viewRestaurant"/>
				<portlet:param name="id" value="${restaurant.id}"/>
		</portlet:renderURL>
		<a href="${viewRestaurant}">
			<spring:message code="restaurant.link.back"/>
		</a>
	
		<div class="menus">
			<ul class="tab-header">
				<c:forEach var="menu" items="${menus}">
					<li>
						<a href="#menu-${menu.date}">
							${menu.date}
						</a>
					</li>
				</c:forEach>
			</ul>
			<c:forEach var="menu" items="${menus}">
				
				<div id="menu-${menu.date}">
					<h2>
						${menu.date}
					</h2>
	
					<c:forEach var="meal" items="${menu.meal}">
	
						<h3>
							${meal.name}
						</h3>
	
						<c:forEach var="foodCategory" items="${meal.foodcategory}">
	
							<h4>
								${foodCategory.name}
							</h4>
							
							<ul>
								<c:forEach var="dish" items="${foodCategory.dishes}">
									<li>
										${dish.name}
										<c:if test="${not empty dish.ingredients}">
											<ul>
												<li>
													<strong>
														<spring:message code="meal.ingredients"/> :
													</strong>
													${dish.ingredients}
												</li>
											</ul>
										</c:if>
										
										<c:if test="${not empty dish.code}">
											<ul><li>
												<c:forEach var="codeNumber" items="${dish.code}">
													<img src="<%= renderRequest.getContextPath() %><spring:message code="meal.code.${codeNumber}.img" />"
													     alt="<spring:message code="meal.code.${codeNumber}.description" />"
														 title="<spring:message code="meal.code.${codeNumber}.name" />"
													/>									
												</c:forEach>
											</ul></li>
										</c:if>
										
										<c:if test="${not empty dish.nutritionitems}">
											<ul>
												<c:forEach var="nutritionitem" items="${dish.nutritionitems}">
													<li>
														<strong>${nutritionitem.name}</strong> : ${nutritionitem.value}
													</li>
												</c:forEach>
											</ul>
										</c:if>
	
									</li>
								</c:forEach>
							</ul>
	
						</c:forEach>
	
					</c:forEach>
	
				</div>
	
			</c:forEach>
		</div>
	</c:if>	
</div>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".menus").tabs();
	});
</script>