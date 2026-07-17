package org.finos.fluxnova.bpm.test;

import org.finos.fluxnova.bpm.test.example.delegates.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockReset;

@Configuration
public class DelegateMocks {

    @Bean
    ExampleOneDelegate exampleOneDelegate() {
        return Mockito.mock(ExampleOneDelegate.class, MockReset.after());
    }

    @Bean
    CarPartsDelegate carPartsDelegate() {
        return Mockito.mock(CarPartsDelegate.class, MockReset.after());
    }

    @Bean
    SendEmailDelegate sendEmailDelegate() {
        return Mockito.mock(SendEmailDelegate.class, MockReset.after());
    }

    @Bean
    SendSMSDelegate sendSMSDelegate() {
        return Mockito.mock(SendSMSDelegate.class, MockReset.after());
    }

    @Bean
    ClaimValidationDelegate claimValidationDelegate() {
        return Mockito.mock(ClaimValidationDelegate.class, MockReset.after());
    }
}