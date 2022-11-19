package inventorymanagement.backend.dto;

import java.util.Date;
import java.util.Objects;

public class DateQueryDTO {
    private Date start;
    private Date end;

    public DateQueryDTO() {
    }

    public DateQueryDTO(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateQueryDTO that = (DateQueryDTO) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "DateQueryDTO{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
