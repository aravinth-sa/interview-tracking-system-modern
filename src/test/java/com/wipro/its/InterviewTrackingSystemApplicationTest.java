package com.wipro.its;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest boots the FULL Spring application context using the test/dev profile.
// If this test passes, it means:
//   - All @Beans wired correctly (no missing dependencies)
//   - application-dev.yml loaded correctly
//   - H2 in-memory DB connected
//   - Security config valid
//   - Flyway disabled (dev profile) so no migration errors
@SpringBootTest
@ActiveProfiles("dev")   // uses application-dev.yml → H2 in-memory, no Flyway
class InterviewTrackingSystemApplicationTest {

    @Test
    void contextLoads() {
        // If Spring context starts without throwing an exception, this test passes.
        // Think of it as a smoke test — "does the app even start?"
    }
}
