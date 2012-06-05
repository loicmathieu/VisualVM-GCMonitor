/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hellovisualvm;

import com.sun.tools.visualvm.tools.jmx.CachedMBeanServerConnectionFactory;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.MBeanCacheListener;
import java.util.HashSet;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

/**
 *
 * @author lmathieu
 */
public class GarbageCollectorModel implements MBeanCacheListener {
    private Set<GarbageCollectorModelListener> listeners = new HashSet<GarbageCollectorModelListener>();
    private ObjectName mbeanName;
    private String name;
    private MonitorGcTool monitor;
    private GarbageCollectorInfo info;
    private long startMonitoringDate;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GarbageCollectorInfo getInfo() {
        return info;
    }

    public void setInfo(GarbageCollectorInfo info) {
        this.info = info;
    }
    
   public GarbageCollectorModel(ObjectName mbeanName, JmxModel model, MBeanServerConnection mbeanServerConnection) 
           throws Exception {
        this.mbeanName = mbeanName;
        CachedMBeanServerConnectionFactory.getCachedMBeanServerConnection(model, 1000).addMBeanCacheListener(this);
        name = mbeanServerConnection.getAttribute(mbeanName, "Name").toString();
        monitor = new MonitorGcTool(mbeanServerConnection);
        startMonitoringDate = System.currentTimeMillis();
        info = monitor.getGarbageCollectorInfo(mbeanName);
    }
   
    public long getMonitoringDuration(){
        return System.currentTimeMillis() - startMonitoringDate;
    }
    
    public long getCummulativeGcDuration(){
        long duration = 0;
        for(CollectionInfo colInfo: info.getCollectionInfos()){
            duration += colInfo.getDuration();
        }
        return duration;
    }
    
    public long getFullGcDuration(){
        //TODO
        return 0;
    }
            
   
    public void flushed() {
        CollectionInfo colInfo = monitor.getLastGarbageInfo(mbeanName);
        
        if(info.getLastCollection().getId() != colInfo.getId()){
            System.out.println("record changed");
            //record last collection
            info.setLastCollection(colInfo);
            info.addCollectionInfo(colInfo);
            
            //tickleListeners
            for(GarbageCollectorModelListener listener : listeners){
                listener.modelUpdated(this);
            }
        } 
    }
    
    public void init() {
        //tickleListeners to write the first point
        for(GarbageCollectorModelListener listener : listeners){
            listener.modelUpdated(this);
        }
    }
    
    public void registerListener(GarbageCollectorModelListener listener){
        listeners.add(listener);
    }
    
}
