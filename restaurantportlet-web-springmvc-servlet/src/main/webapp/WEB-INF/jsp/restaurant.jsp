<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:choose>
	<c:when test="${not empty restaurant}">
		<h1>${restaurant.title}</h1>
		<h2>
			<a href="/restaurant?id=${restaurant.id}&addToFavorite">
				Ajouter aux favoris
			</a>
		</h2>
		<div id="map-canvas" style="width:300px; height:300px;"></div>
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
		
		<h3>Menus</h3>
		
		
		<c:forEach var="meal" items="${meals}">
			<ul>
				<li>${meal.type}</li>
				<li>${meal.date}</li>
				<li>${meal.diningHallId}</li>
				<li>${meal.foodCategory}</li>
			</ul>
		</c:forEach>
		
	</c:when>
	<c:otherwise>
		<h1>Oops on dirait que ce restaurant n'existe pas.</h1>
	</c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
