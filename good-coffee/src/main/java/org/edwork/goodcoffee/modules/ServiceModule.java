package org.edwork.goodcoffee.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.edwork.goodcoffee.services.*;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;

import javax.inject.Singleton;

@Module
public class ServiceModule {

    private ServiceModule() {}

    @Provides
    @Singleton
    public static LoggingService provideLoggingService() {
        return new TempLoggingService();
    }

    @Provides
    @Singleton
    public static IdGenerator provideIdGenerator() {
        return new ShopIdGenerator();
    }

    @Provides
    @Singleton
    public static ModelMapper providesModelMapper() {
        return new ModelMapper();
    }

    @Provides
    @Singleton
    public static MapperService providesMapperService() {
        return new JsonMapperService(new ObjectMapper());
    }

    @Provides
    @Singleton
    public static CoffeeValidator providesValidator() {
        Validator v = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
        return new CoffeeValidator(v);
    }

}
