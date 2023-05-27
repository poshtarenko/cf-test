package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;

import java.util.List;

public interface TaskService {

    ViewTaskDTO find(long id);

    List<ViewTaskDTO> findByTest(long testId);

    ViewTaskDTO save(SaveTaskDTO taskDTO);

    ViewTaskDTO update(UpdateTaskDTO taskDTO);

    void delete(long id);


    void checkAccess(long taskId, long respondentId);
}
