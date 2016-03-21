import com.google.common.base.Charsets;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;

public class Trax {
    public static Transformer newTransformer(File xsl) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xsl));
        } catch (TransformerConfigurationException e) {
            throw new IllegalStateException("Error while creating transformer", e);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, Charsets.ISO_8859_1.name());
        return transformer;
    }

    public static void transform(File xsl, File source,  File result) throws TransformerException {
        final Transformer transformer = newTransformer(xsl);
        transform(source, result, transformer);
    }
    public static void transform(File source,  File result, Transformer transformer) throws TransformerException {
        transformer.transform(new StreamSource(source), new StreamResult(result));
    }
    public static Reader transform(File xsl, File source) throws IOException {
        final Transformer transformer = newTransformer(xsl);
        return transform(source, transformer);
    }
    public static Reader transform(final File source, final Transformer transformer) throws IOException {
        return transform(new StreamSource(source), transformer);
    }
    public static Reader transform(final Source source, final Transformer transformer) throws IOException {
        final PipedWriter pw = new PipedWriter();
        final PipedReader pr = new PipedReader(pw);

        new Thread(new Runnable() {
            public void run() {
                try {
                    transformer.transform(source, new StreamResult(pw));
                    pw.close();
                } catch (Exception e) {
                    throw new IllegalStateException("Error while transforming: " + source.getSystemId(), e);
                }
            }
        }).start();
        return pr;
    }
    
    public static void main(String[] args) throws Exception {
        File source = new File("1083982M.xml");
        File result1 = new File(source.getParentFile(), "tr1_" + source.getName());
        File result2 = new File(source.getParentFile(), "tr2_" + source.getName());
        File xslt1 = new File("/path/to_first.xsl");
        File xslt2 = new File("/path/to_second.xsl");
        transform(source, result1, newTransformer(xslt1));
        transform(result1, result2, newTransformer(xslt2));
    }
}
