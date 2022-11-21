package inventorymanagement.backend.dto;

import java.util.Objects;

public class PdfDTO {
    private String pdf;

    public PdfDTO() {
    }

    public PdfDTO(String pdf) {
        this.pdf = pdf;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfDTO pdfDTO = (PdfDTO) o;
        return Objects.equals(pdf, pdfDTO.pdf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdf);
    }

    @Override
    public String toString() {
        return "PdfDTO{" +
                "pdf='" + pdf + '\'' +
                '}';
    }
}
