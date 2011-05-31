package org.fastorm.stats;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.sqlDialects.ISqlStrings;

public class StatsKey {

	private final int databaseSize;
	private final FastOrmOptions options;
	private final Class<? extends IMemoryManager> memoryManager;
	private final ISqlStrings sqlStrings;

	public StatsKey(IFastOrm fastOrm, int databaseSize) {
		IFastOrmContainer container = fastOrm.getContainer();
		this.databaseSize = databaseSize;
		this.options = container.getOptions();
		this.memoryManager = container.getMemoryManager().getClass();
		this.sqlStrings = container.getSqlStrings();
	}

	@Override
	public String toString() {
		return "StatsKey [databaseSize=" + databaseSize + ", options=" + options + ", memoryManager=" + memoryManager + ", sqlStrings=" + sqlStrings + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + databaseSize;
		result = prime * result + ((memoryManager == null) ? 0 : memoryManager.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((sqlStrings == null) ? 0 : sqlStrings.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatsKey other = (StatsKey) obj;
		if (databaseSize != other.databaseSize)
			return false;
		if (memoryManager == null) {
			if (other.memoryManager != null)
				return false;
		} else if (!memoryManager.equals(other.memoryManager))
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (sqlStrings == null) {
			if (other.sqlStrings != null)
				return false;
		} else if (!sqlStrings.equals(other.sqlStrings))
			return false;
		return true;
	}

}
