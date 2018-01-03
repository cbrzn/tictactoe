 function $(id) {
	return document.getElementById(id);
};

var ws = new WebSocket("ws://"+location.host+"/TicTacToe/game");

ws.onmessage = function (e) {
	var inc = JSON.parse(e.data);
	var id = inc.id;
	$(id).innerHTML = inc.msg;
	if ($(0).innerHTML == "X" && $(0).innerHTML == "X" && $(2).innerHTML == "X") {
		alert("you have won")
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
	for (var i=0; i<9; i++) {
		var grid = document.createElement("div");
		centreinside.appendChild(grid);
		grid.setAttribute('class', 'grid');
		grid.setAttribute('id', i);
		grid.addEventListener('click', function() {
			ws.send(JSON.stringify({ msg:"X",
					  id:this.id}));
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