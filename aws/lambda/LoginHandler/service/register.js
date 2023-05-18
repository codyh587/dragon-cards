const AWS = require('aws-sdk');
AWS.config.update({
    region: process.env.USERS_TABLE_REGION
})
const util = require('../utils/util');
const bcrypt = require('bcryptjs');

const dynamodb = new AWS.DynamoDB.DocumentClient();
const userTable = process.env.USERS_TABLE_NAME;

async function register(userInfo) {
    const username = userInfo.username;
    const password = userInfo.password;

    if (!username || !password) {
        return util.buildResponse(401, {
            message: 'All fields are required'
        })
    }

    const dynamoUser = await getUser(username);
    if (dynamoUser && dynamoUser.username) {
        return util.buildResponse(401, {
            message: 'Username already exists, please choose another'
        })
    }

    const encryptedPassword = bcrypt.hashSync(password.trim(), 10);
    const user = {
        username: username,
        password: encryptedPassword
    }

    const saveUserResponse = await saveUser(user);
    if (!saveUserResponse) {
        return util.buildResponse(503, {
            message: 'Server error, please try again later'
        })
    }

    return util.buildResponse(200, {
        username: username
    })
}

async function getUser(username) {
    const params = {
        TableName: userTable,
        Key: {
            username: username
        }
    }

    return await dynamodb.get(params).promise().then(response => {
        return response.Item;
    }, error => {
        console.log('Error getting user:', error);
    });
}

async function saveUser(user) {
    const params = {
        TableName: userTable,
        Item: user
    }

    return await dynamodb.put(params).promise().then(() => {
        return true;
    }, error=> {
        console.log('Error writing user: ', error)
    });
}

module.exports.register = register;
