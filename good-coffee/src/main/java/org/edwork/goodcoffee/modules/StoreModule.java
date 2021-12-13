package org.edwork.goodcoffee.modules;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;
import org.edwork.goodcoffee.database.DynamoShopStore;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.services.LoggingService;

import javax.inject.Singleton;

@Module
public class StoreModule {

    private StoreModule() {}

    @Provides
    @Singleton
    public static DynamoDBMapper providesDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Provides
    @Singleton
    public static AmazonDynamoDB providesAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .build();
    }

    @Provides
    @Singleton
    public static ShopStore providesShopStore(DynamoDBMapper dynamoDBMapper, LoggingService loggingService) {
        return new DynamoShopStore(dynamoDBMapper, loggingService);
    }

}
