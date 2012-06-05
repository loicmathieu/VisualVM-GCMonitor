/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hellovisualvm;

import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import java.lang.management.MemoryUsage;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author lmathieu
 */
public class GcMonitorPanel extends JPanel implements GarbageCollectorModelListener {

    private SimpleXYChartSupport chart;

    public GcMonitorPanel(String name) {
        //setLayout(new BorderLayout());
        SimpleXYChartDescriptor description = SimpleXYChartDescriptor.bytes(0, 20, 100, false, 1000);
        description.setChartTitle(name);        
        description.setDetailsItems(new String[]{"Application Duration (s)", "GC Duration (s)", "% time in GC"});
        
        description.addLineItems("Used");
        //description.addLineItems("Used");
        
        description.setXAxisDescription("<html>Time</html>");
        description.setYAxisDescription("<html>HeapUsageAfterGc</html>");

        
        chart = ChartFactory.createSimpleXYChart(description);
        add(chart.getChart());
    }
    

    public void modelUpdated(GarbageCollectorModel model) {
        System.out.println("Update view");
        long[] dataPoints = new long[1];
        long totalMemUsage = 0;
        for(Map.Entry<String, MemoryUsage> entry : model.getInfo().getLastCollection().getMemoryUsageAfterGc().entrySet()){            
            MemoryUsage usage = entry.getValue();
            System.out.println("Find memory usage : " + usage.toString());
            totalMemUsage += usage.getUsed();
        }
        dataPoints[0] = totalMemUsage;
        //dataPoints[1] = model.getUsed();
        System.out.println("Add point : " + model.getInfo().getLastCollection().getEndTime() + " - " + totalMemUsage);
        chart.addValues(model.getInfo().getLastCollection().getEndTime(), dataPoints);
        
        long applicationDuration = model.getMonitoringDuration();
        long gcDuration = model.getCummulativeGcDuration();
        String [] details = new String [3];
        details[0] = String.valueOf(applicationDuration / 1000);
        details[1] = String.valueOf(gcDuration / 1000);
        details[2] = String.valueOf((gcDuration * 100) / applicationDuration);
        chart.updateDetails(details);
    }

}