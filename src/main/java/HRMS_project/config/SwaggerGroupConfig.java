package HRMS_project.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupConfig {

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin APIs")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi hrApi() {
        return GroupedOpenApi.builder()
                .group("HR Manager APIs")
                .pathsToMatch("/hr/**")
                .build();
    }

    @Bean
    public GroupedOpenApi employeeApi() {
        return GroupedOpenApi.builder()
                .group("Employee APIs")
                .pathsToMatch("/employee/**")
                .build();
    }

    @Bean
    public GroupedOpenApi candidateApi() {
        return GroupedOpenApi.builder()
                .group("Candidate APIs")
                .pathsToMatch("/candidate/**")
                .build();
    }

    @Bean
    public GroupedOpenApi managerApi() {
        return GroupedOpenApi.builder()
                .group("Manager APIs")
                .pathsToMatch("/manager/**")
                .build();
    }
}
