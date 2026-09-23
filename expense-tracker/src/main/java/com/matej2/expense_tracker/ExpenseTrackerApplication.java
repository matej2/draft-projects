package com.matej2.expense_tracker;

import com.matej2.expense_tracker.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties({RsaKeyProperties.class})
public class ExpenseTrackerApplication {

	static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

}

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled")
class SchedulingConfiguration {
}