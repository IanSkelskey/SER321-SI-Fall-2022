# Assignment 4 Activity 1
## Description
The initail Performer code only has one function for adding strings to an array: 

## Protocol

### Requests
request: { "selected": <int: 1=add, 2=clear, 3=display, 4=remove, 5=index,
0=quit>, "data": <thing to send>}

  add: data <string> -- a string to add to the list
  clear: data <> -- no data given, clears the whole list
  display: data <> -- no data given, displays the whole list
  remove: data <int> -- integer of index in list that should be removed
  index: data <int> -- integer of index in list that should be displayed

### Responses

sucess response: {"ok" : true, type": <"add",
"pop", "display", "count", "switch", "quit"> "data": <thing to return> }

type <String>: echoes original selected from request
data <string>: 
    add: return complete list
    clear: return empty list
    display: return complete list
    remove: return removed item
    index: return item at index


error response: {"ok" : false, "message"": <error string> }
error string: Should give good error message of what went wrong


## How to run the program
### Terminal
Base Code, please use the following commands:
```
    For Server, run "gradle runServer -Pport=9099 -q --console=plain"
```
```   
    For Client, run "gradle runClient -Phost=localhost -Pport=9099 -q --console=plain"
```   



