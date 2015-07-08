package com.github.nyrkovalex.get.me.git;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.URIish;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.nyrkovalex.seed.Sys;
import com.github.nyrkovalex.seed.logging.Logging;

@RunWith(Enclosed.class)
public class GitTest {

	public static class CredentialsProviderTest {

		@Mock Function<CredentialItem, Boolean> function;
		@Mock URIish urIish;

		private CredentialItem item;
		private Map<Class<?>, Function<CredentialItem, Boolean>> handlers = new HashMap<>();
		private GitCredentialsProvider provider;

		@Before
		public void initLogging() throws Exception {
			Logging.init(true, GitTest.class);
		}

		@Before
		public void setUp() throws Exception {
			MockitoAnnotations.initMocks(this);
			item = new CredentialItem.YesNoType("test");
			handlers.put(CredentialItem.YesNoType.class, function);
			provider = new GitCredentialsProvider(handlers);
		}

		@Test
		public void testShouldPickHandlerByClass() throws Exception {
			when(function.apply(item)).thenReturn(true);
			provider.get(urIish, item);
			verify(function).apply(item);
		}

		@Test
		public void testShouldSupportClassesFromHandlerMap() throws Exception {
			assertThat(provider.supports(item), is(true));
		}

		@Test
		public void testShouldNotSupportClassesNotFromHandlerMap() throws Exception {
			assertThat(provider.supports(new CredentialItem.Username()), is(false));
		}

		@Test
		public void testShouldBeInteractove() throws Exception {
			assertThat(provider.isInteractive(), is(true));
		}
	}

	public static class CredentialHandlersTest {

		@Mock private CredentialItem.StringType stringRequest;
		@Mock private CredentialItem.CharArrayType charArrRequest;
		@Mock private CredentialItem.YesNoType yesNoRequest;
		@Mock private Sys.Console inputProvider;
		@InjectMocks private CredentialHandlers.CharArrayHandler charArrayHandler;
		@InjectMocks private CredentialHandlers.StringHandler stringHandler;
		@InjectMocks private CredentialHandlers.YesNoHandler yesNoHandler;

		@Before
		public void setUp() throws Exception {
			MockitoAnnotations.initMocks(this);
			when(inputProvider.read(Matchers.anyString())).thenReturn("plain");
			when(inputProvider.readSecure(Matchers.anyString())).thenReturn("secure");
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
			when(inputProvider.read(Matchers.anyString())).thenReturn("Y");
			yesNoHandler.apply(yesNoRequest);
			verify(yesNoRequest).setValue(true);
		}

		@Test
		public void testShouldProvideTrueInputForLowercaseYesNoCredRequest() throws Exception {
			when(inputProvider.read(Matchers.anyString())).thenReturn("y");
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

}
