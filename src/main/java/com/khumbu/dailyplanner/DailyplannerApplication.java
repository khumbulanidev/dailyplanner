package com.khumbu.dailyplanner;

import com.khumbu.dailyplanner.controller.DayController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DailyplannerApplication  implements CommandLineRunner {
	@Autowired
	ApplicationContext applicationContext;

	private Logger logger = LoggerFactory.getLogger(DailyplannerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DailyplannerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Inside the Run method");
		DayController dayController=applicationContext.getBean(DayController.class);
		//DataSourceInfo dataSourceInfo= applicationContext.getBean(DataSourceInfo.class);

	}

}
