# Chat Protocol Documentation
- [Description](https://github.com/DevMushy/Chat-ragusa-duka/tree/master#description)
- [Technologies utilized](https://github.com/DevMushy/chat-ragusa-duka/edit/main/README.md#technologies-utilized)
- [Message Protocol](https://github.com/DevMushy/Chat-ragusa-duka/tree/master#message-protocol)
  + [Client connection](https://github.com/DevMushy/Chat-ragusa-duka/tree/master#client-connection)
  + [Broadcast Message](https://github.com/DevMushy/Chat-ragusa-duka/tree/master#broadcast-message)
  + [Private Message](https://github.com/DevMushy/Chat-ragusa-duka/tree/master#private-message)


## Description
Simple Java Maven Chat using JSON to transfer information from client to server and viceversa, multiple clients can connect to the server currently using the port: 3000.


## Technologies utilized

* Java SE 18
* Xml/Json
* ...

## Message Protocol

### Message Structure

```
{
"sender":"",
"type":"",
"receiver":"*",
"body":""
}
```

### Client connection
![diagramma connessione](https://github.com/DevMushy/Chat-ragusa-duka/blob/master/images/ClientsConnectionDiagram.png)

## Broadcast Message

### Broadcast Message Diagram

![diagramma messaggio broadcast](https://github.com/DevMushy/Chat-ragusa-duka/blob/master/images/BroadcastMessageDiagram.png)





## Private Message

### Private Message Diagram
