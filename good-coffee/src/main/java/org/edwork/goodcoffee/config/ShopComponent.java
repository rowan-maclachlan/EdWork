package org.edwork.goodcoffee.config;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Component;
import org.edwork.goodcoffee.database.ShopStore;
import org.edwork.goodcoffee.modules.ServiceModule;
import org.edwork.goodcoffee.modules.StoreModule;
import org.edwork.goodcoffee.services.*;
import org.modelmapper.ModelMapper;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ServiceModule.class, StoreModule.class})
public interface ShopComponent {
    LoggingService loggingService();
    IdGenerator idGenerator();
    ModelMapper modelMapper();
    MapperService mapperService();
    DynamoDBMapper dynamoDbMapper();
    ShopStore shopStore();
    CoffeeValidator validator();
}
