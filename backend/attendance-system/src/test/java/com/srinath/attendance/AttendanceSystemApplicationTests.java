package com.srinath.attendance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // ⭐ VERY IMPORTANT
class AttendanceSystemApplicationTests {

	@Test
	void contextLoads() {
	}
}