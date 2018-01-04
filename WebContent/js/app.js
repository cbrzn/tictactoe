 function $(id) {
	return document.getElementById(id);
};

var ws = new WebSocket("ws://"+location.host+"/TicTacToe/game");

ws.onmessage = function (e) {
	var grid = JSON.parse(e.data);
	var id = grid.id;
	var token = grid.token;
	console.log(grid);
	$('p1').innerHTML = grid.score_p1;
	$('p2').innerHTML = grid.score_p2;
	$(id).innerHTML = token;
	if (($(0).innerHTML == grid.token && $(1).innerHTML == grid.token && $(2).innerHTML == grid.token) || 
		($(0).innerHTML == grid.token && $(3).innerHTML == grid.token && $(6).innerHTML == grid.token) || 
		($(0).innerHTML == grid.token && $(4).innerHTML == grid.token && $(8).innerHTML == grid.token) || 
		($(3).innerHTML == grid.token && $(4).innerHTML == grid.token && $(5).innerHTML == grid.token) || 
		($(6).innerHTML == grid.token && $(7).innerHTML == grid.token && $(8).innerHTML == grid.token) || 
		($(1).innerHTML == grid.token && $(4).innerHTML == grid.token && $(7).innerHTML == grid.token) || 
		($(2).innerHTML == grid.token && $(5).innerHTML == grid.token && $(8).innerHTML == grid.token) || 
		($(2).innerHTML == grid.token && $(4).innerHTML == grid.token && $(6).innerHTML == grid.token))  {
		$('status').innerHTML = grid.token + " has won!"
		for (var i=0; i<9; i++) {
			$(i).innerHTML = "";
		}
		
	}  
};

	ws.onopen = function (e) {
	};

ws.onclose = function (e) {
	$("inbox").textContent = '\n Connection Finished';
};

function play() {
	var centre = $('test');
	var centreinside = document.createElement("div");
	centre.setAttribute('class', 'centre');
	centreinside.setAttribute('class', 'centreinside');
	centre.appendChild(centreinside);
	var score_one = $('p1').innerHTML = 0;
	var score_two = $('p2').innerHTML = 0;
	for (var i=0; i<9; i++) {
		var grid = document.createElement("div");
		centreinside.appendChild(grid);
		grid.setAttribute('class', 'grid');
		grid.setAttribute('id', i);
		grid.addEventListener('click', function() {
			if ($('status').innerHTML != "") {
				ws.send(JSON.stringify({msg:$('status').innerHTML,
										score_p1:$('p1').innerHTML,
										score_p2:$('p2').innerHTML}));
				$('status').innerHTML = "";
			} else {
			ws.send(JSON.stringify({id:this.id,
									msg:"",
									score_p1:$('p1').innerHTML,
									score_p2:$('p2').innerHTML}));
			}
		})
	}
}

function sendMessage() {
	var test = {
			name: "cesar"

	}
	ws.send(JSON.stringify(test));
}
		


window.onunload = ()=>{
    ws.onclose();
}
	
addEventListener('load', play);