const verifyService = require('./verify');

exports.handler = function(event, context, callback) {
    
    // const verifyBody = JSON.parse(event.body);
    const verifyBody = { 
        token: event['authorizationToken']
    };
    let response = verifyService.verify(verifyBody);
    let responseBody = JSON.parse(response.body);
    console.log(responseBody.verified)
    
    if (responseBody.verified) {
        callback(null, generatePolicy('Allow', event.methodArn));
    } else {
        callback(null, generatePolicy('Deny', event.methodArn));
    }
}

function generatePolicy(effect, resource) {
    var authResponse = {};
    
    if (effect && resource) {
        var policyDocument = {};
        policyDocument.Version = '2012-10-17'; 
        policyDocument.Statement = [];
        var statementOne = {};
        statementOne.Action = 'execute-api:Invoke'; 
        statementOne.Effect = effect;
        statementOne.Resource = resource;
        policyDocument.Statement[0] = statementOne;
        authResponse.policyDocument = policyDocument;
    }
    
    return authResponse;
}
