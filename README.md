# AppDirect integration prototype

## Deployment

The appliction is deployed at https://protodirect.herokuapp.com


## Build instructions

```
    $ mvn clean package
```

## Integration settings

#### Login URL

https://protodirect.herokuapp.com/login/openid?openid_identifier={openid}

#### OpenID Realm 

https://protodirect.herokuapp.com/*


### Subscriptions

#### Subscription Create Notification URL

https://protodirect.herokuapp.com/notify/create?url={eventUrl}
 

#### Subscription Change Notification URL

https://protodirect.herokuapp.com/notify/change?url={eventUrl}


#### Subscription Cancel Notification URL

https://protodirect.herokuapp.com/notify/cancel?url={eventUrl}


#### Subscription Status Notification URL

https://protodirect.herokuapp.com/notify/status?url={eventUrl}


#### User Assignment Notification URL

https://protodirect.herokuapp.com/notify/assign?url={eventUrl}


#### User Unassignment Notification URL

https://protodirect.herokuapp.com/notify/unassign?url={eventUrl}
