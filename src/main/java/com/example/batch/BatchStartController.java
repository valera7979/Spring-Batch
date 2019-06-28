package com.example.batch;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class BatchStartController {

	JobLauncher jobLauncher;

	Job job;

	@GetMapping("/job")
	public void startJob() {
		Map<String, JobParameter> parameters = new HashMap<>();
		parameters.put("Job start time", new JobParameter(Instant.now().toEpochMilli()));
		parameters.put("JobThread", new JobParameter(Thread.currentThread().getId()));

		try {
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameters));
			log.info("Job started in thread :" + jobExecution.getJobParameters().getString("JobThread"));
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException | JobInstanceAlreadyCompleteException e) {
			log.error("Something sent wrong during job execution", e);
		}
	}
}
