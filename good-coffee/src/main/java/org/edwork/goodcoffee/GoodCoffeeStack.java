package org.edwork.goodcoffee;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class GoodCoffeeStack extends Stack {
    public GoodCoffeeStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public GoodCoffeeStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final TableProps tableProps = TableProps.builder()
                .partitionKey(Attribute.builder()
                        .name("name")
                        .type(AttributeType.STRING)
                        .build())
                .tableName("CoffeeShops")
                .build();
        final GlobalSecondaryIndexProps gsiProps = GlobalSecondaryIndexProps.builder()
                .indexName("rating")
                .partitionKey(Attribute.builder()
                        .name("gsiPartition")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("rating")
                        .type(AttributeType.NUMBER)
                        .build()).build();

        final Table dynamoDbTable = new Table(this, "coffee-shops", tableProps);
        dynamoDbTable.addGlobalSecondaryIndex(gsiProps);

        // Defines a new lambda resource
        final Function createShopRequestHandler = Function.Builder.create(this, "create-shop-request-handler")
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("target/good-coffee-0.1.jar"))
                .timeout(Duration.seconds(15))
                .memorySize(256)
                .handler("org.edwork.goodcoffee.apigateway.CreateShopRequestHandler::handleRequest")
                .build();

        final Function getShopsRequestHandler = Function.Builder.create(this, "get-shops-request-handler")
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("target/good-coffee-0.1.jar"))
                .timeout(Duration.seconds(15))
                .memorySize(256)
                .handler("org.edwork.goodcoffee.apigateway.GetShopsRequestHandler::handleRequest")
                .build();

        final Function getShopRequestHandler = Function.Builder.create(this, "get-shop-request-handler")
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("target/good-coffee-0.1.jar"))
                .timeout(Duration.seconds(15))
                .memorySize(256)
                .handler("org.edwork.goodcoffee.apigateway.GetShopRequestHandler::handleRequest")
                .build();

        final Function deleteShopRequestHandler = Function.Builder.create(this, "delete-shop-request-handler")
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("target/good-coffee-0.1.jar"))
                .timeout(Duration.seconds(15))
                .memorySize(256)
                .handler("org.edwork.goodcoffee.apigateway.DeleteShopRequestHandler::handleRequest")
                .build();

        dynamoDbTable.grantFullAccess(getShopRequestHandler);
        dynamoDbTable.grantFullAccess(getShopsRequestHandler);
        dynamoDbTable.grantFullAccess(createShopRequestHandler);
        dynamoDbTable.grantFullAccess(deleteShopRequestHandler);

        RestApi api = RestApi.Builder.create(this, "good-coffee-API")
                .restApiName("GoodCoffeeRestApi")
                .description("Check out good coffee")
                .build();

        Resource coffeeShops = api.getRoot().addResource("coffeeshops");

        LambdaIntegration createCoffeeShopIntegration =
                LambdaIntegration.Builder.create(createShopRequestHandler).build();
        coffeeShops.addMethod("POST", createCoffeeShopIntegration);

        LambdaIntegration getCoffeeShopsIntegration =
                LambdaIntegration.Builder.create(getShopsRequestHandler).build();
        coffeeShops.addMethod("GET", getCoffeeShopsIntegration);

        Resource coffeeShop = coffeeShops.addResource("{name}");
        LambdaIntegration getCoffeeIntegration =
                LambdaIntegration.Builder.create(getShopRequestHandler).build();
        LambdaIntegration deleteCoffeeIntegration =
                LambdaIntegration.Builder.create(deleteShopRequestHandler).build();
        coffeeShop.addMethod("GET", getCoffeeIntegration);
        coffeeShop.addMethod("DELETE", deleteCoffeeIntegration);
    }
}
