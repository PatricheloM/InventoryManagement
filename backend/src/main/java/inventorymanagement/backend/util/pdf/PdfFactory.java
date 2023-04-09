package inventorymanagement.backend.util.pdf;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import inventorymanagement.backend.dto.ItemDTO;
import inventorymanagement.backend.util.enums.ImportExport;
import inventorymanagement.backend.util.exception.PdfTemplateNotFoundException;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class PdfFactory {

    private PdfFactory() {}

    public static byte[] produce(ItemDTO item, ImportExport type) {

        Mustache mustache;
        MustacheFactory mf = new DefaultMustacheFactory();

        Writer writer = new StringWriter();
        HashMap<String, Object> scopes = new HashMap<>();
        scopes.put("ITEMNAME", item.getName());
        scopes.put("ITEMWEIGHT", item.getWeight() + " kg");
        scopes.put("COMPANY", item.getCompany());
        scopes.put("ARRIVAL", item.getArrival().toString());
        scopes.put("LOCATION", item.getLocation());

        try {
            if (type == ImportExport.IMPORT) {
                String name = "import.mustache";
                mustache = mf.compile(new InputStreamReader(PdfFactory.class.getResourceAsStream(name), UTF_8), name);
            } else {
                scopes.put("DEPARTURE", new Date().toString());
                String name = "export.mustache";
                mustache = mf.compile(new InputStreamReader(PdfFactory.class.getResourceAsStream(name), UTF_8), name);
            }
        } catch (NullPointerException e) {
            throw new PdfTemplateNotFoundException(e);
        }

        mustache.execute(writer, scopes);

        ITextRenderer renderer = new ITextRenderer();
        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext.setPrint(true);
        sharedContext.setInteractive(false);
        renderer.setDocumentFromString(writer.toString());
        renderer.layout();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.createPDF(baos);

        return baos.toByteArray();
    }
}
