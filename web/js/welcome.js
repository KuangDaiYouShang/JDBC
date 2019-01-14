/*function onClick(){
    showMessage();
};*/

onload=function(){
    setInterval(showMessage, 3000);
};

var labels = ["one", "two", "three"];
var x = 0;
function showMessage() {
    if(x < 3) {
        document.getElementById("label").innerHTML = document.getElementById(labels[x%3]).innerHTML;
        x++;
    }
}

function onClick() {
    location.href='http://localhost:8080/login.jsp'
}

/*function sleep(numberMillis) {
    var now = new Date();
    var exitTime = now.getTime() + numberMillis;
    while (true) {
        now = new Date();
        if (now.getTime() > exitTime)
            return;
    }
}*/

var button = document.querySelector('button');
button.addEventListener('click', onClick);

