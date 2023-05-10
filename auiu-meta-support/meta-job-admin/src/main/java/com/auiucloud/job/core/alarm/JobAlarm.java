package com.auiucloud.job.core.alarm;

import com.auiucloud.job.core.model.XxlJobInfo;
import com.auiucloud.job.core.model.XxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

	/**
	 * job alarm
	 * @param info
	 * @param jobLog
	 * @return
	 */
	public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
