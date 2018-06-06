var webSocket = null;

function setConnected(connected) {
}

function showMessage(message) {
    // console.log(message);
    var jsonMessage = JSON.parse(message);
    $("#chatArea").append(jsonMessage.nickname +' : ' + jsonMessage.content + '\n');

    var textArea = $('#chatArea');
    textArea.scrollTop( textArea[0].scrollHeight - textArea.height()   );

}


function connect() {
    webSocket = new SockJS('/ws?chatRoomId='+chatRoomId);
    webSocket.onmessage = function(e) {
        console.log(e.data);
        showMessage(e.data);
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