# regurgitator-core-yml

regurgitator is a lightweight, modular, extendable java framework that you configure to 'regurgitate' canned or clever responses to incoming requests; useful for quickly mocking or prototyping services without writing any code. simply configure, deploy and run.

start your reading here: [regurgitator-all](http://github.com/talmeym/regurgitator-all#regurgitator)

## yml configuration of regurgitator

below is an example of a yml configuration file for regurgitator:

```yml
sequence:
 id: my-sequence
 steps:
 - create-parameter: 
    id: my-step-1 
    name: response
    value: this is the response 
 - create-response: 
    source: response 
```

### step ids

all steps in a regurgitator configuration can be given an ``id`` property. ids can be used for identifying which step to run next (see [decision](https://github.com/talmeym/regurgitator-core-yml#decision), below) and therefore must be unique. if no id property is given for a step, a system-generated one will be assigned to it at load time, combining the type of the step with a 4 digit randon number, eg: ``create-parameter-6557``

## core steps in yml

### sequence

a sequence is a step that executes a series of child steps, one after another in order

```yml
sequence:
 steps:
 - create-parameter:
    name: response
    value: this is the response
 - create-response:
    source: response
```

by default, when each child step executes, it is passed the same message object that the sequence received. it is possible to "isolate" a sequence's child steps from the data contained in the message object, by giving the sequence an isolation level. this prevents a child step from receiving data it shouldn't see or that it won't need.

```yml
sequence:
 isolate: with-parameters 
 steps:
 - create-parameter
    name: response
    value: this is the response
 - create-response
    source: response
```

isolation has 4 settings:

| value | child step receives |
| :--- | :--- |
| ``true`` | new blank message object |
| ``with-parameters`` | new message object containing the parameters context of the original message |
| ``with-session`` | new message object containing the session context of the original message |
| ``with-parameters-and-session`` | new message object containing parameters and session of the original message |

### decision

a decision executes one or more child steps, using ``rules`` and ``conditions`` to determine which steps to run

```yml
decision:
 steps:
 - create-response:
    id: default-response
    value: this is the default response
 - create-response:
    id: special-response
    value: this is the special response
 default-step: default-response
 rules:
 - step: special-response
   conditions:
   - source: parameters:special
     equals: true
```

upon execution a decision evaluates all of its rules to see which pass. it then uses its ``rules behaviour`` to determines which of the passed rules should have their corresponding step executed. the default rules behaviour is ``first-match`` whereby the first rule that passes provides the step to be executed.

there are 3 core rules behaviours:

| value | behaviour |
| :--- | :--- |
| ``first-match`` | execute the step of the first rule passed |
| ``first-match-onwards`` | execute the step of the first rule passed, and all subsequent steps |
| ``all-matches`` | execute the steps of all passed rules |

each rule has one or more conditions that must be satisfied to make the rule pass. each condition evaluates the value of a parameter within the message object, specified by the ``source`` property, against an operand. each condition has a ``condition behaviour`` that dictates the manner in which the value is evaluated against the operand. the example above uses the ``equals`` condition behaviour, specified as a property named 'equals'.

the behaviour of a condition can also be specified as a 'behaviour' property, either as a string:

```yml
...
default-step: default-step
rules:
- step: special-step
  conditions:
  - source: parameters:special
    value: special
    behaviour: equals
...
```

or as an object:

```yml
...
default-step: no-id-found
rules:
- step: found-id
  conditions:
  - source: parameters:xml
    value: /rg:config/@id
    behaviour:
     contains-xpath:
      namespaces:
       rg: http://url.com
...
```

which allows some condition behaviours to have properties besides the operand (in the example above, the namespaces of the xpath specified). if behaviour is specified in a behaviour property, the operand is specified in the value property.

there are 6 core condition behaviours:

| value | behaviour |
| :--- | :--- |
| ``equals`` | checks the parameter value equals the operand |
| ``equals-param`` | checks the parameter value equals the value of another parameter |
| ``exists`` | checks the parameter value exists (read as 'parameter exists') |
| ``contains`` | checks the parameter value contains the operand |
| ``contains-param`` | checks the parameter value contains the value of another parameter |
| ``matches`` | checks the parameter values matches a regex |

### create-parameter

a create-parameter creates a parameter in the message, with a type and a value

```yml
create-parameter:
 name: index
 type: NUMBER
 value: 5
 merge: CONCAT
```

a create-parameter can have one of the following value sources:

| property | value source | example |
|:---|:---|:---|
| ``source`` | value drawn from a source parameter | ``request-metadata:query-param`` |
| ``value`` | value provided explicitly | ``arg1=this,arg2=that`` |
| ``file`` | value loaded from a file | ``classpath:/query-param.txt`` |

a create-parameter can have a ``merge`` property that specifies a ``conflict policy`` defining what to do if the parameter being created already exists in the message. both the ``merge`` and ``type`` properties are optional, with their defaults being ``REPLACE`` and ``STRING``, respectively.

there are 4 core conflict policies available:

| merge type | behaviour | ``STRING`` example | result |
|:---|:---|:---|:---|
| ``LEAVE`` | leave the existing value in place | existing: ``some`` new: ``thing`` | ``some`` |
| ``REPLACE`` | replace the existing value with the new | existing: ``some`` new: ``thing`` | ``thing`` |
| ``CONCAT`` | concatenate the existing and new values | existing:``some`` new: ``thing`` | ``something`` |
| ``REMOVE`` | remove the new value from the existing | existing:``some`` new: ``me`` | ``so`` |

when using ``NUMBER`` and ``DECIMAL`` parameter types, ``CONCAT`` and ``REMOVE`` conflict policies behave as addition and subtraction operators. when collection-based parameter types are used, ``CONCAT`` and ``REMOVE`` behaves like java collection ``add-all`` and ``remove-all`` operations, respectively.

find more details on parameter types in [regurgitator-core](https://github.com/talmeym/regurgitator-core#parameter-types).

to create a parameter in a particular parameter context, simple prepend your parameters name with the desired context, separated by a colon, eg: 

```yml
create-parameter:
 name: context:param-name
 value: some value
``` 

to draw a source parameter value from a particular context, use the same notation in the ``source`` property, eg: 

```yml
create-parameter:
 name: some-param
 source: context:other-param
```

#### value-processors

all steps that create parameters (as well as ``create-response``) have the ability to include a ``value-processor``. this is extra processing that is applied to the steps value after it is built / generated / retrieved, as seen below:

```yml 
create-parameter:
 name: positive-spin
 value: you are unhappy
 processor:
  substitute-processor:
   token: un
   replacement: "very " 
```

the manner in which the value is processed depends on the processor included.

### build-parameter

a build-parameter creates a parameter in the message, with it's value provided by a ``value-builder``

```yml
build-parameter:
 name: response
 type: STRING
 merge: CONCAT
 builder:
  freemarker-builder:
   file: classpath:/response_file.ftl
```

as with create-parameter above, a build-parameter can have optional ``merge`` and ``type`` properties, their defaults being ``REPLACE`` and ``STRING``, respectively.

### generate-parameter

a generate-parameter creates a parameter in the message, with it's value generated by a ``value-generator``

```yml
generate-parameter:
 name: random-number
 type: NUMBER
 merge: REPLACE
 generator:
  number-generator:
   max: 10
```

as with create-parameter above, a generate-parameter can have optional ``merge`` and ``type`` properties, their defaults being ``REPLACE`` and ``STRING``, respectively.

generators that require no configuration can be specified as a string property, rather than as an object, eg:

```yml
generate-parameter:
 name: new-id
 type: STRING
 generator: uuid-generator
```

### create-response

a create-response returns a response back from regurgitator via a message's ``response-callback`` mechanism. 

```yml
create-response:
 source: parameters:response-text

create-response:
 value: <xml>this is the response</xml>

create-response:
 file: classpath:/canned_response.xml
```
a create-response can have the same value sources as create-parameter, ``source``, ``value``, or ``file``. 

regurgitator can be configured to return as many responses as is desired, but that may be incompatible with some single request / response usages, such as over [http](https://github.com/talmeym/regurgitator-extensions-web#regurgitator-over-http).

### identify-session

an identify-session step allows you to create or retrieve a cached session parameter context for your message object.

```yml
identify-session:
 source: request-metadata:http-session-id
```

having identified your session, you can store and retrieve parameters from the session context as you would any other context, eg:

```yml
create-parameter:
 name: session:stored-value
 value: store this

create-parameter:
 name: local-parameter
 source: session:stored-value
```

### record-message

a record-message step allows you to output the contents of a ``message`` to either a file or to standard out.

```yml
record-message:
 folder: /users/miles/regurgitator/messages
```

the output is a json document representing the message state, showing each ``context`` and their ``parameter`` values grouped, eg.

```json
{
    "request-headers": {
        "accept": "*/*",
        "accept-encoding": "gzip",
        "connection": "Keep-Alive",
        "content-type": "application/json",
        "cookie": "JSESSIONID=E830BD8B9217BA34441A0DDABBAB7F9E",
        "host": "localhost:9090",
        "user-agent": "Mozilla/5.0"
    },
    "request-metadata": {
        "content-length": "-1",
        "content-type": "application/json",
        "http-session-id": "E830BD8B9217BA34441A0DDABBAB7F9E",
        "local-address": "127.0.0.1",
        "local-name": "localhost",
        "local-port": "9090",
        "method": "GET",
        "path-info": "/app/resource/123",
        "protocol": "HTTP/1.1",
        "query-string": "country=GB&currency=GBP",
        "remote-address": "127.0.0.1",
        "remote-host": "127.0.0.1",
        "request-uri": "/app/resource/123",
        "requested-session-id": "E830BD8B9217BA34441A0DDABBAB7F9E",
        "scheme": "http",
        "server-name": "localhost",
        "server-port": "9090"
    }
}
```

if a folder is ommitted, the message state is output to standard out. 

## core constructs in yml

### number-generator

a number-generator generates a random number parameter value.

```yml
generate-parameter:
 name: random-number
 type: NUMBER
 generator:
  number-generator:
   max: 1000
```

the ``max`` property is optional:

```yml
generate-parameter:
 name: unrestricted-random-number
 type: NUMBER
 generator: number-generator
```

more info on ``value-generator`` [here](https://github.com/talmeym/regurgitator-core-yml#generate-parameter).

### uuid-generator

a uuid-generator generates a uuid parameter value.

```yml
generate-parameter:
 name: new-id
 type: STRING
 generator:
  uuid-generator:

generate-parameter:
 name: new-id
 type: STRING
 generator: uuid-generator
```

more info on ``value-generator`` [here](https://github.com/talmeym/regurgitator-core-yml#generate-parameter).

### extract-processor

an extract-processor extracts a value from another value, using the [java.text.MessageFormat](https://docs.oracle.com/javase/7/docs/api/java/text/MessageFormat.html) syntax.

```yml
create-parameter:
 name: customer-id
 source: request-metadata:query-string
 processor:
  extract-processor:
   format: order={0}&amp;customer={1}
   index: 1
```

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-yml#value-processors).

### substitute-processor

a substitute-processor manipulates ``STRING`` values, replacing occurrences of one value with another.

```yml
create-parameter:
 name: positive-spin
 value: you are unhappy
 processor:
  substitute-processor:
  token: un
  replacement: "very "
```

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-yml#value-processors).

### index-processor

an index-processor manipulates collection parameter values, such as ``LIST_OF_STRING``, returning the data value at a given index, starting from zero.

```yml
create-parameter:
 name: data
 type: LIST_OF_STRING
 value: not this one,or this one,but this one

create-parameter:
 name: the-one
 type: STRING
 source: data
 processor:
  index-processor:
   index: 2
```

the index to be looked up can be specified using either a ``value`` property, or ``source`` to retrieve the index from a parameter.

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-yml#value-processors).

### index-of-processor

an index-of-processor manipulates collection parameter values, such as ``LIST_OF_STRING``, returning the index of a given data value, starting from zero.

```yml
create-parameter:
 name: data
 type: LIST_OF_STRING
 value: not this one,or this one,but this one

create-parameter:
 name: index-of-the-one
 type: NUMBER
 source: data
 processor:
  index-of-processor:
   value: but this one
```

the data value to be looked up can be specified using either a ``value`` property, or ``source`` to retrieve the data value from a parameter.

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-yml#value-processors).

### size-processor

a size-processor manipulates collection parameter values, such as ``LIST_OF_STRING``, returning the size of the collection

```yml
create-parameter:
 name: data
 type: LIST_OF_STRING
 value: one,two,three,four

create-parameter:
 name: data-size
 type: NUMBER
 source: data
 processor: size-processor
```

the optional ``as-index`` property returns the size zero-indexed, eg. 3 items returning a value of 2. without this property, the processor can be in-lined, as shown above.

```yml
create-parameter:
 name: data
 type: LIST_OF_STRING
 value: one,two,three,four

create-parameter:
 name: four
 type: NUMBER
 source: data
 processor:
  size-processor:
   as-index: true
```

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-yml#value-processors).
