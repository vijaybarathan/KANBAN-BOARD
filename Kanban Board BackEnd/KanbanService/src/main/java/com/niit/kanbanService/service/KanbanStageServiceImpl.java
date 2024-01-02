package com.niit.kanbanService.service;

import com.niit.kanbanService.domain.KanbanBoard;
import com.niit.kanbanService.domain.KanbanStage;
import com.niit.kanbanService.domain.User;
import com.niit.kanbanService.exception.KanbanBoardNotFoundException;
import com.niit.kanbanService.exception.KanbanStageAlreadyExistsException;
import com.niit.kanbanService.exception.KanbanStageNotFoundException;
import com.niit.kanbanService.exception.UserNotFoundException;
import com.niit.kanbanService.repository.KanbanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KanbanStageServiceImpl implements KanbanStageService{

    private KanbanRepository kanbanRepository;
    @Autowired
    public KanbanStageServiceImpl(KanbanRepository kanbanRepository) {
        this.kanbanRepository=kanbanRepository;
    }
    @Override
    public List<KanbanStage> getAllStagesFromBoard(String email, int boardId) throws UserNotFoundException, KanbanBoardNotFoundException {
        if(kanbanRepository.findById(email).isEmpty()){
            throw new UserNotFoundException();
        }
        User kanbandetail=kanbanRepository.findById(email).get();
        List<KanbanStage> stageList=null;
        List<KanbanBoard> boardlist=kanbandetail.getKanbanBoardList();
        for(KanbanBoard boards:boardlist){
            if(boards.getBoardId()==boardId){
                stageList=boards.getKanbanStageList();
            }
        }
        if(stageList!=null) {
            return stageList;
        }
        else{
            throw new KanbanBoardNotFoundException();
        }
    }

    @Override
    public User saveStagesToBoards(String email, int boardId, KanbanStage stage) throws UserNotFoundException, KanbanBoardNotFoundException, KanbanStageAlreadyExistsException {
        if (kanbanRepository.findById(email).isEmpty()) {
            throw new UserNotFoundException();
        }

        User kanbanDetails = kanbanRepository.findById(email).get();
        List<KanbanBoard> boardList = kanbanDetails.getKanbanBoardList();

        KanbanBoard targetBoard = null;

        // Find the target board by name
        for (KanbanBoard bo : boardList) {
            if (bo.getBoardId()==boardId) {
                targetBoard = bo;
                break;
            }
        }

        if (targetBoard == null) {
            throw new KanbanBoardNotFoundException();
        }

        // Update the existing board by adding the column
        List<KanbanStage> stageList = targetBoard.getKanbanStageList();
        if(stageList==null)
        {
            targetBoard.setKanbanStageList(Arrays.asList(stage));
        }
        else {
            for (KanbanStage kanbanStage:stageList) {
                if(kanbanStage.getStageId()==stage.getStageId())
                {
                    throw new KanbanStageAlreadyExistsException();
                }
            }
                stageList.add(stage);
                targetBoard.setKanbanStageList(stageList);

        }
        kanbanDetails.setKanbanBoardList(boardList);
        // Save the updated user details


        // Updating in team members
        for (KanbanBoard B : boardList) {
            if (B.getBoardId()!=boardId) {
                continue;
            }

            List<String> team_memberlist = B.getBoardMembers();
            for (String m : team_memberlist) {
                if (m.equals(email)) {
                    continue;
                } else {
                    User teamuser = kanbanRepository.findById(m).get();
                    for (KanbanBoard b : teamuser.getKanbanBoardList()) {
                        if (b.getBoardId()==boardId) {
                            List<KanbanStage> clist = b.getKanbanStageList();
                            if (clist==null) {
                                b.setKanbanStageList(Arrays.asList(stage));
                            } else if (!clist.contains(stage)) {
                                clist.add(stage);
                                b.setKanbanStageList(clist);
                            }
                            kanbanRepository.save(teamuser);
                            break;
                        }
                    }
                }
            }
        }
        return kanbanRepository.save(kanbanDetails);

    }

    @Override
    public boolean deleteStage(String email, int boardId, int stageId) throws UserNotFoundException, KanbanBoardNotFoundException, KanbanStageNotFoundException {
        boolean value = false;
        if (kanbanRepository.findById(email).isEmpty()) {
            throw new UserNotFoundException();
        }

        User kanbandetails = kanbanRepository.findById(email).get();
        List<KanbanBoard> boardlist = kanbandetails.getKanbanBoardList();
        for (KanbanBoard bo : boardlist) {
            if (bo.getBoardId()==boardId) {
                List<KanbanStage> stageList = bo.getKanbanStageList();
                KanbanStage deletingcolumn = null;
                for (KanbanStage col : stageList) {
                    if (col.getStageId()==stageId) {
                        deletingcolumn = col;
                        break;
                    }
                }
                if (deletingcolumn != null) {
                    stageList.remove(deletingcolumn);
                    bo.setKanbanStageList(stageList);
                    kanbanRepository.save(kanbandetails);
                    value = true;
                }
            }
        }
        if (!value) {
            throw new KanbanStageNotFoundException();
        }
        return value;
    }

    @Override
    public User updateStage(String email, int boardId, int stageId, KanbanStage stage) throws UserNotFoundException, KanbanBoardNotFoundException, KanbanStageNotFoundException {
        if(kanbanRepository.findById(email).isEmpty()){
            throw new UserNotFoundException();
        }
        User kanbandetails=kanbanRepository.findById(email).get();
        List<KanbanBoard>boardlist=kanbandetails.getKanbanBoardList();
        if(boardlist.isEmpty()){
            throw new KanbanBoardNotFoundException();
        }
        for(KanbanBoard bo:boardlist){
            if(bo.getBoardId()==boardId) {
                List<KanbanStage> stageList = bo.getKanbanStageList();
                if (stageList.isEmpty()) {
                    throw new KanbanStageNotFoundException();
                }
                for (KanbanStage col : stageList) {
                    if (col.getStageId() == stageId) {
                        col.setStageName(stage.getStageName());
                        col.setKanbanTaskList(stage.getKanbanTaskList());
                    }
                }
                bo.setKanbanStageList(stageList);
            }
        }
        kanbandetails.setKanbanBoardList(boardlist);
        return kanbanRepository.save(kanbandetails);
    }

    @Override
    public List<String> getAllStageNames(String email,int boardId)throws KanbanStageNotFoundException {
        User user= kanbanRepository.findById(email).get();
        List<KanbanBoard> boardlist=user.getKanbanBoardList();
        List<String> stageName=new ArrayList<>();
        for(KanbanBoard bo:boardlist){
            if(bo.getBoardId()==(boardId)){
                List<KanbanStage> stageList=bo.getKanbanStageList();
                for(KanbanStage co:stageList){
                    stageName.add(co.getStageName());
                }
                break;
            }
        }
        return stageName;
    }
}
