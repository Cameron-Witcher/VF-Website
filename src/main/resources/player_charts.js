google.charts.load('current', { 'packages': ['corechart'] });
google.charts.load('current', { 'packages': ['table'] });
google.charts.setOnLoadCallback(drawTimeChart);
#if($infringements)
google.charts.setOnLoadCallback(drawInfringementChart);
#end
function drawTimeChart() {
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Server');
	data.addColumn('number', 'Hours');
	data.addRows([
		#foreach($server in $playtimes)
		  ['$server.name', (($server.playtime / 54000) * 0.015)],
		#end
	]);
	var options = {
		'title': 'Server Playtimes',
		'is3D': false,
		pieSliceBorderColor: '$borderColor',
		pieHole: 0,
		backgroundColor: '#202a55',
		legend: {
			textStyle: {
				color: 'white'
			}
		},
		chartArea: {
			width: '94%'
		},
		titleTextStyle: {
			color: 'white',
			bold: true
		},
		width: '100%'
	};
	var chart = new google.visualization.PieChart(document.getElementById('playtime_chart'));
	chart.draw(data, options);
}

function drawInfringementChart() {
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Type');
	data.addColumn('string', 'Duration');
	data.addColumn('string', 'Date');
	data.addColumn('string', 'Staff');
	data.addColumn('string', 'Action');
	data.addColumn('string', 'Notes');
	data.addColumn('boolean', 'Expired');

	var cssClassNames = {
		'headerRow': 'headerRow',
		'tableRow': 'tableRow',
		'oddTableRow': 'oddTableRow',
		'selectedTableRow': 'selectedTableRow',
		'hoverTableRow': 'hoverTableRow',
		'headerCell': 'headerCell',
		'tableCell': 'tableCell',
		'rowNumberCell': 'rowNumberCell'
	};
	
	#foreach($inf in $infringements)
		  var notes${inf.date} = "$inf.notes";
		  var arr${inf.date} = notes${inf.date}.split(" ");
          var sum${inf.date} = "";
          for(var z = 0; z < arr${inf.date}.length; z++){
	        var s = (arr${inf.date}[z].toLowerCase().startsWith("http://") || arr${inf.date}[z].toLowerCase().startsWith("https://")) ? "<a href='" + arr${inf.date}[z] +"' target='_blank'>" + arr${inf.date}[z] + "<sup><i style='font-size: xx-small;' class='fas fa-external-link-alt'></i></sup></a>" : arr${inf.date}[z];
	        sum${inf.date} = sum${inf.date} == "" ? s : sum${inf.date} + " " + s;
          }
    #end
	data.addRows([
		#foreach($inf in $infringements)
		  ['$inf.type', formatDate($inf.duration), new Date($inf.date).toLocaleDateString("en-US"), '$inf.staff', '$inf.action', sum${inf.date}, (new Date().getTime() - $inf.duration > $inf.date)],
		#end
	]);

	var table = new google.visualization.Table(document.getElementById('infringement_chart'));

	table.draw(data, {  page: 'enable', pageSize: 10, 'allowHtml': true, 'style': "color: white;", 'showRowNumber': false, 'width': '100%', 'height': '100%', 'cssClassNames': cssClassNames });
}

function formatDate(ms) {

	var l = Math.floor(ms / 1000);
	var sec = Math.floor(l % 60);
	var min = Math.floor((l / 60) % 60);
	var hours = Math.floor(((l / 60) / 60) % 24);
	var days = Math.floor((((l / 60) / 60) / 24) % 7);
	var weeks = Math.floor((((l / 60) / 60) / 24) / 7);

	if (weeks > 0) {
		return weeks + " weeks" + (days > 0 ? ", " + days + " days" : "")
			+ (hours > 0 ? ", " + hours + " hours" : "")
			+ (min > 0 ? ", " + min + " minutes" : "")
			+ (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
	}
	if (days > 0) {
		return days + " days" + (hours > 0 ? ", " + hours + " hours" : "")
			+ (min > 0 ? ", " + min + " minutes" : "")
			+ (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
	}
	if (hours > 0) {
		return hours + " hours" + (min > 0 ? ", " + min + " minutes" : "")
			+ (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
	}
	if (min > 0) {
		return min + " minutes"
			+ (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
	}
	if (sec > 0) {
		return sec + " " + (sec == 1 ? "second" : "seconds");
	}

	return "N/A";
}

