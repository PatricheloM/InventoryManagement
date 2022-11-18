package inventorymanagement.backend.dto;

import java.util.Date;
import java.util.Objects;

public class ItemDTO {

    private String name;
    private int weight;
    private String company;
    private Date arrival;
    private String location;

    public ItemDTO() {
    }

    public ItemDTO(String name, int weight, String company, Date arrival, String location) {
        this.name = name;
        this.weight = weight;
        this.company = company;
        this.arrival = arrival;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return weight == itemDTO.weight && Objects.equals(name, itemDTO.name) && Objects.equals(company, itemDTO.company) && Objects.equals(arrival, itemDTO.arrival) && Objects.equals(location, itemDTO.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, company, arrival, location);
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", company='" + company + '\'' +
                ", arrival=" + arrival +
                ", location='" + location + '\'' +
                '}';
    }
}
