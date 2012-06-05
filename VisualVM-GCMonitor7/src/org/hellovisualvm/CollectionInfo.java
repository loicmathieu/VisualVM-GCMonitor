package org.hellovisualvm;

import java.lang.management.MemoryUsage;
import java.util.Map;



public class CollectionInfo {
	private int gcThreadCount;
	private long startTime;
	private long endTime;
	private long duration;
	private long id;
	private Map<String, MemoryUsage> memoryUsageBeforeGc;
	private Map<String, MemoryUsage> memoryUsageAfterGc;

	public int getGcThreadCount() {
		return gcThreadCount;
	}

	public void setGcThreadCount(int gcThreadCount) {
		this.gcThreadCount = gcThreadCount;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<String, MemoryUsage> getMemoryUsageBeforeGc() {
		return memoryUsageBeforeGc;
	}

	public void setMemoryUsageBeforeGc(Map<String, MemoryUsage> memoryUsageBeforeGc) {
		this.memoryUsageBeforeGc = memoryUsageBeforeGc;
	}

	public Map<String, MemoryUsage> getMemoryUsageAfterGc() {
		return memoryUsageAfterGc;
	}

	public void setMemoryUsageAfterGc(Map<String, MemoryUsage> memoryUsageAfterGc) {
		this.memoryUsageAfterGc = memoryUsageAfterGc;
	}

    @Override
    public String toString() {
        return "CollectionInfo{" + "gcThreadCount=" + gcThreadCount + ", startTime=" + startTime + ", endTime=" 
                + endTime + ", duration=" + duration + ", id=" + id + ", memoryUsageBeforeGc=" + memoryUsageBeforeGc 
                + ", memoryUsageAfterGc=" + memoryUsageAfterGc + '}';
    }

}
