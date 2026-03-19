package com.poly.project_management;

import com.poly.project_management.entity.RoleEntity;
import com.poly.project_management.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(new RoleEntity("USER"));
			}
			if (roleRepository.findByName("MANAGER").isEmpty()) {
				roleRepository.save(new RoleEntity("MANAGER"));
			}
		};
	}
}