# Daemon Api Documentation v0.3.0

This documentation explains how you can create your own implementation of a Daemon and connect it to our server.

## Connecting

The Java Server supports Unix sockets as well as network sockets. Therefore, we recommend implementing both variants.

| Unix-Sockets                                                 | Network-Sockets                                                                                                                                                     |
|--------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| The Unix-Socket can be reached under `/var/run/cryptic.sock` | The Network-Socket can be reached at the port `4012`. Please note that the network socket only listens on the hostname `localhost` or the IPv4 address `127.0.0.1`. |

In both variant's communication takes place via Json requests and replies.

## Api

### Registering a Daemon on the Server
```json5
{
  "tag": "<Random string, such as a UUID [string]>", // The tag is for assigning the answers to the correct question.
  "endpoint": "daemon/register",
  "response": false,
  "data": {
    "name": "<Name of the Daemon [string]>",
    "functions": [ // List of all functions that can be accessed via the frontend
      {
        "name": "<Name of the Function [string]>",
        "arguments": [ // List of all arguments of the function that the server has validated for you, e.g. whether they are present.
          {
            "name": "<Name of the Function Argument [string]>",
            "required": "<If Specifies whether this argument is mandatory. [boolean]>"
          }
        ]
      }
    ]
  }
}
```

### The frontend is calling an endpoint from your Daemon
```json5
{
  "tag": "<Random string, such as a UUID [string]>", // The tag is for assigning the answers to the correct question.
  "endpoint": "<Function Name from the frontend>",
  "response": false,
  "data": {
    // The Parameters for the Function
  }
}
```

Response:
```json5
{
  "tag": "<The same tag as in the request>",
  "info": {
    "notification": false, // Needed for future features
    "code": 200, // Response Status Code - See the link below
    "name": "OK", // Response Status Name - See the link below
    "error": false // // Response Status Type - See the link below
  },
  "response": true, // Needed to prevent a recursion
  "data": {
    // The Data for the frontend (JsonObject or json Array)
  } 
}
```
[Response Status Codes](../../api/latest/response-codes.html)