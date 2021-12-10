package org.edwork.goodcoffee.modules;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import dagger.Module;
import dagger.Provides;
import org.edwork.goodcoffee.database.DynamoShopStore;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.services.MapperService;

import javax.inject.Singleton;

@Module
public class StoreModule {

    @Provides
    @Singleton
    DynamoDB providesDynamoDB(final AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDB(amazonDynamoDB);
    }

    @Provides
    @Singleton
    DynamoDBMapper providesDynamoDBMapper(final AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Provides
    @Singleton
    AmazonDynamoDB providesAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .build();
    }

    @Provides
    @Singleton
    ShopStore providesShopStore(DynamoDB dynamoDB, MapperService mapperService) {
        return new DynamoShopStore(dynamoDB, mapperService);
    }

}
