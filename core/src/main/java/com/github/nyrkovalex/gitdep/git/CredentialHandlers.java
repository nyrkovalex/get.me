package com.github.nyrkovalex.gitdep.git;

import com.github.nyrkovalex.seed.core.Seed;
import org.eclipse.jgit.transport.CredentialItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

final class CredentialHandlers {

    private static final Seed.Console.InputProvider READER = new Seed.Console.InputProvider();
    private static final StringHandler STRING_HANDLER = new StringHandler(READER);
    private static final CharArrayHandler CHAR_ARR_HANDLER = new CharArrayHandler(READER);
    private static final YesNoHandler YES_NO_HANDLER = new YesNoHandler(READER);
    private static final CredentialHandlers.InfoHandler INFO_HANDLER = new CredentialHandlers.InfoHandler();

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
        protected StringHandler(Seed.Console.InputProvider inputProvider) {
            super(inputProvider);
        }

        @Override
        public Boolean apply(CredentialItem credentialItem) {
            CredentialItem.StringType casted = (CredentialItem.StringType) credentialItem;
            casted.setValue(readInput(credentialItem));
            return true;
        }
    }

    static class CharArrayHandler extends InputHandler {
        protected CharArrayHandler(Seed.Console.InputProvider inputProvider) {
            super(inputProvider);
        }

        @Override
        public Boolean apply(CredentialItem credentialItem) {
            CredentialItem.CharArrayType casted = (CredentialItem.CharArrayType) credentialItem;
            casted.setValue(readInput(credentialItem).toCharArray());
            return true;
        }
    }

    static class YesNoHandler extends InputHandler {
        protected YesNoHandler(Seed.Console.InputProvider inputProvider) {
            super(inputProvider);
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
        private final Seed.Console.InputProvider inputProvider;

        protected InputHandler(Seed.Console.InputProvider inputProvider) {
            this.inputProvider = inputProvider;
        }

        protected String readInput(CredentialItem credentialItem) {
            String prompt = String.format("Please provide %s: ", credentialItem.getPromptText());
            return credentialItem.isValueSecure()
                   ? inputProvider.readSecure(prompt)
                   : inputProvider.read(prompt);
        }
    }
}
