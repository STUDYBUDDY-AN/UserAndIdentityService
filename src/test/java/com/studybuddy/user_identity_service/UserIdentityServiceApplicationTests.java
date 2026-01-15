package com.studybuddy.user_identity_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:mysql://localhost:3306/user_identity?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
		"spring.datasource.username=root",
		"spring.datasource.password=Anantha2001@"
})
class UserIdentityServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
