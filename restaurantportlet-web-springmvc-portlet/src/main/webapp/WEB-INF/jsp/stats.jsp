<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<portlet:renderURL var="renderRefreshUrl" />
	
	<a href="<portlet:renderURL portletMode="edit"/>" class="icn-fam icn-fam-back">
		<spring:message code="edit.admin.back"/>
	</a> - 	
		<portlet:renderURL var="adminSettings">
				<portlet:param name="action" value="adminSettings"/>
		</portlet:renderURL>
	- 
		<a href="${adminSettings}">
			<spring:message code="menu.editadmin"/>
		</a> - 
	<a href="<portlet:renderURL portletMode="view"/>" class="icn-fam icn-fam-back">
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
		 
/*
		  var dataNutrit = new google.visualization.DataTable();
		  dataNutrit.addColumn('string', 'Preferences');

		  dataNutrit.addRows([
		  	<c:forEach var="prefCode" items="${prefCodeList}">
		  		['<spring:message code="meal.code.${prefCode}.name"/>'],
		  	</c:forEach>
		  ]);

		  var optionsNutrit = {
		  	'title' : 'Préférences nutritives des utilisateurs',
		  	'width' : 500,
		  	'height': 400
		  };

		  var chartNutrit = new google.visualization.ColumnChart(document.getElementById('nutrit_chart'));
		  chartNutrit.draw(dataNutrit, optionsNutrit);*/
		}

	</script>
	
	<h1>Stats</h1>
	<div id="chart_div"></div>
	<div id="nutrit_chart"></div>
	
<%@ include file="/WEB-INF/jsp/footer.jsp"%>