package com.planit.repository

import com.planit.config.JpaConfig
import com.planit.domain.Schedule
import com.planit.domain.User
import com.planit.domain.enums.Role
import com.planit.domain.enums.SchedulePriority
import com.planit.domain.enums.UserProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime

@DataJpaTest
@Import(JpaConfig::class)
class ScheduleRepositoryTest {

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        // given
        testUser = userRepository.save(User(
            email = "test@test.com",
            nickname = "testuser",
            provider = UserProvider.GOOGLE,
            providerId = "12345",
            role = Role.USER
        ))
    }

    @Test
    @DisplayName("새로운 일정을 저장하고 ID로 조회하면 정상적으로 조회된다")
    fun saveAndFindById() {
        // given
        val newSchedule = Schedule(
            user = testUser,
            title = "Test Schedule",
            description = "This is a test schedule.",
            priority = SchedulePriority.MEDIUM,
            isCompleted = false,
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().plusHours(1)
        )

        // when
        val savedSchedule = scheduleRepository.save(newSchedule)

        // then
        val foundSchedule = scheduleRepository.findById(savedSchedule.id!!).orElse(null)
        assertThat(foundSchedule).isNotNull
        assertThat(foundSchedule?.title).isEqualTo(newSchedule.title)
        assertThat(foundSchedule?.user?.id).isEqualTo(testUser.id)
    }

    @Test
    @DisplayName("특정 사용자의 모든 일정을 조회한다")
    fun findAllByUser() {
        // given
        scheduleRepository.save(Schedule(testUser, "Schedule 1", null, SchedulePriority.HIGH, false, LocalDateTime.now(), LocalDateTime.now().plusHours(2)))
        scheduleRepository.save(Schedule(testUser, "Schedule 2", null, SchedulePriority.LOW, true, LocalDateTime.now(), LocalDateTime.now().plusHours(3)))

        val otherUser = userRepository.save(User("other@test.com", "otheruser", UserProvider.KAKAO, "67890", Role.USER))
        scheduleRepository.save(Schedule(otherUser, "Other User's Schedule", null, SchedulePriority.MEDIUM, false, LocalDateTime.now(), LocalDateTime.now().plusHours(1)))


        // when
        val schedules = scheduleRepository.findAllByUser(testUser)

        // then
        assertThat(schedules).hasSize(2)
        assertThat(schedules.all { it.user.id == testUser.id }).isTrue()
    }
} 