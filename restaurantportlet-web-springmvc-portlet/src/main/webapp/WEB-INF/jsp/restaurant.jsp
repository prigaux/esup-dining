<%@ include file="/WEB-INF/jsp/include.jsp"%>

<div class="restaurant-portlet">
	
	<c:if test="${not empty restaurant}">
		
		<h1>${restaurant.title}</h1>
		
		<p>
			<portlet:actionURL var="addToFavorite">
				<portlet:param name="action" value="setFavorite" />
				<portlet:param name="id" value="${restaurant.id}" />
			</portlet:actionURL>
			<a href="${addToFavorite}">
				Ajouter aux favoris
			</a>
		</p>
		
		<p>
			${restaurant.shortDesc}
		</p>
		<p>
			<strong>Contact : </strong> 
			${restaurant.contact}
		</p>

		<c:if test="${not empty restaurant.lat and not empty restaurant.lon}">
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
	<c:if test="${empty restaurant}">
		<spring:message code="restaurant.empty"/>
	</c:if>
	
		
</div>

