package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Seed;
import com.github.nyrkovalex.seed.Sys;
import com.github.nyrkovalex.seed.Tests;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.URIish;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.mockito.Matchers.anyString;

@RunWith(Enclosed.class)
public class GitTest {

	public static class CredentialsProviderTest extends Tests.Expect {

		@Mock Function<CredentialItem, Boolean> function;
		@Mock URIish urIish;
		private CredentialItem item;
		private Map<Class<?>, Function<CredentialItem, Boolean>> handlers = new HashMap<>();
		private GitCredentialsProvider provider;

		@Before
		public void initLogging() throws Exception {
			Seed.Logging.init(true, GitTest.class);
		}

		@Before
		public void setUp() throws Exception {
			item = new CredentialItem.YesNoType("test");
			handlers.put(CredentialItem.YesNoType.class, function);
			provider = new GitCredentialsProvider(handlers);
		}

		@Test
		public void testShouldPickHandlerByClass() throws Exception {
			given(function.apply(item)).returns(true);
			provider.get(urIish, item);
			expect(function).toHaveCall().apply(item);
		}

		@Test
		public void testShouldSupportClassesFromHandlerMap() throws Exception {
			expect(provider.supports(item)).toBe(true);
		}

		@Test
		public void testShouldNotSupportClassesNotFromHandlerMap() throws Exception {
			expect(provider.supports(new CredentialItem.Username())).toBe(false);
		}

		@Test
		public void testShouldBeInteractove() throws Exception {
			expect(provider.isInteractive()).toBe(true);
		}
	}

	public static class CredentialHandlersTest extends Tests.Expect {

		@Mock private CredentialItem.StringType stringRequest;
		@Mock private CredentialItem.CharArrayType charArrRequest;
		@Mock private CredentialItem.YesNoType yesNoRequest;
		@Mock private Sys.Console inputProvider;
		@InjectMocks private CredentialHandlers.CharArrayHandler charArrayHandler;
		@InjectMocks private CredentialHandlers.StringHandler stringHandler;
		@InjectMocks private CredentialHandlers.YesNoHandler yesNoHandler;

		@Before
		public void setUp() throws Exception {
			given(inputProvider.read(anyString())).returns("plain");
			given(inputProvider.readSecure(anyString())).returns("secure");
		}

		@Test
		public void testShouldProvidePlainInputForStringCredRequest() throws Exception {
			stringHandler.apply(stringRequest);
			expect(stringRequest).toHaveCall().setValue("plain");
		}

		@Test
		public void testShouldProvidePlainInputForCharArrCredRequest() throws Exception {
			charArrayHandler.apply(charArrRequest);
			expect(charArrRequest).toHaveCall().setValue("plain".toCharArray());
		}

		@Test
		public void testShouldProvideTrueInputForYesNoCredRequest() throws Exception {
			yesNoHandler.apply(yesNoRequest);
			expect(yesNoRequest).toHaveCall().setValue(false);
		}

		@Test
		public void testShouldProvideTrueInputForUppercaseYesNoCredRequest() throws Exception {
			given(inputProvider.read(anyString())).returns("Y");
			yesNoHandler.apply(yesNoRequest);
			expect(yesNoRequest).toHaveCall().setValue(true);
		}

		@Test
		public void testShouldProvideTrueInputForLowercaseYesNoCredRequest() throws Exception {
			given(inputProvider.read(anyString())).returns("y");
			yesNoHandler.apply(yesNoRequest);
			expect(yesNoRequest).toHaveCall().setValue(true);
		}

		@Test
		public void testShouldProvidePlainInputForSecureStringCredRequest() throws Exception {
			given(stringRequest.isValueSecure()).returns(true);
			stringHandler.apply(stringRequest);
			expect(stringRequest).toHaveCall().setValue("secure");
		}

		@Test
		public void testShouldProvidePlainInputForSecureCharArrCredRequest() throws Exception {
			given(charArrRequest.isValueSecure()).returns(true);
			charArrayHandler.apply(charArrRequest);
			expect(charArrRequest).toHaveCall().setValue("secure".toCharArray());
		}
	}

}
