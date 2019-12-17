package com.bharath.quartzscheduler;

import org.apache.log4j.Logger;
import org.quartz.*;

import java.util.Date;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ColorJob implements Job {

  public static String FAVOURITE_COLOR = "favouriteColor";
  public static String EXECUTION_COUNT = "executionCount";

  private static final Logger logger = Logger.getLogger(ColorJob.class);
  // member variables cannot be used to store the state
  private int _counter = 1;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobKey jobKey = context.getJobDetail().getKey();
    JobDataMap data = context.getJobDetail().getJobDataMap();
    String favoriteColor = data.getString(FAVOURITE_COLOR);
    int count = data.getInt(EXECUTION_COUNT);

    logger.info("ColorJob: " + jobKey + " executing at " + new Date() + "\n" +
                  "  favorite color is " + favoriteColor + "\n" +
                  "  execution count (from job map) is " + count + "\n" +
                  "  execution count (from job member variable) is " + _counter);
    count++;
    _counter++;
    data.put(EXECUTION_COUNT, count);
  }
}
