package com.github.nyrkovalex.get.me.git;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.jgit.transport.CredentialItem;

import com.github.nyrkovalex.seed.Sys;

class CredentialHandlers {

	private static final Sys.Console CONSOLE = Sys.console();
	private static final StringHandler STRING_HANDLER = new StringHandler(CONSOLE);
	private static final CharArrayHandler CHAR_ARR_HANDLER = new CharArrayHandler(CONSOLE);
	private static final YesNoHandler YES_NO_HANDLER = new YesNoHandler(CONSOLE);
	private static final InfoHandler INFO_HANDLER = new InfoHandler();

	static Map<Class<?>, Function<CredentialItem, Boolean>> handlerMap() {
		Map<Class<?>, Function<CredentialItem, Boolean>> map = new HashMap<>();
		map.put(CredentialItem.StringType.class, STRING_HANDLER);
		map.put(CredentialItem.Username.class, STRING_HANDLER);
		map.put(CredentialItem.CharArrayType.class, CHAR_ARR_HANDLER);
		map.put(CredentialItem.Password.class, CHAR_ARR_HANDLER);
		map.put(CredentialItem.YesNoType.class, YES_NO_HANDLER);
		map.put(CredentialItem.InformationalMessage.class, INFO_HANDLER);
		return Collections.unmodifiableMap(map);
	}

	static class InfoHandler implements Function<CredentialItem, Boolean> {

		@Override
		public Boolean apply(CredentialItem credentialItem) {
			System.out.println(credentialItem.getPromptText());
			return true;
		}
	}

	static class StringHandler extends InputHandler {

		protected StringHandler(Sys.Console console) {
			super(console);
		}

		@Override
		public Boolean apply(CredentialItem credentialItem) {
			CredentialItem.StringType casted = (CredentialItem.StringType) credentialItem;
			casted.setValue(readInput(credentialItem));
			return true;
		}
	}

	static class CharArrayHandler extends InputHandler {

		protected CharArrayHandler(Sys.Console console) {
			super(console);
		}

		@Override
		public Boolean apply(CredentialItem credentialItem) {
			CredentialItem.CharArrayType casted = (CredentialItem.CharArrayType) credentialItem;
			casted.setValue(readInput(credentialItem).toCharArray());
			return true;
		}
	}

	static class YesNoHandler extends InputHandler {

		protected YesNoHandler(Sys.Console console) {
			super(console);
		}

		@Override
		public Boolean apply(CredentialItem credentialItem) {
			String input = readInput(credentialItem);
			CredentialItem.YesNoType casted = (CredentialItem.YesNoType) credentialItem;
			casted.setValue(input.toLowerCase().equals("y"));
			return true;
		}
	}

	static abstract class InputHandler implements Function<CredentialItem, Boolean> {

		private final Sys.Console console;

		protected InputHandler(Sys.Console console) {
			this.console = console;
		}

		protected String readInput(CredentialItem credentialItem) {
			String prompt = String.format("Please provide %s: ", credentialItem.getPromptText());
			return credentialItem.isValueSecure()
			       ? console.readSecure(prompt)
			       : console.read(prompt);
		}
	}
}
