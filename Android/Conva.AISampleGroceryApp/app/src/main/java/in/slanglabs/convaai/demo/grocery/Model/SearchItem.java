package in.slanglabs.convaai.demo.grocery.Model;

import java.io.Serializable;
import java.util.Objects;

public class SearchItem implements Serializable {
    public String name = "";
    public String brandName = "";
    public String productName = "";
    public String size = "";
    public int quantity = 0;
    public boolean isAddToCart = false;
    public String offerAcceptance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchItem that = (SearchItem) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brandName, size, quantity, isAddToCart, offerAcceptance);
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "name='" + name + '\'' +
                ", brandName='" + brandName + '\'' +
                ", productName='" + productName + '\'' +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", isAddToCart=" + isAddToCart +
                ", offerAcceptance=" + offerAcceptance +
                '}';
    }
}
