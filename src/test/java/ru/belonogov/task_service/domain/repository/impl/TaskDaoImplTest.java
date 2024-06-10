package ru.belonogov.task_service.domain.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.PostgresTestContainer;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.DatabaseInterectionException;
import ru.belonogov.task_service.domain.exception.UpdateException;
import ru.belonogov.task_service.domain.repository.TaskDao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskDaoImplTest {

    private static TaskDao taskDao;
    private static PostgresTestContainer postgresTestContainer;

    @BeforeAll
    static void init(){
        postgresTestContainer = PostgresTestContainer.getInstance();
        taskDao = new TaskDaoImpl();
    }


    @Test
    void testSave_shouldSveAndReturnTask() {
        Task task = new Task();
        task.setName("Название нового задания");
        task.setDescription("описание нового задания");
        task.setRating(5);
        task.setTaskStatus(TaskStatus.SEARCH_FOR_EMPLOYEES);

        Task result = taskDao.save(task);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "Название нового задания");
    }


    @Test
    void testFindById_shouldReturnOptionalOfTask_whenTaskExist() {
        assertTrue(taskDao.findById(3L).isPresent());
    }

    @Test
    void testFindById_shouldReturnOptionalOfEmpty_whenTaskIsNotExist() {
        assertFalse(taskDao.findById(300L).isPresent());
    }

    @Test
    void testFindAllByEmployee_shouldReturnListTask_whenEmployeeExist() {
        assertThat(taskDao.findAllByEmployee(1L)).isNotEmpty();
    }

    @Test
    void testFindAllByEmployee_shouldReturnEmptyList_whenEmployeeIsNotExist() {
        assertThat(taskDao.findAllByEmployee(100L)).isEmpty();
    }

    @Test
    void testUpdate_shouldReturnUpdateTask_whenTestExist() {
        Task task = new Task();
        task.setId(2L);
        task.setName("Новое название задания");
        task.setDescription("Новое описание задания");
        task.setRating(5);
        task.setTaskStatus(TaskStatus.SEARCH_FOR_EMPLOYEES);

        Task result = taskDao.update(task);
        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "Новое название задания");
    }

    @Test
    void testUpdate_shouldReturnUpdateException_whenTestisNotExist() {
        Task task = new Task();
        task.setId(200L);
        task.setName("Название нового задания");
        task.setDescription("описание нового задания");
        task.setRating(5);
        task.setTaskStatus(TaskStatus.SEARCH_FOR_EMPLOYEES);

        assertThrows(UpdateException.class, () -> taskDao.update(task));
    }

    @Test
    void testAddNewEmployee_shouldReturnTrue_whenEmployeeAndTaskExist() {
        assertThat(taskDao.addNewEmployeeToTask(3L, 3L)).isTrue();
    }

    @Test
    void testAddNewEmployee_shouldReturnFalse_whenEmployeeOrTaskIsNotExist() {
        assertThat(taskDao.addNewEmployeeToTask(30L, 30L)).isFalse();
    }

    @Test
    void testDelete_shouldReturnTrue_whenTaskExist() {
        assertThat(taskDao.delete(5L)).isTrue();
    }

    @Test
    void testDelete_shouldReturnFalse_whenTaskIsNotExist() {
        assertThat(taskDao.delete(50L)).isFalse();
    }

    @Test
    void testDelete_shouldReturnDatabaseInterectionException_whenTaskHasARelationship() {
        assertThrows(DatabaseInterectionException.class, () -> taskDao.delete(1L));
    }
}