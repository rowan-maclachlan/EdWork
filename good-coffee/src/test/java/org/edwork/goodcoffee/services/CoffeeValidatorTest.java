package org.edwork.goodcoffee.services;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.edwork.goodcoffee.config.DaggerShopComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CoffeeValidatorTest {

    CoffeeValidator validator;

    @BeforeEach
    public void setup() {
        this.validator = DaggerShopComponent.create().validator();
    }

    @Test
    public void testNoViolations() {
        TestPojo pojo = new TestPojo();
        pojo.testInt = 5;
        pojo.testList = Arrays.asList(1, 2, 3);
        pojo.testString = "test";

        validator.validate(pojo);
    }


    @Test
    public void testOneViolation() {
        TestPojo pojo = new TestPojo();
        pojo.testInt = 11; // out of range
        pojo.testList = Arrays.asList(1, 2, 3);
        pojo.testString = "test";

        Exception e = Assertions.assertThrows(ValidationException.class, () -> validator.validate(pojo));
    }

    @Test
    public void testSeveralViolation() {
        TestPojo pojo = new TestPojo();
        pojo.testInt = 11; // out of range
        pojo.testList = new ArrayList<>();
        pojo.testString = "";

        Exception e = Assertions.assertThrows(ValidationException.class, () -> validator.validate(pojo));
    }

    public class TestPojo {

        @Min(0) @Max(10)
        private Integer testInt;
        @NotBlank
        private String testString;
        @NotEmpty
        private List<Integer> testList;

    }

}