package org.finos.fluxnova.bpm.test.helpers;

import org.finos.fluxnova.bpm.test.mocks.delegates.MockedDelegateInstance;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

public class DelegateHelpers {

    private DelegateHelpers() {}

    public static <T extends JavaDelegate> MockedDelegateInstance<JavaDelegate> mockDelegate(T delegate) {
        return new MockedDelegateInstance<>(delegate);
    }

}