<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<portlet:renderURL var="renderRefreshUrl" />
	
	<!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">

      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
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
                       'height':400};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
        
      }

    </script>
	
	<h1>Stats</h1>
	<div id="chart_div"></div>
	
<%@ include file="/WEB-INF/jsp/footer.jsp"%>