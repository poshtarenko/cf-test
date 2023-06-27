package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.TaskMapper;
import com.poshtarenko.codeforge.dto.request.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.TaskRepository;
import com.poshtarenko.codeforge.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional(readOnly = true)
    public ViewTaskDTO find(long id) {
        return taskMapper.toDto(findById(id));
    }

    @Override
    public ViewTaskDTO save(SaveTaskDTO taskDTO) {
        Task task = taskRepository.save(taskMapper.toEntity(taskDTO));
        return taskMapper.toDto(task);
    }

    @Override
    public ViewTaskDTO update(UpdateTaskDTO taskDTO) {
        taskRepository.findById(taskDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(taskDTO.id())));

        Task task = taskRepository.save(taskMapper.toEntity(taskDTO));
        return taskMapper.toDto(task);
    }

    @Override
    public void delete(long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkAccess(long taskId, long authorId) {
        if (taskRepository.findById(taskId).isEmpty()) {
            throw new EntityNotFoundException(Task.class, "Task with id %d not found".formatted(taskId));
        }
        if (!taskRepository.existsByIdAndTestAuthorId(taskId, authorId)) {
            throw new EntityAccessDeniedException(Task.class, taskId, authorId);
        }
    }

    private Task findById(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Task.class, "Task with id " + taskId + " not found")
                );
    }
}
