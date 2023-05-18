## Lambda Functions Required Values

#### CreateDragon, DeleteDragon, ReadDragon, UpdateDragon, ValidateDragon use two values from SSM Parameter Store:

```
dragon_data_bucket_name (String): name of host S3 bucket
dragon_data_file_name (String): name of the file in host S3 bucket (JSON array of Dragon objects)
```
#### VerifySession uses one environment variable:
```
JWT_SECRET (String): JWT secret key used to encrypt session tokens
```
#### LoginHandler uses the above environment variables as well as:
```
USERS_TABLE_NAME (String): name of DynamoDB registered users table
USERS_TABLE_REGION (String): region string of DynamoDB registered users table
```

#### DynamoDB registered users table has the following attributes:
```
username (String, Partition key): username of user
password (String): bcrypt encrypted password of user
```
