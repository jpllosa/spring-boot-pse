<!DOCTYPE html>
<html>
<head>
<title>${title}</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body onload="fetchStockSymbolAndNames()">
	<div class="container">
		<h1 class="text-center">${title}</h1>
		<p>Description here...</p>
		<p>Usage instruction here...</p>
		<div class="form-group">
			<label for="portfolioSize">Number of Stocks in your Portfolio</label>
			<select class="form-control" id="portfolioSize" name="portfolioSize"
				onkeyup="processPortfolioSize()" onmouseup="processPortfolioSize()">
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
		<div class="form-group">
			<label for="startDate">Investment Start Date</label>
			<input class="form-control" type="date" id="startDate" name="startDate" value="yyyy-MM-dd">
		</div>
		<h3 class="text-center">Portfolio</h3>
		<div class="table-responsive">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="col-md-2">Stock Symbol</th>
						<th class="col-md-6">Stock Name</th>
						<th class="col-md-2">Shares</th>
						<th class="col-md-2">Total Cost</th>
					</tr>
				</thead>
				<tbody id="tableBody">

				</tbody>
			</table>
		</div>
	</div>

<script>
function processPortfolioSize() {
	// jQuery for the list of stock symbols

	var tableRows;
	for (i = 0; i < portfolioSize.value; i++) {
		tableRows = tableRows + "<tr>";
		tableRows = tableRows + "<td> <select class='form-control' id='stockSymbol" + i 
			+ "' name='stockSymbol" + i + "' onkeyup='getStockNameBySymbol(" + i + ")'"
			+ " onmouseup='getStockNameBySymbol(" + i + ")'>"
			+ "<option value='AC'>AC</option>"
			+ "<option value='ALI'>ALI</option> </select> </td>";
			
		tableRows = tableRows + "<td id='stockName" + i + "'></td>";

		tableRows = tableRows + "<td id='numberOfShares" + i + "'>"
			+ "<input type='number' class='form-control' id='numberOfShares" + i + "'"
			+ " name='numberOfShares" + i + "'> </td>";

		tableRows = tableRows + "<td id='totalCost" + i + "'>100.00</td>";

		tableRows = tableRows + "</tr>";
	}

	$(document).ready(function() {
		$("#tableBody").empty();
	});

	$(document).ready(function() {
		$("#tableBody").append(tableRows);
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

function fetchStockSymbolAndNames() {
	console.log("fetching stock symbol and names");
}
</script>
</body>
</html>