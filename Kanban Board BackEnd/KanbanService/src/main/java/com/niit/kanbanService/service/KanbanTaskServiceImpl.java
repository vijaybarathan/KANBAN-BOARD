package com.niit.kanbanService.service;

import com.niit.kanbanService.domain.KanbanBoard;
import com.niit.kanbanService.domain.KanbanStage;
import com.niit.kanbanService.domain.KanbanTask;
import com.niit.kanbanService.domain.User;
import com.niit.kanbanService.exception.*;
import com.niit.kanbanService.repository.KanbanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KanbanTaskServiceImpl implements KanbanTaskService {
    private KanbanRepository kanbanRepository;

    @Autowired
    public KanbanTaskServiceImpl(KanbanRepository kanbanRepository){
        this.kanbanRepository=kanbanRepository;
    }

    @Override
    public KanbanTask getTaskByTaskName(String email, int boardId, int stageId, int taskId)
            throws UserNotFoundException, KanbanTaskNotFoundException {
        User user = kanbanRepository.findById(email).get();

        if (user == null) {
            throw new UserNotFoundException();
        }

        for (KanbanBoard board : user.getKanbanBoardList()) {
            if (boardId==(board.getBoardId())) {
                for (KanbanStage stage : board.getKanbanStageList()) {
                    if (stageId==(stage.getStageId())) {
                        for (KanbanTask task : stage.getKanbanTaskList()) {
                            if (taskId==(task.getTaskId())) {
                                return task;
                            }
                        }
                    }
                }
            }
        }

        throw new KanbanTaskNotFoundException();
    }

    @Override
    public User saveTaskToStage(String email, int boardId, int stageId, KanbanTask task)
            throws UserNotFoundException, KanbanStageNotFoundException, KanbanTaskAlreadyExistsException, MoreThanTwoTasksException, KanbanBoardNotFoundException {
        int count = 0;
        User user = kanbanRepository.findById(email).orElseThrow(UserNotFoundException::new);

        // Find the specific board
        KanbanBoard targetBoard = null;
        for (KanbanBoard board : user.getKanbanBoardList()) {
            if (board.getBoardId() == boardId) {
                targetBoard = board;
                break;
            }
        }

        if (targetBoard == null) {
            throw new KanbanBoardNotFoundException();
        }

        // Find the specific stage within the board
        KanbanStage targetStage = null;
        for (KanbanStage stage : targetBoard.getKanbanStageList()) {
            if (stage.getStageId() == stageId) {
                targetStage = stage;
                break;
            }
        }

        if (targetStage == null) {
            throw new KanbanStageNotFoundException();
        }

        // Check if the task already exists in the stage
        List<KanbanTask> taskList = targetStage.getKanbanTaskList();
        if (taskList != null) {
            for (KanbanTask kanbanTask : taskList) {
                if (kanbanTask.getTaskId() == task.getTaskId()) {
                    throw new KanbanTaskAlreadyExistsException();
                }
            }
        } else {
            taskList = new ArrayList<>();
        }

        // Check if the stage is "In Progress" and limit tasks assigned to the same person
        if (targetStage.getStageName().equals("In Progress")) {
            for (KanbanTask t : taskList) {
                if (t.getTaskAssignee().equals(task.getTaskAssignee())) {
                    count++;
                }
            }
            if (count > 2) {
                throw new MoreThanTwoTasksException();
            }
        }

        // Add the task to the stage
        taskList.add(task);
        targetStage.setKanbanTaskList(taskList);

        // Add the task to all board members' stages of the same board
        List<String> boardMembers = targetBoard.getBoardMembers();
        if (boardMembers != null && !boardMembers.isEmpty()) {
            for (String memberEmail : boardMembers) {
                if (!memberEmail.equals(email)) {
                    User boardMember = kanbanRepository.findById(memberEmail).orElseThrow(UserNotFoundException::new);
                    for (KanbanBoard board : boardMember.getKanbanBoardList()) {
                        if (board.getBoardId() == boardId) {
                            for (KanbanStage stage : board.getKanbanStageList()) {
                                if (stage.getStageId() == stageId) {
                                    List<KanbanTask> memberTaskList = stage.getKanbanTaskList();
                                    if (memberTaskList == null) {
                                        memberTaskList = new ArrayList<>();
                                    }
                                    memberTaskList.add(task);
                                    stage.setKanbanTaskList(memberTaskList);
                                }
                            }
                            kanbanRepository.save(boardMember);
                            break;
                        }
                    }
                }
            }
        }

        return kanbanRepository.save(user);
    }

    @Override
    public User deleteTaskFromStage(String email, int boardId, int stageId, int taskId)
            throws UserNotFoundException, KanbanTaskNotFoundException, KanbanStageNotFoundException, KanbanBoardNotFoundException {
        User user = kanbanRepository.findById(email).orElseThrow(UserNotFoundException::new);

        // Find the specific board
        KanbanBoard targetBoard = null;
        for (KanbanBoard board : user.getKanbanBoardList()) {
            if (board.getBoardId() == boardId) {
                targetBoard = board;
                break;
            }
        }

        if (targetBoard == null) {
            throw new KanbanBoardNotFoundException();
        }

        // Find the specific stage within the board
        KanbanStage targetStage = null;
        for (KanbanStage stage : targetBoard.getKanbanStageList()) {
            if (stage.getStageId() == stageId) {
                targetStage = stage;
                break;
            }
        }

        if (targetStage == null) {
            throw new KanbanStageNotFoundException();
        }

        // Find and remove the task from the stage
        List<KanbanTask> taskList = targetStage.getKanbanTaskList();
        if (taskList != null) {
            KanbanTask taskToDelete = null;
            for (KanbanTask task : taskList) {
                if (task.getTaskId() == taskId) {
                    taskToDelete = task;
                    break;
                }
            }

            if (taskToDelete != null) {
                taskList.remove(taskToDelete);
                targetStage.setKanbanTaskList(taskList);
            } else {
                throw new KanbanTaskNotFoundException();
            }
        } else {
            throw new KanbanTaskNotFoundException();
        }

        // Remove the task from all board members' stages of the same board
        List<String> boardMembers = targetBoard.getBoardMembers();
        if (boardMembers != null && !boardMembers.isEmpty()) {
            for (String memberEmail : boardMembers) {
                if (!memberEmail.equals(email)) {
                    User boardMember = kanbanRepository.findById(memberEmail).orElseThrow(UserNotFoundException::new);
                    for (KanbanBoard board : boardMember.getKanbanBoardList()) {
                        if (board.getBoardId() == boardId) {
                            for (KanbanStage stage : board.getKanbanStageList()) {
                                if (stage.getStageId() == stageId) {
                                    List<KanbanTask> memberTaskList = stage.getKanbanTaskList();
                                    if (memberTaskList != null) {
                                        KanbanTask memberTaskToDelete = null;
                                        for (KanbanTask task : memberTaskList) {
                                            if (task.getTaskId() == taskId) {
                                                memberTaskToDelete = task;
                                                break;
                                            }
                                        }

                                        if (memberTaskToDelete != null) {
                                            memberTaskList.remove(memberTaskToDelete);
                                            stage.setKanbanTaskList(memberTaskList);
                                        }
                                    }
                                }
                            }
                            kanbanRepository.save(boardMember);
                            break;
                        }
                    }
                }
            }
        }

        return kanbanRepository.save(user);
    }


    @Override
    public User UpdateTask(String email, int boardId, int stageId, int taskId, KanbanTask task)
            throws UserNotFoundException, KanbanTaskNotFoundException, KanbanStageNotFoundException, KanbanBoardNotFoundException {
        User user = kanbanRepository.findById(email).orElseThrow(UserNotFoundException::new);

        // Find the specific board
        KanbanBoard targetBoard = null;
        for (KanbanBoard board : user.getKanbanBoardList()) {
            if (board.getBoardId() == boardId) {
                targetBoard = board;
                break;
            }
        }

        if (targetBoard == null) {
            throw new KanbanBoardNotFoundException();
        }

        // Find the specific stage within the board
        KanbanStage targetStage = null;
        for (KanbanStage stage : targetBoard.getKanbanStageList()) {
            if (stage.getStageId() == stageId) {
                targetStage = stage;
                break;
            }
        }

        if (targetStage == null) {
            throw new KanbanStageNotFoundException();
        }

        // Find and update the task within the stage
        List<KanbanTask> taskList = targetStage.getKanbanTaskList();
        if (taskList != null) {
            for (KanbanTask existingTask : taskList) {
                if (existingTask.getTaskId() == taskId) {
                    existingTask.setTaskTitle(task.getTaskTitle());
                    existingTask.setTaskAssignee(task.getTaskAssignee());
                    existingTask.setTaskDeadline(task.getTaskDeadline());
                    existingTask.setTaskDescription(task.getTaskDescription());
                    existingTask.setStartDate(task.getStartDate());
                    existingTask.setTaskPriority(task.getTaskPriority());
                    existingTask.setTaskType(task.getTaskType());
                }
            }
        } else {
            throw new KanbanTaskNotFoundException();
        }

        // Update the task across all board members' stages of the same board
        List<String> boardMembers = targetBoard.getBoardMembers();
        if (boardMembers != null && !boardMembers.isEmpty()) {
            for (String memberEmail : boardMembers) {
                if (!memberEmail.equals(email)) {
                    User boardMember = kanbanRepository.findById(memberEmail).orElseThrow(UserNotFoundException::new);
                    for (KanbanBoard board : boardMember.getKanbanBoardList()) {
                        if (board.getBoardId() == boardId) {
                            for (KanbanStage stage : board.getKanbanStageList()) {
                                if (stage.getStageId() == stageId) {
                                    List<KanbanTask> memberTaskList = stage.getKanbanTaskList();
                                    if (memberTaskList != null) {
                                        for (KanbanTask memberTask : memberTaskList) {
                                            if (memberTask.getTaskId() == taskId) {
                                                memberTask.setTaskTitle(task.getTaskTitle());
                                                memberTask.setTaskAssignee(task.getTaskAssignee());
                                                memberTask.setTaskDeadline(task.getTaskDeadline());
                                                memberTask.setTaskDescription(task.getTaskDescription());
                                                memberTask.setStartDate(task.getStartDate());
                                                memberTask.setTaskPriority(task.getTaskPriority());
                                                memberTask.setTaskType(task.getTaskType());
                                            }
                                        }
                                    }
                                }
                            }
                            kanbanRepository.save(boardMember);
                            break;
                        }
                    }
                }
            }
        }

        return kanbanRepository.save(user);
    }



    @Override
    public List<KanbanTask> getAllTasks(String email, int stageId) {
        User user = kanbanRepository.findById(email).get();
        List<KanbanBoard> boardsList = user.getKanbanBoardList();
        List<KanbanTask> tasklist = new ArrayList<>();
        for (KanbanBoard b : boardsList) {
            List<KanbanStage> stageList = b.getKanbanStageList();
            for (KanbanStage c : stageList) {
                if (c.getStageId()==(stageId)) {
                    tasklist = c.getKanbanTaskList();
                    break;
                }
            }
        }
        return tasklist;
    }

    @Override
    public User updatingTaskByDragAndDrop(String email, int boardId, int stageId, KanbanTask task) throws UserNotFoundException,
            MoreThanTwoTasksException, KanbanStageNotFoundException {
        User user = kanbanRepository.findById(email).orElseThrow(UserNotFoundException::new);

        KanbanStage sourceStage = null;
        KanbanStage destinationStage = null;

        // Find the source and destination columns
        for (KanbanBoard b : user.getKanbanBoardList()) {
            if (b.getBoardId() == boardId) {
                for (KanbanStage c : b.getKanbanStageList()) {
                    if (c.getStageId() == stageId) {
                        destinationStage = c;
                    }
                    List<KanbanTask> taskList = c.getKanbanTaskList();
                    if (taskList != null) {
                        for (KanbanTask t : taskList) {
                            if (t.getTaskId() == task.getTaskId()) {
                                sourceStage = c;
                                break;
                            }
                        }
                    }
                    if (sourceStage != null && destinationStage != null) {
                        break;
                    }
                }
            }
        }

        if (destinationStage == null || sourceStage == null) {
            throw new KanbanStageNotFoundException();
        }

        // Check if destination stage is "In Progress" and already has more than 1 task assigned to the same person
        if (destinationStage.getStageName().equals("In Progress")) {
            long countAssignee = destinationStage.getKanbanTaskList()
                    .stream()
                    .filter(t -> t.getTaskAssignee().equals(task.getTaskAssignee()))
                    .count();

            if (countAssignee >= 2) {
                throw new MoreThanTwoTasksException();
            }
        }

        List<KanbanTask> sourceTasks = sourceStage.getKanbanTaskList();
        sourceTasks.removeIf(t -> t.getTaskId() == task.getTaskId());

        List<KanbanTask> destinationTasks = destinationStage.getKanbanTaskList();
        if (destinationTasks == null) {
            destinationStage.setKanbanTaskList(Arrays.asList(task));
        } else {
            destinationTasks.add(task);
        }
        return kanbanRepository.save(user);
    }
}
