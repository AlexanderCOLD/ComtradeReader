import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ComtradeParser {

	public static ArrayList<ComtradeData> parse(String fileName) throws Exception {
		File path = new File("./Comtrade"); if(!path.exists()) path.mkdirs();
		File cfgFile = Files.walk(path.toPath())
				.filter(p -> FilenameUtils.getName(p.toString()).equals(fileName+".cfg"))
				.map(Path::toFile)
				.findFirst()
				.orElse(null);
		File datFile = Files.walk(path.toPath())
				.filter(p -> FilenameUtils.getName(p.toString()).equals(fileName+".dat"))
				.map(Path::toFile)
				.findFirst()
				.orElse(null);

		if(cfgFile==null || datFile==null) throw new Exception("Comtrade не найден");

		ArrayList<ComtradeData> signalList = parseCFG(cfgFile);
		List<String> datList = FileUtils.readLines(datFile, Charset.defaultCharset());

		datList.forEach(s -> {
			String[] line = s.split(",");
			IntStream.range(2, line.length).forEach(v -> {
				ComtradeData data = signalList.get(v-2);
				double val = data.getK1()!=null ? Double.parseDouble(line[v])*data.getK1()+data.getK2() : Double.parseDouble(line[v]);
				data.getValues().add(val);
			});
		});

		return signalList;
	}

	private static ArrayList<ComtradeData> parseCFG(File file) throws IOException {

		List<String> cfgList = FileUtils.readLines(file, Charset.defaultCharset());
		ArrayList<ComtradeData> dataList = new ArrayList<>();

		String[] signalNumbers = cfgList.get(1).split(",");
		int aNumb = Integer.parseInt(signalNumbers[1].replace("A",""));
		int dNumb = Integer.parseInt(signalNumbers[2].replace("D",""));

		IntStream.range(2, 2+aNumb).forEach(v -> {
			String[] sign = cfgList.get(v).split(",");
			dataList.add(new ComtradeData(Integer.parseInt(sign[0]), sign[1], sign[4], Double.parseDouble(sign[5]), Double.parseDouble(sign[6])));
		});
		IntStream.range(2+aNumb, 2+aNumb+dNumb).forEach(v -> {
			String[] sign = cfgList.get(v).split(",");
			dataList.add(new ComtradeData(Integer.parseInt(sign[0])+aNumb, sign[1]));
		});

		return dataList;
	}

}
