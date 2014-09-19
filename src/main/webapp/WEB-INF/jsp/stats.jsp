<%@ include file="/WEB-INF/jsp/header.jsp"%>
	
	<a href="<spring:url value="/settings" />" class="icn-fam icn-fam-back">
		<spring:message code="edit.admin.back"/>
	</a> - 	
		<spring:url value="/admin" var="adminSettings" />
	- 
		<a href="${adminSettings}">
			<spring:message code="menu.editadmin"/>
		</a> - 
	<a href="<spring:url value="/" />" class="icn-fam icn-fam-back">
		<spring:message code="go.back.home"/>
	</a> 	
	
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	<script type="text/javascript">

		google.load('visualization', '1.0', {'packages':['corechart']});
		google.setOnLoadCallback(drawCharts);

		function drawCharts() {

		  // Create the data table.
		  var data = new google.visualization.DataTable();
		  data.addColumn('string', 'Restaurant ID');
		  data.addColumn('number', 'Favorited Time');
		  data.addRows([
		<c:forEach var="entryStat" items="${stats}">
			  ['${restaurantsName[entryStat.key]}', ${entryStat.value}],
			</c:forEach>
		  ]);

		  // Set chart options
		  var options = {'title':'Nombre de personnes ayant mit en favoris des RUs.',
						 'width':500,
						 'height':400
						};

		  var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
		  chart.draw(data, options);

		/*NOT WORKING, TO DO ! */
		
		  var dataNutrit = new google.visualization.DataTable();
		  dataNutrit.addColumn('string', 'Preferences');

		  var dataNutrit = google.visualization.arrayToDataTable([
		  		['Preference nutritives', 'Nombre de fois dans la base'],
		  		<c:forEach var="prefCode" items="${prefCodeList}" varStatus="status">
		  			['<spring:message code="meal.code.${prefCode.key}.name"/>', ${prefCode.value}]<c:if test="${!status.last}">,</c:if>
		  		</c:forEach>
		  ]);

		  var optionsNutrit = {
		  	'title' : 'Preferences nutritives des utilisateurs',
		  	'width' : 500,
		  	'height': 400
		  };

		  var chartNutrit = new google.visualization.ColumnChart(document.getElementById('nutrit_chart'));
		  chartNutrit.draw(dataNutrit, optionsNutrit);
		}

	</script>
	
	<h1>Stats</h1>
	<div id="chart_div"></div>

	<div id="nutrit_chart"></div>
	
<%@ include file="/WEB-INF/jsp/footer.jsp"%>