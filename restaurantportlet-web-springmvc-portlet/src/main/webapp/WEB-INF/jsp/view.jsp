<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<portlet:renderURL var="renderRefreshUrl" />

	${nothingToDisplay}
	<c:if test="${empty nothingToDisplay}">

		<div id="map-canvas"></div>
		
		<h1 class="main-title">
			<spring:message code="view.list.title"/> ${area}
		</h1>

		<c:if test="${not empty dininghalls}">
			<ul class="dininghall-list">
				<c:forEach var="dininghall" items="${dininghalls}">
					<li class="lead">
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

				var diningHalls = new Array();

				// Data access
				<c:forEach var="dininghall" items="${dininghalls}">
					<portlet:renderURL var="viewRestaurantFromMaker">
		  				<portlet:param name="action" value="viewRestaurant"/>
		  				<portlet:param name="id" value="${dininghall.id}"/>
					</portlet:renderURL>
					diningHalls.push(["${dininghall.title}", ${dininghall.lat}, ${dininghall.lon}, "${viewRestaurantFromMaker}"]);
				</c:forEach>

				// Event Listener
				google.maps.event.addDomListener(window, 'load', initialize);
				
				var map;
				var diningHallsLatLng = new Array();
				var myLatlng = new google.maps.LatLng(parseFloat(diningHalls[0][1]).toFixed(6), parseFloat(diningHalls[0][2]).toFixed(6));
				var mapOptions = 
				{
				     center: myLatlng,
				     zoom: 12,
				     mapTypeId: google.maps.MapTypeId.ROADMAP
			    };

				function initialize() {

					// Map init

				    map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
				   
				    // Markers init

				    for(var i=0; i<diningHalls.length; i++) {
				    	
				    	var diningHallPosition = new google.maps.LatLng(diningHalls[i][1],diningHalls[i][2]);

				    	// For the distance matrix
				    	diningHallsLatLng.push(diningHallPosition);

				    	var mark = new google.maps.Marker({
					        position: diningHallPosition,
					        map: map,
					        title:diningHalls[i][0],
					        icon: "<%= renderRequest.getContextPath() + "/images/pin_resto.png" %>"
					    });

				    	// Event Listeners

				    	google.maps.event.addListener(mark, 'click', function() {
				    		for(var j=0; j<diningHalls.length; j++) {
				    			if(diningHalls[j][0] == this.getTitle())
				    				location.href=diningHalls[j][3];
				    		}
				    	});
				    }

				    // Geolocation feature

					if(navigator.geolocation) {
						
						$("<button class='get-located btn btn-primary'>Se localiser <span class='glyphicon glyphicon-map-marker'></span></button>").appendTo($(".main-title"));
						$(".get-located").click(function(e) {
							navigator.geolocation.getCurrentPosition(distanceCalculator, positionUndefined);
							e.preventDefault();
							$(this).fadeOut(500);
							$(this).unbind('click');
						});
					}

					// Event Listeners

					window.onresize = function() {
						map.setCenter(myLatlng);
					}
				}

				function distanceCalculator(position) {

					// If geolocation succeeded, we create a maker to the user position

					var origin = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

					var mark = new google.maps.Marker({
				        position: origin,
				        map: map,
				        title:"My current position",
				        zIndex : 99
				    });

					// We center the map at his place

					map.setCenter(origin);
					map.setZoom(14);

					// And calculate the distance from his place to all other dining hall in his area

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

				// Callback from getDistanceMatrix
				function sortByDistance(response, status) {

					// store the results in a var
					var results = response.rows[0].elements;
					var $diningHallList = $(".dininghall-list");

					// append distance attribute and text node
					$diningHallList.find('li').each(function(index) {
						$(this).append("- Distance : " + results[index].distance.text);
						$(this).attr('data-distance', results[index].distance.value);
					});

					// Dinamically sort by distance attribute numerical value
					var listItems = $diningHallList.find('li').sort(function(a,b){ 
						return $(a).attr('data-distance') - $(b).attr('data-distance'); 
					});
					$diningHallList.find('li').remove();
					$diningHallList.append(listItems);
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

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
