<%@ include file="/WEB-INF/jsp/header.jsp"%>
	
	<p>
		<a href="<spring:url value="/" />" class="icn-fam icn-fam-back">
			<spring:message code="go.back.home" />
		</a>
	</p>

	<c:if test="${not empty restaurant}">
		
		<c:if test="${restaurantClosed}">
			<div class="alert alert-warning">
				<a class="close" data-dismiss="alert">�</a>
				<fmt:parseDate value="${restaurant.closing}" var="parsedDate" pattern="yyyy-MM-dd" />
				<fmt:formatDate value="${parsedDate}" var="localeDate" pattern="${sessionScope.dateLocalePattern}" />
				${localeDate}
				<spring:message code="restaurant.closed" arguments="${localeDate}"/>
			</div>
		</c:if>
		
		<c:if test="${!restaurantClosed}">
			<nav class="navbar navbar-default" role="navigation">
		
  			<div class="navbar-header">
		    	<span type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				    <span class="sr-only">Toggle navigation</span>
				    <span class="icon-bar"></span>
				    <span class="icon-bar"></span>
				    <span class="icon-bar"></span>
		    	</span>
		    	<span class="navbar-brand">${restaurant.title}</span>
		    </div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li>
						<c:choose>
							<c:when test="${isFavorite}">
								<spring:url value="/favorite/remove" var="removeFromFavorite">
									<spring:param name="restaurant-id" value="${restaurant.id}" />
								</spring:url>
								<a href="${removeFromFavorite}" class="icn-fam icn-fam-fav">
									<spring:message code="restaurant.link.removeFromFavorite"/>
								</a>
							</c:when>
							<c:otherwise>

								<spring:url value="/favorite/add" var="addToFavorite">
									<spring:param name="id" value="${restaurant.id}" />
								</spring:url>
								<a href="${addToFavorite}" class="icn-fam icn-fam-fav">
									<spring:message code="restaurant.link.addToFavorite"/>
								</a>
								
							</c:otherwise>
						</c:choose>
					</li>
					<li>
						<spring:url value="/restaurant" var="viewRestaurant">
							<spring:param name="id" value="${restaurant.id}" />
						</spring:url>
						
						<a href="${viewRestaurant}" class="icn-fam icn-fam-meal">
							<spring:message code="restaurant.link.viewRestaurant"/>
						</a>
					</li>
					<c:if test="${restaurant.accessibility}">
					<li>
						<span class="icn-fam icn-fam-disability">
							<spring:message code="restaurant.msg.disability"/>
						</span>
					</li>
					</c:if>
					<c:if test="${restaurant.wifi}">
					<li>
						<span class="icn-fam icn-fam-wifi">
							<spring:message code="restaurant.wifi"/>					
						</span>
					</li>
					</c:if>
				</ul>
			</div>
		</nav>
		
		<div class="menus">
			<ul class="tab-header">
				<c:forEach var="menu" items="${menus}">
					<li>
						<a href="#menu-${menu.date}">
							<fmt:parseDate value="${menu.date}" var="parsedDate" pattern="yyyy-MM-dd" />
							<fmt:formatDate value="${parsedDate}" pattern="${sessionScope.dateLocalePattern}" />
						</a>
					</li>
				</c:forEach>
			</ul>
			<c:forEach var="menu" items="${menus}">
				
				<div id="menu-${menu.date}">
					<h2>
						<fmt:parseDate value="${menu.date}" var="parsedDate" pattern="yyyy-MM-dd" />
						<fmt:formatDate value="${parsedDate}" pattern="${sessionScope.dateLocalePattern}" />
					</h2>
					<div class="meals-accordion">
						
						<c:forEach var="meal" items="${menu.meal}">
		
							<h3 class="accordion-title">
								${meal.name}
							</h3>

							<div class="meal-container" style="height: auto !important;">

								<c:forEach var="foodCategory" items="${meal.foodcategory}">
			
									<h4>
										${foodCategory.name}
									</h4>
									
									<ul>
										<c:forEach var="dish" items="${foodCategory.dishes}">
										<c:if test="${not empty dish.code}">
												<c:forEach var="codeNumber" items="${dish.code}">
													<c:forEach var="userCodeNumber" items="${nutritionPrefs}">
														
														<c:if test="${codeNumber==userCodeNumber}">
															
															<c:choose>
																<c:when test="${codeNumber=='15'}">
																	<c:set var="elementClassName" value='alert alert-success"'/>
																</c:when>
																<c:otherwise>
																	<c:set var="elementClassName" value='alert alert-warning"'/>
																</c:otherwise>
															</c:choose>
														</c:if>
														
													</c:forEach>								
												</c:forEach>
											</c:if>
											<li class="${elementClassName}">

												<spring:url value="/dish" var="viewDish">
													<spring:param name="id" value="${restaurant.id}"/>
													<spring:param name="name" value="${dish.name}"/>
													<spring:param name="ingredients" value="${dish.ingredients}"/>
													<spring:param name="nutritionitems" value="${dish.nutritionitems}"/>
													<spring:param name="code" value="${dish.code}"/>
												</spring:url>

												<c:if test="${not empty dish.code or not empty dish.ingredients or not empty dish.nutritionitems}">
												<a href="${viewDish}">
													</c:if>
														${dish.name}
													<c:if test="${not empty dish.code or not empty dish.ingredients or not empty dish.nutritionitems}">
													<c:forEach var="codeNumber" items="${dish.code}">
														<img src="<spring:url value="/" /><spring:message code="meal.code.${codeNumber}.img" />"
														     alt="<spring:message code="meal.code.${codeNumber}.description" />"
															 title="<spring:message code="meal.code.${codeNumber}.name" />"/>									
													</c:forEach>
												</a>
												</c:if>				
											</li>
										</c:forEach>
									</ul>
								</c:forEach>
							</div>
						</c:forEach>
					</div>
				</div>
	
			</c:forEach>
		</div>
		</c:if>
	</c:if>	

<script type="text/javascript">
	$(window).load(function() {

		var $menus = $(".menus").tabs();
		var $meals = $(".meals-accordion").accordion();

		$menus.each(function(index) {

			var $menuLi = $(this).find('.tab-header li');

			$menuLi.click(function(e) {
				var indexClick = $menuLi.index($(this));
				// We need to refresh the accordion because jQuery UI cannot calculate the height of a display none accordion element.
				$menus.eq(indexClick).tabs("refresh");
				$meals.accordion("refresh");
			});

		});
		
	});
</script>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
