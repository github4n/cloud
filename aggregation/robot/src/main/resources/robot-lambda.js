'use strict';
const http = require('http'); 
const USER_DEVICES = [];
const HOSTNAME = "220.160.105.203";
const HOSTPORT = "8880";
const CONTEXT =  {
      'namespace': '',
      'name': '',
      'value': '',
      'timeOfSample': '2017-02-03T16:20:50.52Z',
      'uncertaintyInMilliseconds': 500
    }; 
const HEADER =  {
      'namespace': 'Alexa',
      'name': 'Response',
      'payloadVersion': '3',
      'messageId': '5f8a426e-01e4-4cc9-8b79-65f8bd0fd8a4'
    } 
    
    
const CONTROLRESPONSE_SUCCESS = {
  'context': {
    'properties': [ ]
  },
  'event': {
    'header': {},
    'endpoint': {
      'endpointId': ''
    },
    'payload': {}
  }
};
const CONTROLRESPONSE_FAIL = {
  'event': {
    'header': {},
    'endpoint': {
      'endpointId': ''
    },
    'payload': {}
  }
};

function log(title, msg) {
    console.log(`[${title}] ${msg}`);
}
function generateMessageID() {
    return '38A28869-DD5E-48CE-BBE5-A4DB78CECB28'; // Dummy
}
function generateResponse(name, payload) {
    return {
        header: {
            messageId: generateMessageID(),
            name: name,
            namespace: 'Alexa.ConnectedHome.Control',
            payloadVersion: '2',
        },
        payload: payload,
    };
}
function colorResponse(vcolor){
    return{
        achievedState : {
            color :vcolor,
        }
    };
}
//设备发现接口调用
function discoveryAll(messageId, token, success){
   var options = {  
    	      hostname:HOSTNAME,
    	      port:HOSTPORT,  
    	      path:'/robot/alexa/discovery', 
    	      method:'GET',
    	      headers:{
	            messageid: messageId,
	            accesstoken: token,
	            payloadversion: '3'
    	      }
    	}
    var req = http.request(options, function (res) { 
        console.log('STATUS: ' + res.statusCode);  
        console.log('HEADERS: ' + JSON.stringify(res.headers));  
        res.setEncoding('utf8');  
        var back = {};
            
        res.on('data', function (body) {
            var bodyJ = JSON.parse(body);
            console.log('BODY: ' + body);  
            console.log('BODY: ' + bodyJ.code); 
            console.log('BODY: ' + bodyJ.data); 
            console.log('BODY: ' + bodyJ.data.payload); 
            console.log('BODY: ' + bodyJ.data.payload.endpoints); 
            back.event = bodyJ.data;
            success(back);
        });  
    }); 
    req.end();
}
function controlAll(reqs,token,correlationToken,namespace,callback){
    var endpointId = reqs.endpointId;
    var contents = JSON.stringify(reqs);
    var options = {  
  	      hostname:HOSTNAME,
  	      port:HOSTPORT,  
  	      path:'/robot/alexa/control',
  	      method:'POST',
  	      headers:{
	            messageid: generateMessageID(),
	            accesstoken: token,
	            correlationtoken: correlationToken,
	            payloadVersion: '3',
	            'Content-Type':'application/json',
	            'Content-Length':contents.length
  	      }
  	}
    var req = http.request(options, function (res) { 
      	 res.on('data', function (body) {
      	    var bodyJ = JSON.parse(body);
            console.log('BODY: ' + body); 
            console.log('BODY: ' + bodyJ.code); 
            console.log('BODY: ' + bodyJ.data); 
            callback(null, convertRes(bodyJ,reqs.name, namespace,endpointId))
      	 })
    }); 
    req.write(contents);
    req.end();
}
function discoverDevices(messageId, token, success){
    discoveryAll(messageId, token,success);
}

function getDevicesFromPartnerCloud() {
    return USER_DEVICES;
}

function isValidToken() {
    return true;
}

function isDeviceOnline(applianceId) {
    log('DEBUG', `isDeviceOnline (applianceId: ${applianceId})`);
    return true;
}

function handleDiscovery(directive, context,callback) {
    log('DEBUG', `Discovery Request: ${JSON.stringify(directive)}`);
    var userAccessToken = directive.payload.scope.token.trim();
    var messageId = directive.header.messageId;
    if (!userAccessToken) {
        const errorMessage = `Discovery Request [${messageId}] failed. Invalid access token: ${userAccessToken}`;
        log('ERROR', errorMessage);
        callback(new Error(errorMessage));
    }
    discoverDevices(messageId, userAccessToken,function (endpoint) {
    		context.succeed(endpoint);
    });   
}

function handleControl(directive, callback) {
    log('DEBUG', `Control Request: ${JSON.stringify(directive)}`);
    var userAccessToken = directive.endpoint.scope.token.trim();
    var correlationToken = directive.header.correlationToken.trim();
    var namespace = directive.header.namespace;
    var messageId = directive.header.messageId;
    var endpointId = directive.endpoint.endpointId;
    if (!userAccessToken || !isValidToken(userAccessToken)) {
        log('ERROR', `Discovery Request [${messageId}] failed. Invalid access token: ${userAccessToken}`);
        callback(null, generateResponse('InvalidAccessTokenError', {}));
        return;
    }
    if (!endpointId) {
        log('ERROR', 'No endpointId provided in request');
        const payload = { faultingParameter: `applianceId: ${endpointId}` };
        callback(null, generateResponse('UnexpectedInformationReceivedError', payload));
        return;
    }
    if (!isDeviceOnline(endpointId, userAccessToken)) {
        log('ERROR', `Device offline: ${endpointId}`);
        callback(null, generateResponse('TargetOfflineError', {}));
        return;
    }
    console.log(directive.header.name);
    var req = {
    	name : directive.header.name,
    	payload : directive.payload,
    	endpointId : endpointId,
    }
    controlAll(req,userAccessToken,correlationToken,namespace,callback);
}

var cloneObj = function(obj){
    var str, newobj = obj.constructor === Array ? [] : {};
    if(typeof obj !== 'object'){
        return;
    } else if(JSON){
        str = JSON.stringify(obj), //系列化对象
        newobj = JSON.parse(str); //还原
    }
    return newobj;
}

function convertRes(response,name, namespace,endpointId) {
	let res = {};
	let header = cloneObj(HEADER);
	log("res:",response.code);
	if(response.code == 200){
	    res = cloneObj(CONTROLRESPONSE_SUCCESS);
	    if(namespace == "Alexa.SceneController") {
	        header.namespace = "Alexa.SceneController";
	        header.name = "ActivationStarted";
	        res.context = {};
	        res.event.payload= {"cause":{"type":"VOICE_INTERACTION"},"timestamp" : "2017-02-03T23:23:23.23Z"};
	    }else if (name == "ReportState") {
	        var datas = response.data;
	        for(var i = 0; i < datas.length; i++) {
	            var data = datas[i];
	            let context = cloneObj(CONTEXT);
	            context.namespace = data.namespace;
        	    context.name = data.name;
        	    context.value = data.value;
	            res.context.properties.push(context);
	        }
	        let context = cloneObj(CONTEXT);
            context.namespace = "Alexa.EndpointHealth";
    	    context.name = "connectivity";
    	    context.value = {"value": "OK"};
	        header.name = "StateReport";
	    }else {
	        var datas_ = response.data;
	        for(var i = 0; i < datas_.length; i++) {
	            var data_ = datas_[i];
	            let context = cloneObj(CONTEXT);
	            context.namespace = data_.namespace;
        	    context.name = data_.name;
        	    context.value = data_.value;
	            res.context.properties.push(context);
	        }
	       log("control-result----------:",JSON.stringify(datas_));
	        
    	   // let context = cloneObj(CONTEXT);
    	   // context.namespace = namespace;
    	   // context.name = response.data.name;
    	   // context.value = response.data.value;
    	   // res.context.properties.push(context);
	    }
	} else {
		res = cloneObj(CONTROLRESPONSE_FAIL);
		header.name = "ErrorResponse";
		res.event.payload = {
		      'type': 'ENDPOINT_UNREACHABLE',
		      'message': 'Unable to reach endpoint because it appears to be offline'
		    }
	}
	res.event.header = header;
    res.event.endpoint.endpointId = endpointId;
    log("control-result:",JSON.stringify(res));
    log("CONTROLRESPONSE_SUCCESS:",JSON.stringify(CONTROLRESPONSE_SUCCESS));
	return res;
}

exports.handler = (request, context, callback) => {
	var namespace =  request.directive.header.namespace;
	if (namespace == "Alexa.Discovery") {
		handleDiscovery(request.directive,context,callback);
	} else {
		handleControl(request.directive, callback);
	}
};

