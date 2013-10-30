<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="renderRefreshUrl" />

<div class="restaurant-portlet">

${nothingToDisplay}
<c:if test="${empty nothingToDisplay}">
	<h1>
		<spring:message code="view.list.title"/> ${area}
	</h1>

	<div id="gmaps-container" style="width:100%; height:300px;"></div>
	
	<c:if test="${not empty dininghalls}">
	<ul class="dininghall-list">
	<c:forEach var="dininghall" items="${dininghalls}">
		<li>
			<portlet:renderURL var="viewRestaurant">
  				<portlet:param name="action" value="viewRestaurant"/>
  				<portlet:param name="id" value="${dininghall.id}"/>
			</portlet:renderURL>
			<a href="${viewRestaurant}">
				${dininghall.title} - ${dininghall.id}
			</a>
		</li>
	</c:forEach>
	</ul>
	<script type="text/javascript">
		var map;
			
		var diningHalls = new Array();
		var diningHallsLatLng = new Array();

		<c:forEach var="dininghall" items="${dininghalls}">
			<portlet:renderURL var="viewRestaurantFromMaker">
  				<portlet:param name="action" value="viewRestaurant"/>
  				<portlet:param name="id" value="${dininghall.id}"/>
			</portlet:renderURL>
			diningHalls.push(["${dininghall.title}", ${dininghall.lat}, ${dininghall.lon}, "${viewRestaurantFromMaker}"]);
		</c:forEach>

		google.maps.event.addDomListener(window, 'load', initialize);
		
		var myLatlng = new google.maps.LatLng(parseFloat(diningHalls[0][1]).toFixed(6), parseFloat(diningHalls[0][2]).toFixed(6));

		function initialize() {
			var mapOptions = {
			     center: myLatlng,
			     zoom: 12,
			     mapTypeId: google.maps.MapTypeId.ROADMAP
		    };
		    map = new google.maps.Map(document.getElementById("gmaps-container"), mapOptions);
		   
		    for(var i=0; i<diningHalls.length; i++) {
		    	var diningHallPosition = new google.maps.LatLng(diningHalls[i][1],diningHalls[i][2]);

		    	diningHallsLatLng.push(diningHallPosition);

		    	var mark = new google.maps.Marker({
			        position: diningHallPosition,
			        map: map,
			        title:diningHalls[i][0],
			        icon: "<%= renderRequest.getContextPath() + "/images/pin_resto.png" %>"
			    });

		    	google.maps.event.addListener(mark, 'click', function() {
		    		for(var j=0; j<diningHalls.length; j++) {
		    			if(diningHalls[j][0] == this.getTitle())
		    				location.href=diningHalls[j][3];
		    		}
		    	});

		    }
			window.onresize = function() {
				map.setCenter(myLatlng);
			}
		}

		$(document).ready(function() {
			if(navigator.geolocation) {

				$("<button class='get-located'>Se localiser</button>").prependTo($(".restaurant-portlet"));
				$(".get-located").click(function(e) {
					navigator.geolocation.getCurrentPosition(distanceCalculator, positionUndefined);
					e.preventDefault();
				});
			}
		});

		function distanceCalculator(position) {

			var origin = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

			var mark = new google.maps.Marker({
		        position: origin,
		        map: map,
		        title:"Ma position actuelle"
		    });

			map.setCenter(origin);
			map.setZoom(14);

			var service = new google.maps.DistanceMatrixService();

			service.getDistanceMatrix({
				origins : [origin],
				destinations : diningHallsLatLng,
				travelMode : google.maps.TravelMode.DRIVING,
				unitSystem : google.maps.UnitSystem.METRIC,
				durationInTraffic : true,
				avoidHighways : false,
				avoidTolls : false
			}, sortByDistance);
		}

		function sortByDistance(response, status) {
			var results = response.rows[0].elements;
			var $diningHallList = $(".dininghall-list li");
			for(var i=0; i<results.length; i++) {
				$diningHallList.eq(i).append("- Distance : " + results[i].distance.text);
			}
		}

		function positionUndefined(err) {
			console.error(err);
		}

	</script>
	</c:if>
	<c:if test="${empty dininghalls}">
		<p>
			<spring:message code="view.list.empty"/>
		</p>
	</c:if>
</c:if>
</div>

