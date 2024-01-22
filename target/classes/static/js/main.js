var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');

var connectForm = document.querySelector('#connectForm')

var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var usernameBox = document.querySelector('#username');
var userIdBox = document.querySelector('#userId')
var conversationIdBox = document.querySelector('#conversationId')

var stompClient = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

var username;
var userId;
var conversationId;

function connect(event) {
    username = usernameBox.value;
    userId = userIdBox.value;
    conversationId = conversationIdBox.value;

    console.log("Inputted username: " + username)
    console.log("Inputted user id: " + userId)
    console.log("Inputted conversation id: " + conversationId)

    if(username) {

        var socket = new SockJS('/ws', null, {
            withCredentials: true
        });
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/queue/' + conversationId, onMessageReceived);
    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            senderId: userId,
            content: messageInput.value,
            conversationId: conversationId
        };
        stompClient.send("/chat/conversation/" + conversationId, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    avatarElement.style['background-color'] = "red";

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.senderId);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);


    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

connectForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)