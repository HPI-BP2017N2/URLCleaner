# URLCleaner[![Build Status](https://travis-ci.org/HPI-BP2017N2/URLCleaner.svg?branch=master)](https://travis-ci.org/HPI-BP2017N2/URLCleaner)

URLCleaner is a microservice component, that is used to remove redirects and affiliate markers from a url.

## Getting started
### Prerequisites

To run the microservice it is required to set up the following:

1. MongoDB
...The MongoDB is used to keep track of blacklisted shops (in the collection named 'blacklist').

2. Idealo bridge
...The URLCleaner uses idealo bridge to resolve shopID to shop root url.

### Configuration

The name of the database which will be used is 'data'.

#### Environment variables

URLCLEANER_PORT	- the port used by URLCleaner
API_URL - URL address of idealo bridge
ACCESS_TOKEN_URI - URI for retrieving access token from idealo bridge
CLIENT_ID - Client ID for retrieving access token from idealo bridge
CLIENT_SECRET - Client ID for retrieving access token from idealo bridge
MONGO_IP - IP address of Mongo DB
MONGO_PORT - Port that Mongo DB is using
MONGO_URLCLEANER_USER - Username that used to access Mongo DB
MONGO_PASSWORD - Password that used to access Mongo DB

## How it works

The URLCleaner is using 2 different strategies to clean urls: 

1. Redirect clean
... The component removes parts of given Url before the corresponding root url.

2. Affiliate marker clean 
... The component removes affiliate marker (for example UTM) from the given url.

The both strategies are applied after each other. The result is the output. 

## Outlook

1. The list of affiliate markers can be extended.
2. The list of shop specific affiliate markers can be collected automatically. We can remove every parameter and 
fetching the page using the resulting url. If it is identical to the webpage fetched using dirtyUrl, then
 the removed parameter is affiliate marker for this shop.  If not, then not. 
3. The component should compare the webpages that got fetched using dirtyUrl and corresponding cleanUrl.
If they are identical, the dirtyUrl got cleaned successfully. If not, then not.
It is enough to check this way one url for every shop.