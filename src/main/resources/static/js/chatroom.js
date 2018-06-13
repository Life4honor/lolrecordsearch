var webSocket = null;

function setConnected(connected) {
}

function showMessage(message) {
    // console.log(message);
    var josonArray = JSON.parse(message);
    for(var i = 0; i < josonArray.length; i++) {
        var chatDate = new Date(josonArray[i].regDate);
        var strDate = chatDate.getFullYear()+'년'+(chatDate.getMonth()+1)+'월'+chatDate.getDate()+'일 '+chatDate.getHours()+'시'+chatDate.getMinutes()+'분';
        $("#chatArea").append(josonArray[i].nickname +' : ' + josonArray[i].content + ' [' + strDate + ']' + '\n');

        var textArea = $('#chatArea');
        textArea.scrollTop( textArea[0].scrollHeight - textArea.height()   );
    }

}


function connect() {
    webSocket = new SockJS('/ws?chatRoomId='+chatRoomId+'&userId='+userId+'&nickname='+nickname+'&date='+new Date().getTime());

    webSocket.onopen = function (e) {

    };

    webSocket.onmessage = function(e) {
        // console.log(e.data);
        showMessage(e.data);
    }

    webSocket.onclose = function () {
        alert("연결 종료!!");
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
        , 'type' : 'normal'
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