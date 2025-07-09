package com.planit.domain

import com.planit.config.JpaConfig
import com.planit.config.QueryDslConfig
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import java.time.LocalDateTime

@DataJpaTest
@Import(JpaConfig::class, QueryDslConfig::class)
class BaseEntityAuditTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun `엔티티 저장 시 createdAt과 updatedAt이 자동으로 생성된다`() {
        // given
        val testEntity = TestEntity()

        // when
        entityManager.persist(testEntity)
        entityManager.flush()
        entityManager.clear()

        // then
        val foundEntity = entityManager.find(TestEntity::class.java, testEntity.id)
        assertThat(foundEntity).isNotNull
        assertThat(foundEntity.createdAt).isNotNull
        assertThat(foundEntity.updatedAt).isNotNull
        assertThat(foundEntity.createdAt).isBeforeOrEqualTo(LocalDateTime.now())
        assertThat(foundEntity.updatedAt).isBeforeOrEqualTo(LocalDateTime.now())
    }

    @Test
    fun `엔티티 수정 시 updatedAt이 자동으로 갱신된다`() {
        // given
        var testEntity = TestEntity(name = "before")
        entityManager.persist(testEntity)
        entityManager.flush()
        val initialUpdatedAt = testEntity.updatedAt
        val entityId = testEntity.id

        entityManager.clear() // 영속성 컨텍스트 초기화

        // when
        // 시간을 약간 지연시켜 updatedAt의 변화를 확실히 확인
        Thread.sleep(10)
        val foundEntity = entityManager.find(TestEntity::class.java, entityId)
        foundEntity.name = "after"
        entityManager.flush()

        // then
        val updatedEntity = entityManager.find(TestEntity::class.java, entityId)
        assertThat(updatedEntity.updatedAt).isAfter(initialUpdatedAt)
    }
}

@Entity
class TestEntity(
    var name: String? = null
) : BaseEntity() 