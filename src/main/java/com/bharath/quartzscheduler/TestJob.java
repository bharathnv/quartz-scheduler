package com.bharath.quartzscheduler;

import org.apache.log4j.Logger;
import org.quartz.*;

import java.util.Date;

public class TestJob implements Job {
  private Logger logger = Logger.getLogger(TestJob.class);

  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    JobKey key = jobExecutionContext.getJobDetail().getKey();
    logger.error("SimpleJob says: "+key+" executing at "+new Date());
  }
}
