package com.github.nyrkovalex.gitdep.conf;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "executors")
class ExecutorsDocument {
    static final String MVN_EXECUTOR_CLASS = "com.github.nyrkovalex.gitdep.mvn.MvnExecutor";

    @XmlElement(name = "executor")
    private Set<String> executors;

    @SuppressWarnings("UnusedDeclaration") // Used by JAXB
    private ExecutorsDocument() {}

    private ExecutorsDocument(String... executors) {
        this.executors = new HashSet<>(Arrays.asList(executors));
    }

    public Set<String> executors() {
        return executors;
    }

    public static ExecutorsDocument from(InputStream in) throws MalformedConfigurationException {
        try {
            JAXBContext context = jaxbContext();
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ExecutorsDocument) unmarshaller.unmarshal(in);
        } catch (JAXBException e) {
            throw new MalformedConfigurationException(e);
        }
    }

    private static JAXBContext jaxbContext() throws JAXBException {
        return JAXBContext.newInstance(ExecutorsDocument.class);
    }

    public void writeTo(OutputStream out) {
        try {
            JAXBContext context = jaxbContext();
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, out);
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot save executor configuration", e);
        }
    }

    public static ExecutorsDocument defaultOne() {
        return new ExecutorsDocument(MVN_EXECUTOR_CLASS);
    }
}
