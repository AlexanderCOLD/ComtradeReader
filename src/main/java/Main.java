import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws Exception {

		ArrayList<ComtradeData> data = ComtradeParser.parse("PhABC20");
		data.forEach(d->{ Charts.createAnalogChart(d.getName(),d.getNumb()-1); Charts.addSeries(d.getName() + ", " + d.getUnit(), d.getNumb()-1, 0); });
		data.forEach(d -> { d.getValues().forEach(v-> { Charts.addAnalogData(d.getNumb()-1, 0, v); }); });
	}

}
