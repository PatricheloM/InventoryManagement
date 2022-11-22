package inventorymanagement.backend.util.pdf;

import inventorymanagement.backend.dto.ItemDTO;
import inventorymanagement.backend.util.enums.ImportExport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfFactory {
    public static byte[] produce(ItemDTO item, ImportExport type) {
        Document document;

        if (type == ImportExport.IMPORT) {
            try {
                document = Jsoup.parse(PdfFactory.class.getResourceAsStream("import.html"), "UTF-8", "");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                document = Jsoup.parse(PdfFactory.class.getResourceAsStream("export.html"), "UTF-8", "");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        Element name = document.getElementById("name");
        name.text(item.getName());
        Element weight = document.getElementById("weight");
        weight.text(item.getWeight() + " kg");
        Element company = document.getElementById("company");
        company.text(item.getCompany());
        Element arrival = document.getElementById("arrival");
        arrival.text(item.getArrival().toString());
        Element location = document.getElementById("location");
        location.text(item.getLocation());

        ITextRenderer renderer = new ITextRenderer();
        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext.setPrint(true);
        sharedContext.setInteractive(false);
        renderer.setDocumentFromString(document.html());
        renderer.layout();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.createPDF(baos);
        return baos.toByteArray();
    }
}
