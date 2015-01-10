package com.github.nyrkovalex.gitdep.git;

import com.github.nyrkovalex.gitdep.UnitTest;
import com.github.nyrkovalex.seed.core.Seed;
import org.eclipse.jgit.transport.CredentialItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CredentialHandlersTest extends UnitTest {

    @Mock private CredentialItem.StringType stringRequest;
    @Mock private CredentialItem.CharArrayType charArrRequest;
    @Mock private CredentialItem.YesNoType yesNoRequest;
    @Mock private Seed.Console.InputProvider inputProvider;

    @InjectMocks private CredentialHandlers.CharArrayHandler charArrayHandler;
    @InjectMocks private CredentialHandlers.StringHandler stringHandler;
    @InjectMocks private CredentialHandlers.YesNoHandler yesNoHandler;

    @Before
    public void setUp() throws Exception {
        when(inputProvider.read(anyString())).thenReturn("plain");
        when(inputProvider.readSecure(anyString())).thenReturn("secure");
    }

    @Test
    public void testShouldProvidePlainInputForStringCredRequest() throws Exception {
        stringHandler.apply(stringRequest);
        verify(stringRequest).setValue("plain");
    }

    @Test
    public void testShouldProvidePlainInputForCharArrCredRequest() throws Exception {
        charArrayHandler.apply(charArrRequest);
        verify(charArrRequest).setValue("plain".toCharArray());
    }

    @Test
    public void testShouldProvideTrueInputForYesNoCredRequest() throws Exception {
        yesNoHandler.apply(yesNoRequest);
        verify(yesNoRequest).setValue(false);
    }

    @Test
    public void testShouldProvideTrueInputForUppercaseYesNoCredRequest() throws Exception {
        when(inputProvider.read(anyString())).thenReturn("Y");
        yesNoHandler.apply(yesNoRequest);
        verify(yesNoRequest).setValue(true);
    }

    @Test
    public void testShouldProvideTrueInputForLowercaseYesNoCredRequest() throws Exception {
        when(inputProvider.read(anyString())).thenReturn("y");
        yesNoHandler.apply(yesNoRequest);
        verify(yesNoRequest).setValue(true);
    }

    @Test
    public void testShouldProvidePlainInputForSecureStringCredRequest() throws Exception {
        when(stringRequest.isValueSecure()).thenReturn(true);
        stringHandler.apply(stringRequest);
        verify(stringRequest).setValue("secure");
    }

    @Test
    public void testShouldProvidePlainInputForSecureCharArrCredRequest() throws Exception {
        when(charArrRequest.isValueSecure()).thenReturn(true);
        charArrayHandler.apply(charArrRequest);
        verify(charArrRequest).setValue("secure".toCharArray());
    }
}
