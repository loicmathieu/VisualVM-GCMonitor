/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hellovisualvm;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lmathieu
 */
public class SummaryPanel  extends JPanel implements GarbageCollectorModelListener {
    private long applicationStart;
    private Map<String, Long> gcDurations = new HashMap<String, Long>(); 
    private Map<String, Long> nbGcs = new HashMap<String, Long>();
    private JLabel applicationDurationLbl;
    private JLabel numberGcLbl;
    private JLabel allGcDurationLbl;
    private JLabel percentTimeInGcLbl;
    private JLabel fullGcDurationLbl;
    private JLabel percentTimeInFullGcLbl;
    
    private static List<String> fullGcNames = new ArrayList<String>();
    static {
        fullGcNames.add("");
    }
    
    public SummaryPanel() {
        applicationStart = System.currentTimeMillis();        
        
        numberGcLbl = new JLabel();
        numberGcLbl.setBorder(BorderFactory.createEmptyBorder(14, 8, 14, 8));
        add(numberGcLbl, BorderLayout.WEST);
        
        applicationDurationLbl = new JLabel();
        applicationDurationLbl.setBorder(BorderFactory.createEmptyBorder(14, 8, 14, 8));
        add(applicationDurationLbl, BorderLayout.WEST);
        
        allGcDurationLbl = new JLabel();
        allGcDurationLbl.setBorder(BorderFactory.createEmptyBorder(14, 8, 14, 8));
        add(allGcDurationLbl, BorderLayout.WEST);
        
        percentTimeInGcLbl = new JLabel();
        percentTimeInGcLbl.setBorder(BorderFactory.createEmptyBorder(14, 8, 14, 8));
        add(percentTimeInGcLbl, BorderLayout.WEST);
        
        fullGcDurationLbl = new JLabel();
        fullGcDurationLbl.setBorder(BorderFactory.createEmptyBorder(14, 8, 14, 8));
        add(fullGcDurationLbl, BorderLayout.WEST);
        
        percentTimeInFullGcLbl = new JLabel();
        percentTimeInFullGcLbl.setBorder(BorderFactory.createEmptyBorder(14, 8, 14, 8));
        add(percentTimeInFullGcLbl, BorderLayout.WEST);
    }

    public void modelUpdated(GarbageCollectorModel model) {
        long applicationDuration = System.currentTimeMillis() - applicationStart;
        applicationDurationLbl.setText("Duration of measurement : " + (applicationDuration/1000) + "s");
        
        String collectorName = model.getName();
        long gcCount = 0;
        if(nbGcs.containsKey(collectorName)){
            gcCount = nbGcs.get(collectorName);
            gcCount++;
            nbGcs.put(collectorName, gcCount);
        }                        
        long totalGcCount = 0;
        for(Long count : nbGcs.values()){
            totalGcCount += count;
        }
        allGcDurationLbl.setText("Number of GC events : " + totalGcCount);
        
        long gcDuration = model.getCummulativeGcDuration();
        gcDurations.put(model.getName(), gcDuration);                
        long allGcDuration = 0;
        for(Long l : gcDurations.values()){
            allGcDuration += l;
        }
        allGcDurationLbl.setText("Time Spent in GC : " + (allGcDuration/1000) + "s");

        percentTimeInGcLbl.setText("Percentage of Time in GC : " + ((gcDuration * 100) / applicationDuration) + "%");
        
        long fullGcDuration = 0;
        for(String key : gcDurations.keySet()){
            if(fullGcNames.contains(key)){
                fullGcDuration = gcDurations.get(key);
                break;
            }
        }
        fullGcDurationLbl.setText("Time Spent in Full GC : " + (fullGcDuration/1000) + "s");

        percentTimeInFullGcLbl.setText("Percentage of Time in Full GC : " + ((fullGcDuration * 100) / applicationDuration) + "%");
    }
    
}
