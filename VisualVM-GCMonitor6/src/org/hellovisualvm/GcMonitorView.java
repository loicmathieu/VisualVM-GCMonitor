/*
 * Copyright 2007-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package org.hellovisualvm;

import com.sun.tools.visualvm.application.Application;
import com.sun.tools.visualvm.core.ui.DataSourceView;
import com.sun.tools.visualvm.core.ui.components.DataViewComponent;
import com.sun.tools.visualvm.tools.jmx.JmxModel;
import com.sun.tools.visualvm.tools.jmx.JmxModelFactory;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import org.openide.util.Utilities;

public class GcMonitorView extends DataSourceView {

    private DataViewComponent dvc;
    //Make sure there is an image at this location in your project:
    private static final String IMAGE_PATH = "org/hellovisualvm/coredump.png"; // NOI18N
    private List<GarbageCollectorModel> models = new ArrayList<GarbageCollectorModel>();
    
    private static Map<String, Point> positions = new HashMap<String, Point>();
    static {
        positions.put( "PS MarkSweep", new Point(DataViewComponent.BOTTOM_LEFT,10));
        positions.put( "Copy", new Point(DataViewComponent.BOTTOM_LEFT,10));
        positions.put( "PS Scavenge", new Point(DataViewComponent.BOTTOM_RIGHT,10));
        positions.put( "MarkSweepCompact", new Point(DataViewComponent.BOTTOM_RIGHT,10));
    }

    public GcMonitorView(Application Application) {
        //closeable/not = last boolean (closebale views for thread/heap/Application
        super(Application,"GC Monitor", new ImageIcon(Utilities.loadImage(IMAGE_PATH, true)).getImage(), 60, false);
    }

    protected DataViewComponent createComponent() {
        //Data area for master view:
        JEditorPane generalDataArea = new JEditorPane();
        generalDataArea.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));

        //Master view:
        DataViewComponent.MasterView masterView = new DataViewComponent.MasterView("GC Monitors", "View of GC Monitoring", generalDataArea);
        DataViewComponent.MasterViewConfiguration masterConfiguration = new DataViewComponent.MasterViewConfiguration(false);
        dvc = new DataViewComponent(masterView, masterConfiguration);
        
        //the summary view
        SummaryPanel summaryPanel = new SummaryPanel();
        dvc.addDetailsView(new DataViewComponent.DetailsView( "Summary", "Summary", DataViewComponent.TOP_LEFT, summaryPanel, null), DataViewComponent.TOP_LEFT);
        
        // the magic that gets a handle on all instances of GarbageCollectorMXBean
        findGcMonitors();
        for ( GarbageCollectorModel model : models) {
            GcMonitorPanel panel = new GcMonitorPanel(model.getName());
            Point position = calculatePosition(model.getName());
            dvc.addDetailsView(new DataViewComponent.DetailsView(  model.getName(), "gc charts", position.y, panel, null), position.x);
            model.registerListener(panel);
            model.registerListener(summaryPanel);
            model.init();
        }
        return dvc;
    }

    private Point calculatePosition(String name) {
        return positions.get(name);
    }
    
    private void findGcMonitors() {
        try {
            JmxModel jmx = JmxModelFactory.getJmxModelFor((Application)super.getDataSource());
            MBeanServerConnection conn = jmx.getMBeanServerConnection();
            ObjectName pattern = new ObjectName("java.lang:type=GarbageCollector,name=*");  
            for (ObjectName name : conn.queryNames(pattern, null)) {
                System.out.println("ObjectName : " + name.getCanonicalName());
                GarbageCollectorModel model =  new GarbageCollectorModel(name, jmx, conn);
                models.add(model);
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
}