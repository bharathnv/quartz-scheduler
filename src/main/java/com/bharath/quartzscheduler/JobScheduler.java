package com.bharath.quartzscheduler;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class JobScheduler {

  private static final Logger logger = Logger.getLogger(JobScheduler.class);

  public static void main(String[] args) {
    try {
      // specify the job' s details..
      JobDetail jobDetail = JobBuilder.newJob(TestJob.class).withIdentity("testJob").build();

      JobDetail jobDetail1 = JobBuilder.newJob(ColorJob.class).withIdentity("colorJob1", "group1").usingJobData(new JobDataMap()).build();
      jobDetail1.getJobDataMap().put(ColorJob.FAVOURITE_COLOR, "Green");
      jobDetail1.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);
      JobDetail jobDetail2 = JobBuilder.newJob(ColorJob.class).withIdentity("colorJob2", "group1").usingJobData(new JobDataMap()).build();
      jobDetail2.getJobDataMap().put(ColorJob.FAVOURITE_COLOR, "Red");
      jobDetail2.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);

      JobDetail job = JobBuilder.newJob(StatefulDumbJob.class).withIdentity("statefulJob1", "statefulJobGroup1").usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L).build();
      job.getJobDataMap().put(StatefulDumbJob.NUM_EXECUTIONS, 1);
      JobDetail job1 = JobBuilder.newJob(StatefulDumbJob.class).withIdentity("statefulJob2", "statefulJobGroup1").usingJobData(StatefulDumbJob.EXECUTION_DELAY, 10000L).build();
      job1.getJobDataMap().put(StatefulDumbJob.NUM_EXECUTIONS, 1);

      // specify the running period of the job
      // simple trigger
//      Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

      // cron trigger
      // 0/20 * * * * ? every 20s
      // 15 0/2 * * * ? every other minute, starting at 15 seconds past the minute
      // 0 0/2 8-17 * * ? every other minute, between 8am and 5pm (17 o’clock)
      // 0 0/3 17-23 * * ? every three minutes but only between 5pm and 11pm
      // 0 0 10 1,15 * ? run at 10am on the 1st and 15th days of the month
      // 0/30 * * ? * MON-FRI run every 30 seconds on Weekdays (Monday through Friday)
      // 0/30 * * ? * SAT,SUN
      CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                              .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * ? * MON-FRI")).build();

      SimpleTrigger trigger1 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger1", "group1")
                                  .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4))
                                  .build();

      SimpleTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity("simpleTrigger2", "group1")
                                   .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4))
                                   .build();

      SimpleTrigger statefulTrigger = TriggerBuilder.newTrigger().withIdentity("statefulTrigger1", "statefulJobGroup1")
                                          .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
                                          .build();
      SimpleTrigger statefulTrigger1 = TriggerBuilder.newTrigger().withIdentity("statefulTrigger2", "statefulJobGroup1")
                                          .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever().withMisfireHandlingInstructionNowWithExistingCount()) // set misfire instruction
                                          .build();

      //schedule the job
      SchedulerFactory schedulerFactory = new StdSchedulerFactory();
      Scheduler scheduler = schedulerFactory.getScheduler();
      scheduler.start();
//      scheduler.scheduleJob(jobDetail, trigger);
//      scheduler.scheduleJob(jobDetail1, trigger1);
//      scheduler.scheduleJob(jobDetail2, trigger2);
      scheduler.scheduleJob(job, statefulTrigger);
      scheduler.scheduleJob(job1, statefulTrigger1);
      Thread.sleep(300000);
      scheduler.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("error --> {}", e);
    }
  }
}
