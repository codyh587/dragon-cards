const jwt = require('jsonwebtoken');
const util = require('./utils/util')

function verify(requestBody) {
    if(!requestBody.token) {
        return util.buildResponse(401, {
            verified: false,
            message: 'incorrect response body'
        })
    }

    const token = requestBody.token;
    const verification = verifyToken(token);
    if (!verification.verified) {
        return util.buildResponse(401, verification);
    }

    return util.buildResponse(200, {
        verified: true,
        message: 'success',
        token: token
    })
}


function verifyToken(token) {
    return jwt.verify(token, process.env.JWT_SECRET, (error, response) => {
        if (error) {
            return {
                verified: false,
                message: 'invalid token'
            }
        }

        return {
            verified: true,
            message: 'verified'
        }
    })
}

module.exports.verify = verify;
