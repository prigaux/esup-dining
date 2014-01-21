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
      google.setOnLoadCallback(drawChart);

      function drawChart() {

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
        
      }

    </script>
	
	<h1>Stats</h1>
	<div id="chart_div"></div>
	
<%@ include file="/WEB-INF/jsp/footer.jsp"%>