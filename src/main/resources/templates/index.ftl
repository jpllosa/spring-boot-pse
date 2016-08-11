<!DOCTYPE html>
<html>
<head>
  <title>${title}</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
  <h1 class="text-center">${title}</h1>
  <p>Description here...</p>
  <p>Usage instruction here...</p>
  <div class="form-group">
  	<label for="portfolioSize">Number of Stocks in your Portfolio</label>
  	<select class="form-control" id="portfolioSize" name="portfolioSize" onmouseup="processPortfolioSize()">
  		<option value="1">1</option>
  		<option value="2">2</option>
  		<option value="3">3</option>
  		<option value="4">4</option>
  		<option value="5">5</option>
	</select>
  </div>
  <div class="form-group">
	<label for="yearsHeld">Number of Years Invested</label>
	<select class="form-control" type="date" id="yearsHeld" name="yearsHeld">
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
  <div class="table-responsive">
  	<h3 class="text-center">Portfolio</h3>
	  <table class="table; table-bordered; table-striped">
	    <thead>
	      <tr>
	        <th>Stock Symbol</th>
	        <th>Stock Name</th>
	        <th>Shares</th>
	        <th>Total Cost</th>
	      </tr>
	    </thead>
	    <tbody>
	      <tr>
	        <td>XXXX</td>
	        <td>XXXX Corp.</td>
	        <td>100</td>
	        <td>100.00</td>
	      </tr>
	    </tbody>
	  </table>
  </div>
</div>

<script>
function processPortfolioSize() {
	// jQuery for the list of stock symbols
	var portfolioSize = document.getElementById("portfolioSize");
    console.log("in function processPortfolioSize");
    console.log(portfolioSize.value);
}
</script>
</body>
</html>