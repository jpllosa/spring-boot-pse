<!DOCTYPE html>
<html>
<head>
<title>${title}</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="datepicker/css/datepicker.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script	src="datepicker/js/bootstrap-datepicker.js"></script>
</head>
<body onload="getStockSymbolAndNames()">
	<div class="container">
		<h1 class="text-center">${title}</h1>
		<p>Description here...</p>
		<p>Usage instruction here...</p>
		<div class="form-group">
			<label for="portfolioSize">Number of Stocks in your Portfolio</label>
			<select class="form-control" id="portfolioSize" name="portfolioSize" 
				onchange="processPortfolioSize()" onclick="processPortfolioSize()">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
			</select>
		</div>
		<div class="form-group">
			<label for="yearsHeld">Number of Years Invested</label>
			<select class="form-control" id="yearsHeld" name="yearsHeld">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
			</select>
		</div>
		<!-- 
		<div class="well">
            <input type="text" class="span2" value="2012-02-16" id="dp1" >
          </div>
		 --> 
		<div class="form-group">
			<label for="startDate">Investment Start Date (2005-01-02)</label>
			<input class="form-control" type="text" id="datePicker" name="startDate" value="2005-01-02"
			onchange="getStartDate()"> <!-- getStartDate not needed -->
		</div>
		
		<h3 class="text-center">Portfolio</h3>
		<div class="table-responsive">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="col-md-2">Stock Symbol</th>
						<th class="col-md-6">Stock Name</th>
						<th class="col-md-2">Shares</th>
						<th class="col-md-2">Total Cost (PHP)</th>
					</tr>
				</thead>
				<tbody id="tableBody">

				</tbody>
			</table>
		</div>
	</div>

<script>
var startDate = "";

function processPortfolioSize() {
	// jQuery for the list of stock symbols
	$(document).ready(function() {
		$.ajax({url : "/api/all-stock-symbols"}).then(function(data) {
			
			var tableRows;
			for (i = 0; i < portfolioSize.value; i++) {
				tableRows = tableRows + "<tr>";
				tableRows = tableRows + "<td> <select class='form-control' id='stockSymbol" + i 
					+ "' name='stockSymbol" + i + "' onkeyup='getStockNameBySymbol(" + i + ")'"
					+ " onmouseup='getStockNameBySymbol(" + i + ")'>";
					
					for (x = 0; x < data.length; x++) {
						tableRows = tableRows + "<option value='" + data[x].symbol + "'>"
						+ data[x].symbol + "</option>";
					}
					//+ "<option value='AC'>AC</option>"
					//+ "<option value='ALI'>ALI</option>"
					
				tableRows = tableRows + "</select> </td>";
					
				tableRows = tableRows + "<td id='stockName" + i + "'></td>";

				tableRows = tableRows + "<td id='numberOfSharesTableCell" + i + "'>"
					+ "<input type='number' class='form-control' id='numberOfShares" + i + "'"
					+ " name='numberOfShares" + i + "' onchange='processTotalCost(" + i + ")'> </td>";

				tableRows = tableRows + "<td class='text-right' id='totalCost" + i + "'></td>";

				tableRows = tableRows + "</tr>";
			}

			$("#tableBody").empty();
			$("#tableBody").append(tableRows);
		});
	});
}

function getStockNameBySymbol(row) {

	$(document).ready(function() {
		var stockSymbol = $("#stockSymbol" + row);

		$.ajax({url : "/api?symbol=" + stockSymbol.val()}).then(function(data) {
			$("#stockName" + row).text(data.name);
		});

	});
}

function getStartDate() {
	
	$(document).ready(function() {
		var startDate = $("#datePicker");
		console.log("startDate: " + startDate.val());
		$.ajax({url : "/api/startDate/" + startDate.val()}).then(function(data) {
			//$("#startDate" + row).text(data.date);
			if (data.date === "1970-01-01" && data.symbol === "" &&
					data.close === 0.00 && data.volume === 0.00) {
				alert("Please choose another date.  The date you have chosen does not exist in the database" + 
						" or is not a trading day.");
			} else {
				console.log("valid start date: " + data.date);
				startDate = data.date;
			}
		});

	});
}

function getStockSymbolAndNames() {
	console.log("fetching stock symbol and names");
    
	$("#datePicker").datepicker({format: 'yyyy-mm-dd'}).on("changeDate", function(ev) {
		//console.log("ev.date: " + $("#datePicker").val())
		getStartDate();
	});
}

function processTotalCost(row) {
	
	$(document).ready(function() {
		var numberOfShares = $("#numberOfShares" + row);
		var totalCost = numberOfShares.val() * 5;
		$("#totalCost" + row).text(totalCost.toFixed(2));
	});
}
</script>
</body>
</html>