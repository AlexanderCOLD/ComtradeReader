import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
 
public class Charts {
	
	private static Charts self = new Charts();
	private static final ArrayList<XYSeriesCollection> datasetsAnalog = new ArrayList<XYSeriesCollection>();
	private static final ArrayList<XYSeries> datasetsDiscrete = new ArrayList<XYSeries>();
	private static CombinedDomainXYPlot plot;
	private static XYSeries tempSeries;
	private static double timeStep = 0.00025; // шаг дискретизации при 80 т. за период
	private static double currentTime = 0.0;
	private static boolean lastData=false;
	
  public Charts(){
  	self = this;
    plot = new CombinedDomainXYPlot(new NumberAxis("Время, сек"));

	  JFreeChart chart = new JFreeChart("Comtrade Viewer", plot);
    chart.setBorderPaint(Color.black);
    chart.setBorderVisible(true);
    chart.setBackgroundPaint(Color.white);

	  JFrame frame = new JFrame("Comtrade Viewer");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new ChartPanel(chart));
    frame.setSize(1024,768);
    frame.show();   
  }
  
  /**
   * Добавить аналоговый график
   * @param name - имя графика
   * @param number - порядковый номер
   */
  public static void createAnalogChart(String name, int number){ 
	  XYSeriesCollection dataset = new XYSeriesCollection();
	  NumberAxis rangeAxis = new NumberAxis(name); rangeAxis.setAutoRangeIncludesZero(false);
	  XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer() ); 
	  subplot.setBackgroundPaint(Color.BLACK);
	  plot.add(subplot); 
	  subplot.setWeight(7);
	  datasetsAnalog.add(dataset);
  }
  
  /**
   * Добавить дискретный график
   * @param name - имя графика
   * @param number - порядковый номер
   */
  public static void createDiscreteChart(String name, int number){ 
	  XYSeriesCollection dataset = new XYSeriesCollection();
	  NumberAxis rangeAxis = new NumberAxis(name); 
	  rangeAxis.setAutoRangeIncludesZero(false);
	  XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer() );
	  plot.add(subplot); 
	  subplot.setWeight(1);
	  XYStepAreaRenderer xysteparearenderer = new XYStepAreaRenderer(2); 
	  subplot.setRenderer(xysteparearenderer);
	  XYSeries series = new XYSeries(name);
	  datasetsDiscrete.add(series);
	  dataset.addSeries(series);
  }
  
  
  /**
   * Добавляет в указанный аналоговый график новый сигнал
   * @param name - имя аналогового сигнала
   * @param chartNumber - номер графика
   * @param number - номер сигнала
   */
  public static void addSeries(String name, int chartNumber, int number){
	  XYSeries series = new XYSeries(name); series.add(0.0, 0.0);
	  datasetsAnalog.get(chartNumber).addSeries(series);
  }
 
  /**
   * Строит аналоговый сигнал на графике
   * @param chart - номер графика
   * @param series - номер сигнала
   * @param data - значение (double)
   */
  public static void addAnalogData(int chart, int series, double data){
	  tempSeries = (XYSeries) datasetsAnalog.get(chart).getSeries().get(series);
	  currentTime = tempSeries.getMaxX()+timeStep;
	  tempSeries.add(currentTime, data);
  }

  /**
   * Строит аналоговый сигнал на графике
   * @param chart - номер графика
   * @param series - номер сигнала
   * @param data - значение (double)
   * @param timeStep - шаг по времени
   */
  public static void addAnalogData(int chart, int series, double data, double timeStep){
	  tempSeries = (XYSeries) datasetsAnalog.get(chart).getSeries().get(series);
	  currentTime = tempSeries.getMaxX()+timeStep;
	  tempSeries.add(currentTime, data);
  }
  
  /**
   * Строит дискретный сигнал на графике
   * @param chart - номер дисретного сигнала
   * @param data - значение (true/false)
   */
  public static void addDiscreteData(int chart, boolean data){
	  tempSeries = (XYSeries) datasetsDiscrete.get(chart);
	  if(!tempSeries.isEmpty()) lastData = tempSeries.getY(tempSeries.getItemCount()-1).doubleValue()==1;
	  if(!lastData && data) tempSeries.add(currentTime, data?1.0:0.0);
	  if(lastData && !data) tempSeries.add(currentTime, data?1.0:0.0);  
  }

}