var webSocket = null;

function setConnected(connected) {
}

function showMessage(message) {
    // console.log(message);
    var josonArray = JSON.parse(message);
    for(var i = 0; i < josonArray.length; i++) {
        $("#chatArea").append(josonArray[i].nickname +' : ' + josonArray[i].content + '\n');

        var textArea = $('#chatArea');
        textArea.scrollTop( textArea[0].scrollHeight - textArea.height()   );
    }

}


function connect() {
    webSocket = new SockJS('/ws?chatRoomId='+chatRoomId+'&userId='+userId+'&nickname='+nickname+'&date='+new Date().getTime());

    webSocket.onopen = function (e) {

    };

    webSocket.onmessage = function(e) {
        console.log(e.data);
        showMessage(e.data);
    }

    webSocket.onclose = function () {
        alert("연결 끊김!!");
    }
}

function disconnect() {
    if (webSocket != null) {
        webSocket.close();
    }

    console.log("Disconnected");
}

function send() {

    var chatMessage = {
        'chatRoomId': chatRoomId
        , 'userId' : userId
        , 'nickname' : nickname
        , 'content': $("#chatInput").val()
        , 'date' : new Date()
    };

    webSocket.send(JSON.stringify(chatMessage));
    $("#chatInput").val('');
    $("#chatInput").focus();
}


$(function () {

    connect();

    $("#chatInput").keyup(function(e) {

        if (e.keyCode == 13){
            send();

        }
    });

    $( "#sendBtn" ).click(function() { send(); });
});