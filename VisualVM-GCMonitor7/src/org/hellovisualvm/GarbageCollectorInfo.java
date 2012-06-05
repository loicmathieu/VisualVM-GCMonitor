package org.hellovisualvm;

import java.util.ArrayList;
import java.util.List;

public class GarbageCollectorInfo {
	private long count;
	private long totalTime;
	private boolean valid;
	private String name;
	private List<String> memoryPoolNames;
	private CollectionInfo lastCollection;
	private List<CollectionInfo> collectionInfos = new ArrayList<CollectionInfo>();


	public List<CollectionInfo> getCollectionInfos() {
		return collectionInfos;
	}

	public void setCollectionInfos(List<CollectionInfo> collectionInfos) {
		this.collectionInfos = collectionInfos;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMemoryPoolNames() {
		return memoryPoolNames;
	}

	public void setMemoryPoolNames(List<String> memoryPoolNames) {
		this.memoryPoolNames = memoryPoolNames;
	}

	public CollectionInfo getLastCollection() {
		return lastCollection;
	}

	public void setLastCollection(CollectionInfo lastCollection) {
		this.lastCollection = lastCollection;
	}

	public void addCollectionInfo(CollectionInfo info){
		collectionInfos.add(info);
	}

    @Override
    public String toString() {
        return "GarbageCollectorInfo{" + "count=" + count + ", totalTime=" + totalTime + ", valid=" + valid + ", name=" 
                + name + ", memoryPoolNames=" + memoryPoolNames + ", lastCollection=" + lastCollection 
                + ", collectionInfos=" + collectionInfos + '}';
    }
        
        
}
