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
	<ul>
	<c:forEach var="dininghall" items="${dininghalls}">
		<li>
			<portlet:renderURL var="viewDiningHall">
  				<portlet:param name="action" value="viewDiningHall"/>
  				<portlet:param name="id" value="${dininghall.id}"/>
			</portlet:renderURL>
			<a href="${viewDiningHall}">
				${dininghall.title}
			</a>
		</li>
	</c:forEach>
	</ul>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=true">s</script>
	<script type="text/javascript">
		var diningHalls = new Array();
		<c:forEach var="dininghall" items="${dininghalls}">
			diningHalls.push(["${dininghall.title}", ${dininghall.lat}, ${dininghall.lon}]);
		</c:forEach>

		google.maps.event.addDomListener(window, 'load', initialize);
		
		function initialize() {
		    var myLatlng = new google.maps.LatLng(parseFloat(diningHalls[0][1]).toFixed(6), parseFloat(diningHalls[0][2]).toFixed(6));
			var mapOptions = {
			     center: myLatlng,
			     zoom: 12,
			     mapTypeId: google.maps.MapTypeId.ROADMAP
		    };
		    var map = new google.maps.Map(document.getElementById("gmaps-container"), mapOptions);
		    
		    var markers = new Array();
		    
		    for(var i=0; i<diningHalls.length; i++) {
		    	markers.push(new google.maps.Marker({
			        position: new google.maps.LatLng(diningHalls[i][1],diningHalls[i][2]),
			        map: map,
			        title:diningHalls[i][0]
			    }));
		    }
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

