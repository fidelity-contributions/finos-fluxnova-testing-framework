package org.finos.fluxnova.bpm.test.helpers;

import org.finos.fluxnova.bpm.test.example.delegates.ExampleOneDelegate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DelegateHelpersTest {

    @Test
    void testMockedDelegate_returnsMockedDelegateInstance() {
        ExampleOneDelegate exampleOneDelegate = mock(ExampleOneDelegate.class);
        assertNotNull(DelegateHelpers.mockDelegate(exampleOneDelegate).mock());
    }
}
