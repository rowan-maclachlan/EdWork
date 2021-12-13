# Welcome to your CDK Java project!

This is a blank project for Java development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

# Good Coffee

The Good Coffee stack defines a simple REST app for tracking coffee joints.

Use the app to find the best coffee near you.

### Create Shops

```
{ 
  "name": "CuppaJoe",
  "location": { "type": "Point", "coordinates": [ -123.123, 48.9 ] },
  "rating": 5
```

The `location` type in the request must be a geojson point.  The `coordinates` are the shop's longitude and latitude in reference to EPSG:4326.

### Find Shops

To find the nearest coffee shop to your location, use the /coffeeshops endpoint and provide `lat` and `lon` query parameters in the URL.  To find the best coffee, 
provide the `rating` query parameter to limit the shops returned to those with ratings greater than or equal to the value of the query parameter.

If you don't provide longitude and latitude, the results will be ordered according to their rating.  The highest rated shops will be listed first, and then in descending order.

If you do provide the coordinates, the results will be ordered in increasing distance from the coordinates you provide.

Use both parameters to find the closest shop with the best coffee, list this:

```
GET /prod/coffeeshops?rating=8&lon=-123.0706021633374&lat=49.28117899383801
```

To find the location and rating of a particular shop, use the shop name as a path parameter, like this:
```
GET /prod/coffeeshops/CuppaJoe
```
