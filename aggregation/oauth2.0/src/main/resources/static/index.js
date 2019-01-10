'use strict';

/**
 * This sample demonstrates a smart home skill using the publicly available API on Amazon's Alexa platform.
 * For more information about developing smart home skills, see
 *  https://developer.amazon.com/alexa/smart-home
 *
 * For details on the smart home API, please visit
 *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference
 */

/**
 * Mock data for devices to be discovered
 *
 * For more information on the discovered appliance response please see
 *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#discoverappliancesresponse
 */
const USER_DEVICES = [
    {
        // This id needs to be unique across all devices discovered for a given manufacturer
        applianceId: 'unique-id-for-non-dimmable-bulb-specific-to-user1',
        // Company name that produces and sells the smart home device
        manufacturerName: 'SmartHome Product Company',
        // Model name of the device
        modelName: 'NON-DIMMABLE BULB MODEL ABC',
        // Version number of the product
        version: '1.0',
        // The name given by the user in your application. Examples include 'Bedroom light' etc
        friendlyName: 'Smart light',
        // Should describe the device type and the company/cloud provider.
        // This value will be shown in the Alexa app
        friendlyDescription: 'Smart light bulb from SmartHome Product Company',
        // Boolean value to represent the status of the device at time of discovery
        isReachable: true,
        // List the actions the device can support from our API
        // The action should be the name of the actions listed here
        // https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#discoverappliancesresponse
        actions: ['turnOn', 'turnOff'],
        // not used at this time
        additionalApplianceDetails: {
            extraDetail1: 'optionalDetailForSkillAdapterToReferenceThisDevice',
            extraDetail2: 'There can be multiple entries',
            extraDetail3: 'but they should only be used for reference purposes.',
            extraDetail4: 'This is not a suitable place to maintain current device state',
        },
    }, {
        // This id needs to be unique across all devices discovered for a given manufacturer
        applianceId: 'unique-id-for-dimmable-bulb-specific-to-user1',
        // Company name that produces and sells the smart home device
        manufacturerName: 'SmartHome Product Company',
        // Model name of the device
        modelName: 'DIMMABLE BULB MODEL XYZ',
        // Version number of the product
        version: '1.0',
        // The name given by the user in your application. Examples include 'Bedroom light' etc
        friendlyName: 'Dimmable light',
        // Should describe the device type and the company/cloud provider.
        // This value will be shown in the Alexa app
        friendlyDescription: 'Dimmable light bulb from SmartHome Product Company',
        // Boolean value to represent the status of the device at time of discovery
        isReachable: true,
        // List the actions the device can support from our API
        // The action should be the name of the actions listed here
        // https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#discoverappliancesresponse
        actions: ['turnOn', 'turnOff', 'setPercentage', 'incrementPercentage', 'decrementPercentage'],
        // not used at this time
        additionalApplianceDetails: {
        },
    },
];

/**
 * Utility functions
 */

function log(title, msg) {
    console.log(`[${title}] ${msg}`);
}
/*discovery*/
function discoverDevices(token, success, failure) {

    //return true if a value in the first group is contained in the second group
    var matchesGroup = function(groups1, groups2){
      for(var num in groups1 ){
        if(groups2.indexOf(groups1[num]) >= 0 )
          return true;
      }
      return false;
    };

    //checks for a Fahrenheit tag and sets the righ property on the
    //additionalApplianceDetails response object
    var setTempFormat = function(item, additionalApplianceDetails){
      if (item.tags.indexOf('Fahrenheit') > -1 || item.tags.indexOf('fahrenheit') > -1) {
          additionalApplianceDetails.temperatureFormat = 'fahrenheit';
      } else {
          additionalApplianceDetails.temperatureFormat = 'celsius';
      }
    };

    //callback for successfully getting items from rest call
    var getSuccess = function (items) {
        log.debug('discoverDevices getSuccess: ' + JSON.stringify(items));
        var discoverdDevices = [];
        var thermostatGroups = [];

        //first retrive any thermostat Groups
        (function () {
          for (var itemNum in items) {
            var item = items[itemNum];
            for (var tagNum in item.tags) {
              var tag = item.tags[tagNum];
              if(tag == 'Thermostat' && item.type === 'Group'){
                thermostatGroups.push(item.name);
              }
            }
          }
        })();

        //now retieve all other items
        (function () {
          for (var itemNum in items) {
              var item = items[itemNum];
              for (var tagNum in item.tags) {
                  var tag = item.tags[tagNum];
                  var actions = null;
                  var additionalApplianceDetails = {};
                  switch (tag) {
                  case 'Lock':
                      actions = [
                          'getLockState',
                          'setLockState'
                      ];
                      break;
                  case 'Lighting':
                  case 'Switchable':
                      actions = getSwitchableActions(item);
                      break;
                  case 'CurrentTemperature':
                    //if this is not part of a thermostatGroup then add it
                    //standalone otherwise it will be available as a thermostat
                    if(!matchesGroup(thermostatGroups, item.groupNames)){
                      actions = [
                          'getTemperatureReading'
                      ];
                      setTempFormat(item,additionalApplianceDetails);
                    }
                    break;
                  case 'homekit:HeatingCoolingMode':
                  case 'TargetTemperature':
                      break;
                  case 'Thermostat':
                      //only group items are allowed to have a Temperature tag
                      if (item.type === 'Group') {
                          actions = [
                              'setTargetTemperature',
                              'incrementTargetTemperature',
                              'decrementTargetTemperature',
                              'getTargetTemperature',
                              'getTemperatureReading'
                          ];
                          setTempFormat(item,additionalApplianceDetails);
                      }
                      break;
                  default:
                      break;
                  }
                  if (actions !== null) {
                      // DEBUG
                      log.trace(' Discovery adding ' + item.name + ' with tag: ' + tag);
                      additionalApplianceDetails.itemType = item.type;
                      additionalApplianceDetails.itemTag = tag;
                      additionalApplianceDetails.openhabVersion = '2';
                      var discoverdDevice = {
                          actions: actions,
                          applianceId: item.name,
                          manufacturerName: 'openHAB',
                          modelName: tag,
                          version: '2',
                          friendlyName: item.label,
                          friendlyDescription: item.type + ' ' + item.name + ' ' + tag + ' via openHAB',
                          isReachable: true,
                          additionalApplianceDetails: additionalApplianceDetails
                      };
                      discoverdDevices.push(discoverdDevice);
                  }
              }
          }
        })();
        success(discoverdDevices);
    };
    rest.getItems(token, getSuccess, failure);
}
/*discovery*/


/*httputil*/

function getItems(token, success, failure) {
    return getItem(token, null, success, failure);
}
function getItem(token, itemName, success, failure) {
    var options = httpItemOptions(token, itemName);
    https.get(options, function (response) {
            if (response.statusCode != 200) {
                failure({
                    message: 'Error response ' + response.statusCode
                });
                return;
            }
            var body = '';
            response.on('data', function (data) {
                body += data.toString('utf-8');
            });
            response.on('end', function () {
                var resp = JSON.parse(body);
                success(resp);
            });
            response.on('error', function (e) {
                failure(e);
            });
        })
        .end();
}
function httpItemOptions(token, itemname, method, length) {
    var options = {
        hostname: 54.172.186.45,
        port: 8080,
        path: '/Microservices-web/smartHome' + (itemname || ''),
        method: method || 'GET',
        headers: {}
    };

    if (config.userpass) {
        options.auth = config.userpass;
    } else {
        options.headers['Authorization'] = 'Bearer ' + token;
    }

    if (method === 'POST' || method === 'PUT') {
        options.headers['Content-Type'] = 'text/plain';
        options.headers['Content-Length'] = length;
    }
    return options;
}
/*httputil*/

/**
 * Generate a unique message ID
 *
 * TODO: UUID v4 is recommended as a message ID in production.
 */
function generateMessageID() {
    return '38A28869-DD5E-48CE-BBE5-A4DB78CECB28'; // Dummy
}

/**
 * Generate a response message
 *
 * @param {string} name - Directive name
 * @param {Object} payload - Any special payload required for the response
 * @returns {Object} Response object
 */
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

/**
 * Mock functions to access device cloud.
 *
 * TODO: Pass a user access token and call cloud APIs in production.
 */

function getDevicesFromPartnerCloud() {
    /**
     * For the purposes of this sample code, we will return:
     * (1) Non-dimmable light bulb
     * (2) Dimmable light bulb
     */
    return USER_DEVICES;
}

function isValidToken() {
    /**
     * Always returns true for sample code.
     * You should update this method to your own access token validation.
     */
    return true;
}

function isDeviceOnline(applianceId) {
    log('DEBUG', `isDeviceOnline (applianceId: ${applianceId})`);

    /**
     * Always returns true for sample code.
     * You should update this method to your own validation.
     */
    return true;
}

function turnOn(applianceId) {
    log('DEBUG', `turnOn (applianceId: ${applianceId})`);

    // Call device cloud's API to turn on the device

    return generateResponse('TurnOnConfirmation', {});
}

function turnOff(applianceId) {
    log('DEBUG', `turnOff (applianceId: ${applianceId})`);

    // Call device cloud's API to turn off the device

    return generateResponse('TurnOffConfirmation', {});
}

function setPercentage(applianceId, percentage) {
    log('DEBUG', `setPercentage (applianceId: ${applianceId}), percentage: ${percentage}`);

    // Call device cloud's API to set percentage

    return generateResponse('SetPercentageConfirmation', {});
}

function incrementPercentage(applianceId, delta) {
    log('DEBUG', `incrementPercentage (applianceId: ${applianceId}), delta: ${delta}`);

    // Call device cloud's API to set percentage delta

    return generateResponse('IncrementPercentageConfirmation', {});
}

function decrementPercentage(applianceId, delta) {
    log('DEBUG', `decrementPercentage (applianceId: ${applianceId}), delta: ${delta}`);

    // Call device cloud's API to set percentage delta

    return generateResponse('DecrementPercentageConfirmation', {});
}

/**
 * Main logic
 */

/**
 * This function is invoked when we receive a "Discovery" message from Alexa Smart Home Skill.
 * We are expected to respond back with a list of appliances that we have discovered for a given customer.
 *
 * @param {Object} request - The full request object from the Alexa smart home service. This represents a DiscoverAppliancesRequest.
 *     https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#discoverappliancesrequest
 *
 * @param {function} callback - The callback object on which to succeed or fail the response.
 *     https://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-handler.html#nodejs-prog-model-handler-callback
 *     If successful, return <DiscoverAppliancesResponse>.
 *     https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#discoverappliancesresponse
 */
function handleDiscovery(request, callback) {
    log('DEBUG', `Discovery Request: ${JSON.stringify(request)}`);

    /**
     * Get the OAuth token from the request.
     */
    const userAccessToken = request.payload.accessToken.trim();

    /**
     * Generic stub for validating the token against your cloud service.
     * Replace isValidToken() function with your own validation.
     */
    if (!userAccessToken || !isValidToken(userAccessToken)) {
        const errorMessage = `Discovery Request [${request.header.messageId}] failed. Invalid access token: ${userAccessToken}`;
        log('ERROR', errorMessage);
        callback(new Error(errorMessage));
    }

    /**
     * Assume access token is valid at this point.
     * Retrieve list of devices from cloud based on token.
     *
     * For more information on a discovery response see
     *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#discoverappliancesresponse
     */
    const response = {
    };

    /**
     * Log the response. These messages will be stored in CloudWatch.
     */
    log('DEBUG', `Discovery Response: ${JSON.stringify(response)}`);

    /**
     * Return result with successful message.
     */
    callback(null, response);
}

/**
 * A function to handle control events.
 * This is called when Alexa requests an action such as turning off an appliance.
 *
 * @param {Object} request - The full request object from the Alexa smart home service.
 * @param {function} callback - The callback object on which to succeed or fail the response.
 */
function handleControl(request, callback) {
    log('DEBUG', `Control Request: ${JSON.stringify(request)}`);

    /**
     * Get the access token.
     */
    const userAccessToken = request.payload.accessToken.trim();

    /**
     * Generic stub for validating the token against your cloud service.
     * Replace isValidToken() function with your own validation.
     *
     * If the token is invliad, return InvalidAccessTokenError
     *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#invalidaccesstokenerror
     */
    if (!userAccessToken || !isValidToken(userAccessToken)) {
        log('ERROR', `Discovery Request [${request.header.messageId}] failed. Invalid access token: ${userAccessToken}`);
        callback(null, generateResponse('InvalidAccessTokenError', {}));
        return;
    }

    /**
     * Grab the applianceId from the request.
     */
    const applianceId = request.payload.appliance.applianceId;

    /**
     * If the applianceId is missing, return UnexpectedInformationReceivedError
     *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#unexpectedinformationreceivederror
     */
    if (!applianceId) {
        log('ERROR', 'No applianceId provided in request');
        const payload = { faultingParameter: `applianceId: ${applianceId}` };
        callback(null, generateResponse('UnexpectedInformationReceivedError', payload));
        return;
    }

    /**
     * At this point the applianceId and accessToken are present in the request.
     *
     * Please review the full list of errors in the link below for different states that can be reported.
     * If these apply to your device/cloud infrastructure, please add the checks and respond with
     * accurate error messages. This will give the user the best experience and help diagnose issues with
     * their devices, accounts, and environment
     *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#error-messages
     */
    if (!isDeviceOnline(applianceId, userAccessToken)) {
        log('ERROR', `Device offline: ${applianceId}`);
        callback(null, generateResponse('TargetOfflineError', {}));
        return;
    }

    let response;

    switch (request.header.name) {
        case 'TurnOnRequest':
            response = turnOn(applianceId, userAccessToken);
            break;

        case 'TurnOffRequest':
            response = turnOff(applianceId, userAccessToken);
            break;

        case 'SetPercentageRequest': {
            const percentage = request.payload.percentageState.value;
            if (!percentage) {
                const payload = { faultingParameter: `percentageState: ${percentage}` };
                callback(null, generateResponse('UnexpectedInformationReceivedError', payload));
                return;
            }
            response = setPercentage(applianceId, userAccessToken, percentage);
            break;
        }

        case 'IncrementPercentageRequest': {
            const delta = request.payload.deltaPercentage.value;
            if (!delta) {
                const payload = { faultingParameter: `deltaPercentage: ${delta}` };
                callback(null, generateResponse('UnexpectedInformationReceivedError', payload));
                return;
            }
            response = incrementPercentage(applianceId, userAccessToken, delta);
            break;
        }

        case 'DecrementPercentageRequest': {
            const delta = request.payload.deltaPercentage.value;
            if (!delta) {
                const payload = { faultingParameter: `deltaPercentage: ${delta}` };
                callback(null, generateResponse('UnexpectedInformationReceivedError', payload));
                return;
            }
            response = decrementPercentage(applianceId, userAccessToken, delta);
            break;
        }

        default: {
            log('ERROR', `No supported directive name: ${request.header.name}`);
            callback(null, generateResponse('UnsupportedOperationError', {}));
            return;
        }
    }

    log('DEBUG', `Control Confirmation: ${JSON.stringify(response)}`);

    callback(null, response);
}

/**
 * Main entry point.
 * Incoming events from Alexa service through Smart Home API are all handled by this function.
 *
 * It is recommended to validate the request and response with Alexa Smart Home Skill API Validation package.
 *  https://github.com/alexa/alexa-smarthome-validation
 */
exports.handler = (request, context, callback) => {
    switch (request.header.namespace) {
        /**
         * The namespace of 'Alexa.ConnectedHome.Discovery' indicates a request is being made to the Lambda for
         * discovering all appliances associated with the customer's appliance cloud account.
         *
         * For more information on device discovery, please see
         *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#discovery-messages
         */
        case 'Alexa.ConnectedHome.Discovery':
            handleDiscovery(request, callback);
            break;

        /**
         * The namespace of "Alexa.ConnectedHome.Control" indicates a request is being made to control devices such as
         * a dimmable or non-dimmable bulb. The full list of Control events sent to your lambda are described below.
         *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#payload
         */
        case 'Alexa.ConnectedHome.Control':
            handleControl(request, callback);
            break;

        /**
         * The namespace of "Alexa.ConnectedHome.Query" indicates a request is being made to query devices about
         * information like temperature or lock state. The full list of Query events sent to your lambda are described below.
         *  https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/smart-home-skill-api-reference#payload
         *
         * TODO: In this sample, query handling is not implemented. Implement it to retrieve temperature or lock state.
         */
        // case 'Alexa.ConnectedHome.Query':
        //     handleQuery(request, callback);
        //     break;

        /**
         * Received an unexpected message
         */
        default: {
            const errorMessage = `No supported namespace: ${request.header.namespace}`;
            log('ERROR', errorMessage);
            callback(new Error(errorMessage));
        }
    }
};
