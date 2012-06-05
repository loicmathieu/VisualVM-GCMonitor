package org.hellovisualvm;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;



public class MonitorGcTool {
	private static final String[] ATTRIBUTES = new String[] {"CollectionCount", "CollectionTime", "LastGcInfo", "MemoryPoolNames", "Name", "Valid"};
	private static final String LAST_GARBAGE_ATTR = "LastGcInfo";

	private MBeanServerConnection serverConnection;

	public MonitorGcTool(MBeanServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public GarbageCollectorInfo getGarbageCollectorInfo(ObjectName objName){
		GarbageCollectorInfo info = new GarbageCollectorInfo();
		try {
			AttributeList infos = serverConnection.getAttributes(objName, ATTRIBUTES);
			Attribute count = (Attribute) infos.get(0);
			info.setCount((Long) count.getValue());

			Attribute totalTime = (Attribute) infos.get(1);
			info.setTotalTime((Long) totalTime.getValue());

			Attribute lastCollectionAttr = (Attribute) infos.get(2);
			CompositeDataSupport lastCollectionData = (CompositeDataSupport) lastCollectionAttr.getValue();
			Integer gcThreadCount = (Integer) lastCollectionData.get("GcThreadCount");
			Long gcDuration = (Long) lastCollectionData.get("duration");
			Long endTime = (Long) lastCollectionData.get("endTime");
			Long startTime = (Long) lastCollectionData.get("startTime");
			Long id = (Long) lastCollectionData.get("id");
			TabularDataSupport memoryUsageAfterGc = (TabularDataSupport) lastCollectionData.get("memoryUsageAfterGc");
			Map<String, MemoryUsage> memoryUsageAfterGcMap = new HashMap<String, MemoryUsage>();
			for(Entry<Object, Object> entry : memoryUsageAfterGc.entrySet()){
				CompositeDataSupport value = (CompositeDataSupport) entry.getValue();
				String key = (String) value.get("key");
				CompositeDataSupport content = (CompositeDataSupport) value.get("value");
				Long committed = (Long) content.get("committed");
				Long init = (Long) content.get("init");
				Long max = (Long) content.get("max");
				Long used = (Long) content.get("used");
				MemoryUsage memUsage = new MemoryUsage(init, used, committed, max);

				memoryUsageAfterGcMap.put(key, memUsage);
			}
			TabularDataSupport memoryUsageBeforeGc = (TabularDataSupport) lastCollectionData.get("memoryUsageBeforeGc");
			Map<String, MemoryUsage> memoryUsageBeforeGcMap = new HashMap<String, MemoryUsage>();
			for(Entry<Object, Object> entry : memoryUsageBeforeGc.entrySet()){
				CompositeDataSupport value = (CompositeDataSupport) entry.getValue();
				String key = (String) value.get("key");
				CompositeDataSupport content = (CompositeDataSupport) value.get("value");
				Long committed = (Long) content.get("committed");
				Long init = (Long) content.get("init");
				Long max = (Long) content.get("max");
				Long used = (Long) content.get("used");
				MemoryUsage memUsage = new MemoryUsage(init, used, committed, max);

				memoryUsageBeforeGcMap.put(key, memUsage);
			}

			CollectionInfo lastCollection = new CollectionInfo();
			lastCollection.setGcThreadCount(gcThreadCount);
			lastCollection.setDuration(gcDuration);
			lastCollection.setStartTime(startTime);
			lastCollection.setEndTime(endTime);
			lastCollection.setId(id);
			lastCollection.setMemoryUsageAfterGc(memoryUsageAfterGcMap);
			lastCollection.setMemoryUsageBeforeGc(memoryUsageBeforeGcMap);

			info.setLastCollection(lastCollection);
			info.addCollectionInfo(lastCollection);

			Attribute memoryPoolNames = (Attribute) infos.get(3);
			String[] memoryPoolNamesTab = (String[]) memoryPoolNames.getValue();
			info.setMemoryPoolNames(Arrays.asList(memoryPoolNamesTab));

			Attribute name = (Attribute) infos.get(4);
			info.setName((String) name.getValue());

			Attribute valid = (Attribute) infos.get(5);
			info.setValid((Boolean) valid.getValue());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return info;
	}

	public CollectionInfo getLastGarbageInfo(ObjectName objName){
		CollectionInfo info = new CollectionInfo();
		try {
			CompositeDataSupport lastCollectionData = (CompositeDataSupport) serverConnection.getAttribute(objName, LAST_GARBAGE_ATTR);
			Integer gcThreadCount = (Integer) lastCollectionData.get("GcThreadCount");
			Long gcDuration = (Long) lastCollectionData.get("duration");
			Long endTime = (Long) lastCollectionData.get("endTime");
			Long startTime = (Long) lastCollectionData.get("startTime");
			Long id = (Long) lastCollectionData.get("id");
			TabularDataSupport memoryUsageAfterGc = (TabularDataSupport) lastCollectionData.get("memoryUsageAfterGc");
			Map<String, MemoryUsage> memoryUsageAfterGcMap = new HashMap<String, MemoryUsage>();
			for(Entry<Object, Object> entry : memoryUsageAfterGc.entrySet()){
				CompositeDataSupport value = (CompositeDataSupport) entry.getValue();
				String key = (String) value.get("key");
				CompositeDataSupport content = (CompositeDataSupport) value.get("value");
				Long committed = (Long) content.get("committed");
				Long init = (Long) content.get("init");
				Long max = (Long) content.get("max");
				Long used = (Long) content.get("used");
				MemoryUsage memUsage = new MemoryUsage(init, used, committed, max);

				memoryUsageAfterGcMap.put(key, memUsage);
			}
			TabularDataSupport memoryUsageBeforeGc = (TabularDataSupport) lastCollectionData.get("memoryUsageBeforeGc");
			Map<String, MemoryUsage> memoryUsageBeforeGcMap = new HashMap<String, MemoryUsage>();
			for(Entry<Object, Object> entry : memoryUsageBeforeGc.entrySet()){
				CompositeDataSupport value = (CompositeDataSupport) entry.getValue();
				String key = (String) value.get("key");
				CompositeDataSupport content = (CompositeDataSupport) value.get("value");
				Long committed = (Long) content.get("committed");
				Long init = (Long) content.get("init");
				Long max = (Long) content.get("max");
				Long used = (Long) content.get("used");
				MemoryUsage memUsage = new MemoryUsage(init, used, committed, max);

				memoryUsageBeforeGcMap.put(key, memUsage);
			}

			info.setGcThreadCount(gcThreadCount);
			info.setDuration(gcDuration);
			info.setStartTime(startTime);
			info.setEndTime(endTime);
			info.setId(id);
			info.setMemoryUsageAfterGc(memoryUsageAfterGcMap);
			info.setMemoryUsageBeforeGc(memoryUsageBeforeGcMap);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (AttributeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (MBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return info;
	}
}