import lombok.Getter;

import java.util.ArrayList;

@Getter
class ComtradeData {

    private final int numb;
    private final String name;
    private String unit = "";
    private Double k1;
    private Double k2;
    private final ArrayList<Double> values = new ArrayList<>();

    public ComtradeData(int numb, String name, String unit, double k1, double k2) {
        this.numb = numb;
        this.name = name;
        this.unit = unit;
        this.k1 = k1;
        this.k2 = k2;
    }
    public ComtradeData(int numb, String name) {
        this.numb = numb;
        this.name = name;
    }


}