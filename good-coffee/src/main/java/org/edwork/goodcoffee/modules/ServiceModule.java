package org.edwork.goodcoffee.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import org.edwork.goodcoffee.services.IdGenerator;
import org.edwork.goodcoffee.services.LoggingService;
import org.edwork.goodcoffee.services.ShopIdGenerator;
import org.edwork.goodcoffee.services.TempLoggingService;
import org.modelmapper.ModelMapper;

import javax.inject.Singleton;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    LoggingService provideLoggingService() {
        return new TempLoggingService();
    }

    @Provides
    @Singleton
    IdGenerator provideIdGenerator() {
        return new ShopIdGenerator();
    }

    @Provides
    @Singleton
    public ModelMapper providesModelMapper() {
        return new ModelMapper();
    }

    @Provides
    @Singleton
    public ObjectMapper providesObjectMapper() {
        return new ObjectMapper();
    }
}
