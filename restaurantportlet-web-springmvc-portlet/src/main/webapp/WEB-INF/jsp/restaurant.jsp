<%@ include file="/WEB-INF/jsp/include.jsp"%>

<style type="text/css">
	img {
		max-width: 100%;
	}
	.ta-center {
		text-align: center;
	}
	#opening {
		width: 100%;
		text-align: center;
	}
	#opening th {
		font-weight: bold;
	}

</style>

<div class="restaurant-portlet">
	
	<c:if test="${empty restaurant}">
		<h1>
			<spring:message code="restaurant.empty"/>
		</h1>
		<portlet:renderURL var="goToHome"/>	
		<a href="${goToHome}">
			<spring:message code="go.back.home"/>
		</a>
	</c:if>

	<c:if test="${not empty restaurant}">		
		
		<h1>${restaurant.title}</h1>
		
		<p>
			<c:choose>
				<c:when test="${isFavorite}">
					<portlet:actionURL var="removeFromFavorite">
						<portlet:param name="action" value="removeFavorite" />
						<portlet:param name="restaurant-id" value="${restaurant.id}" />
					</portlet:actionURL>
					<a href="${removeFromFavorite}">
						<spring:message code="restaurant.link.removeFromFavorite"/>
					</a>
				</c:when>
				<c:otherwise>

					<portlet:actionURL var="addToFavorite">
						<portlet:param name="action" value="setFavorite" />
						<portlet:param name="id" value="${restaurant.id}" />
					</portlet:actionURL>
					<a href="${addToFavorite}">
						<spring:message code="restaurant.link.addToFavorite"/>
					</a>
					
				</c:otherwise>
			</c:choose>
			-
			
			<portlet:actionURL var="viewMeals">
				<portlet:param name="action" value="viewMeals" />
				<portlet:param name="id" value="${restaurant.id}" />
			</portlet:actionURL>
			
			<a href="${viewMeals}">
				<spring:message code="restaurant.link.viewMeals"/>
			</a>
			
			<c:if test="${restaurant.accessibility}">
				- ACCESSIBLE (En attente d'une icône).
			</c:if>
		</p>
		
		<c:if test="${not empty restaurant.shortdesc}">
			<p>
				${restaurant.shortdesc}
			</p>
		</c:if>		
		
		<c:if test="${not empty restaurant.photo.src}">
			<p class="ta-center">
				<img src="${restaurant.photo.src}" alt="${restaurant.photo.alt}" />
			</p>
		</c:if>		
		
		<c:if test="${not empty restaurant.description}">
			<p>
				<strong><spring:message code="restaurant.msg.description"/></strong> :
				${restaurant.description}
			</p>
		</c:if>		
		
		<c:if test="${not empty restaurant.operationalhours}">
			<p>
				<strong><spring:message code="restaurant.msg.operationalhours"/></strong> :
				${restaurant.operationalhours}
			</p>
		</c:if>		
		
		<c:if test="${not empty restaurant.address}">
			<p>
				<strong><spring:message code="restaurant.msg.address"/></strong> :
				${restaurant.address}
			</p>
		</c:if>
		
		<c:if test="${not empty restaurant.access}">
			<p>
				<strong><spring:message code="restaurant.msg.access"/></strong> :
				${restaurant.access}
			</p>
		</c:if>		
		
		<c:if test="${not empty restaurant.contact.tel}">
			<p>
				<strong><spring:message code="restaurant.msg.tel"/></strong> :
				${restaurant.contact.tel}
			</p>
		</c:if>		
		
		<c:if test="${not empty restaurant.contact.email}">
			<p>
				<strong><spring:message code="restaurant.msg.email"/></strong> :
				${restaurant.contact.email}
			</p>
		</c:if>

		<c:if test="${not empty restaurant.opening}">

			<table id="opening">
				
				<tr>
					<th colspan="4">
						Ouverture	
					</th>
				</tr>
				<tr>
					<th></th>
					<th><spring:message code="restaurant.msg.opening.morning"/></strong></th>
					<th><spring:message code="restaurant.msg.opening.midday"/></strong></th>
					<th><spring:message code="restaurant.msg.opening.evening"/></strong></th>
			</table>

			<script type="text/javascript">
				var opening = "${restaurant.opening}";
				opening = opening.split(",");

				var dayOfTheWeek = ["<spring:message code="restaurant.msg.opening.monday"/></strong>", 
									"<spring:message code="restaurant.msg.opening.tuesday"/></strong>", 
									"<spring:message code="restaurant.msg.opening.wednesday"/></strong>", 
									"<spring:message code="restaurant.msg.opening.thursday"/></strong>", 
									"<spring:message code="restaurant.msg.opening.friday"/></strong>", 
									"<spring:message code="restaurant.msg.opening.saturday"/></strong>", 
									"<spring:message code="restaurant.msg.opening.sunday"/></strong>"];

				var tableOpening = document.getElementById("opening");

				for(var i=0; i<opening.length; i++) {

					var ligne = document.createElement("tr");

					var dayNode = document.createElement("th");
					dayNode.innerHTML = dayOfTheWeek[i];

					ligne.appendChild(dayNode);

					for(var j=0; j<3; j++) {
						var tableNode = document.createElement("td");
						if(opening[i].charAt(j) == "1") {
							tableNode.innerHTML = "O";
						} else {
							tableNode.innerHTML = "X";
						}
						ligne.appendChild(tableNode);
					}					
					tableOpening.appendChild(ligne);
				}
			</script>

		</c:if>

		<c:if test="${not empty restaurant.lat and not empty restaurant.lon}">
			<h2>
				<spring:message code="restaurant.msg.map"/>
			</h2>
			
			<div id="map-canvas" style="width:100%; height: 300px;"></div>
			<script type="text/javascript"
			  src="https://maps.googleapis.com/maps/api/js?sensor=true">
			</script>
			<script type="text/javascript">
			function initialize() {
			    var myLatlng = new google.maps.LatLng(${restaurant.lat}, ${restaurant.lon});
				var mapOptions = {
			      center: myLatlng,
			      zoom: 14,
			      mapTypeId: google.maps.MapTypeId.ROADMAP
			    };
			    var map = new google.maps.Map(document.getElementById("map-canvas"),
			        mapOptions);
			    var marker = new google.maps.Marker({
			        position: myLatlng,
			        map: map,
			        title:"${restaurant.title}"
			    });
			  }
			  google.maps.event.addDomListener(window, 'load', initialize);
			</script>
		</c:if>	

	</c:if>		
</div>

