package com.niit.kanbanService.service;

import com.niit.kanbanService.domain.KanbanBoard;
import com.niit.kanbanService.domain.KanbanStage;
import com.niit.kanbanService.domain.Notification;
import com.niit.kanbanService.domain.User;
import com.niit.kanbanService.exception.*;
import com.niit.kanbanService.proxy.NotificationProxy;
import com.niit.kanbanService.proxy.UserProxy;
import com.niit.kanbanService.repository.KanbanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KanbanBoardServiceImpl implements KanbanBoardService {
    private KanbanRepository kanbanRepository;
    private UserProxy userProxy;

    private NotificationProxy notificationProxy;
    @Autowired
    private KanbanBoardServiceImpl(KanbanRepository kanbanRepository, UserProxy userProxy,NotificationProxy notificationProxy){
        this.kanbanRepository=kanbanRepository;
        this.userProxy=userProxy;
        this.notificationProxy=notificationProxy;
    }
    @Override
    public User registerUser(User user) throws UserAlreadyExistsException {

        if(kanbanRepository.findById(user.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException();
        }
        User savedUser = kanbanRepository.save(user);
        if(!(savedUser.getEmail().isEmpty())) {
            ResponseEntity r = userProxy.saveUser(user);
            System.out.println(r.getBody());
            Notification notification=new Notification();
            notification.setEmail(user.getEmail());
            notificationProxy.saveUser(notification);
        }
        return savedUser;
    }

    @Override
    public User getAUser(String email) throws Exception {
        return kanbanRepository.findById(email).get();
    }

    @Override
    public boolean deleteUser(String email) throws UserNotFoundException {
        List<User> list = kanbanRepository.findAll();

        for (User user : list) {
            if (user.getEmail().equals(email)) {
                kanbanRepository.delete(user);
                userProxy.deleteUser(user.getEmail());
                System.out.println("Deleted");
                return true;
            }
        }
        throw new UserNotFoundException();
    }

    @Override
    public User updateUser(String email,User user) throws UserNotFoundException{
        Optional<User> optionalUser = kanbanRepository.findById(email);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(user.getPassword());

            User savedUser = kanbanRepository.save(existingUser);
            userProxy.updateUser(email,user);

            return savedUser;
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public KanbanBoard saveUserKanbanBoardToBoardList(KanbanBoard kanbanBoard, String email) throws KanbanBoardAlreadyExistsException, UserNotFoundException {

        if(kanbanRepository.findById(email).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = kanbanRepository.findById(email).get();
        if(user.getKanbanBoardList() == null)
        {
            user.setKanbanBoardList(Arrays.asList(kanbanBoard));
        }
        else {
            List<KanbanBoard> kanbanBoards = user.getKanbanBoardList();
            if (kanbanBoards.contains(kanbanBoard)) {
                throw new KanbanBoardAlreadyExistsException();
            }
            kanbanBoards.add(kanbanBoard);
            user.setKanbanBoardList(kanbanBoards);
            List<String> members=kanbanBoard.getBoardMembers();
            if(members!=null){
                for (String s:members)
                {
                    Optional<User> user1=kanbanRepository.findById(s);
                    if(user1.isPresent())
                    {
                        User user2=user1.get();
                        List<KanbanBoard> kanbanBoardList=user2.getKanbanBoardList();
                        if(kanbanBoardList==null)
                        {
                            user2.setKanbanBoardList(Arrays.asList(kanbanBoard));
                        }
                        else {
                            kanbanBoardList.add(kanbanBoard);
                            user2.setKanbanBoardList(kanbanBoardList);
                        }
                        kanbanRepository.save(user2);
                    }
                }
            }

        }

        User user1= kanbanRepository.save(user);
        for (KanbanBoard board:user1.getKanbanBoardList()) {
            if(board.getBoardName().equals(kanbanBoard.getBoardName()) && board.getBoardDescription().equals(kanbanBoard.getBoardDescription()) && board.getBoardOwnerName().equals(kanbanBoard.getBoardOwnerName()))
            {
                return board;
            }
        }
        return null;
    }

    @Override
    public List<KanbanBoard> getAllKanbanBoardsFromBoardList(String email) throws Exception {

        if(kanbanRepository.findById(email).isEmpty())
        {
            throw new UserNotFoundException();
        }
        return kanbanRepository.findById(email).get().getKanbanBoardList();
    }

    @Override
    public KanbanBoard getAKanbanBoardFromBoardList(String email,int boardId) throws KanbanBoardNotFoundException{
        List<KanbanBoard> boardList=kanbanRepository.findById(email).get().getKanbanBoardList();
        for (KanbanBoard board:boardList)
        {
            if(board.getBoardId()==boardId)
            {
                return board;
            }
        }
        throw new KanbanBoardNotFoundException();
    }

    @Override
    public User deleteKanbanBoard(String email, int boardId) throws KanbanBoardNotFoundException, UserNotFoundException {
        boolean boardIdIsPresent = false;
        if (kanbanRepository.findById(email).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = kanbanRepository.findById(email).get();
        List<KanbanBoard> kanbanBoards = user.getKanbanBoardList();

        for (int i = 0; i < kanbanBoards.size(); i++) {
            KanbanBoard kanbanBoard = kanbanBoards.get(i);
            if (kanbanBoard.getBoardId() == boardId) {
                // Remove the board from the user's list
                kanbanBoards.remove(i);
                boardIdIsPresent = true;

                // Remove the board from the member's board lists
                List<String> boardMembers = kanbanBoard.getBoardMembers();
                if (boardMembers != null) {
                    for (String memberEmail : boardMembers) {
                        if (!memberEmail.equals(email)) { // Avoid deleting from the owner's list again
                            Optional<User> memberUserOptional = kanbanRepository.findById(memberEmail);
                            if (memberUserOptional.isPresent()) {
                                User memberUser = memberUserOptional.get();
                                List<KanbanBoard> memberBoards = memberUser.getKanbanBoardList();
                                memberBoards.removeIf(board -> board.getBoardId() == boardId);
                                memberUser.setKanbanBoardList(memberBoards);
                                kanbanRepository.save(memberUser);
                            }
                        }
                    }
                }
                break;
            }
        }

        if (!boardIdIsPresent) {
            throw new KanbanBoardNotFoundException();
        }

        user.setKanbanBoardList(kanbanBoards);
        return kanbanRepository.save(user);
    }


    @Override
    public User updateUserKanbanBoardListWithGivenKanbanBoard(String email, KanbanBoard kanbanBoard) throws UserNotFoundException, KanbanBoardNotFoundException {
        User user = kanbanRepository.findById(email).orElseThrow(UserNotFoundException::new);
        List<KanbanBoard> kanbanBoardList = user.getKanbanBoardList();

        boolean kanbanBoardFound = false;

        for (KanbanBoard userBoard : kanbanBoardList) {
            if (userBoard.getBoardId() == kanbanBoard.getBoardId()) {
                kanbanBoardFound = true;
                updateBoardDetails(userBoard, kanbanBoard); // Update details for the user's board
                updateBoardForMember(userBoard, kanbanBoard); // Update details for the board's members
                break;
            }
        }

        if (!kanbanBoardFound) {
            throw new KanbanBoardNotFoundException();
        }

        return kanbanRepository.save(user);
    }


    private void updateBoardDetails(KanbanBoard existingBoard, KanbanBoard newBoard) {
        existingBoard.setBoardName(newBoard.getBoardName());
        existingBoard.setBoardOwnerName(newBoard.getBoardOwnerName());
        existingBoard.setBoardDescription(newBoard.getBoardDescription());
        existingBoard.setBoardCreatedOn(newBoard.getBoardCreatedOn());
        existingBoard.setBoardMembers(newBoard.getBoardMembers());
        existingBoard.setKanbanStageList(newBoard.getKanbanStageList());
    }

    private void updateBoardForMember(KanbanBoard userBoard, KanbanBoard newBoard) {
        List<String> members = userBoard.getBoardMembers();
        if (members != null) {
            for (String memberEmail : members) {
                User memberUser = kanbanRepository.findById(memberEmail).orElse(null);
                if (memberUser != null) {
                    List<KanbanBoard> memberBoards = memberUser.getKanbanBoardList();
                    for (KanbanBoard memberBoard : memberBoards) {
                        if (memberBoard.getBoardId() == newBoard.getBoardId()) {
                            updateBoardDetails(memberBoard, newBoard);
                        }
                    }
                    kanbanRepository.save(memberUser);
                }
            }
        }
    }


    @Override
    public User addMemberToKanbanBoardByEmail(int boardId, String memberEmail, String userEmail) throws UserNotFoundException, KanbanBoardNotFoundException, TeamMemberAlreadyExistsException {
        Optional<User> user = kanbanRepository.findById(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        User userDetail = user.get();

        KanbanBoard board = findBoardByName(userDetail, boardId);
        if (board == null) {
            throw new KanbanBoardNotFoundException();
        }

        List<String> boardMembers = board.getBoardMembers();

        if (boardMembers.contains(memberEmail)) {
            throw new TeamMemberAlreadyExistsException();
        }

        boardMembers.add(memberEmail);

        if (!boardMembers.contains(userEmail)) {
            boardMembers.add(userEmail);
        }

        // Update the user's profile to include the board
        Optional<User> userkanbanOptional = kanbanRepository.findById(memberEmail);
        if (userkanbanOptional.isEmpty()) {
            System.out.println("user");
            throw new UserNotFoundException();
        }
        User userKanban = userkanbanOptional.get();
        List<KanbanBoard> kanbanBoardList=userKanban.getKanbanBoardList();
        if(kanbanBoardList==null)
        {
            userKanban.setKanbanBoardList(Arrays.asList(board));
        }
        else {
            kanbanBoardList.add(board);
        }

        for (String member : boardMembers) {
            if (!member.equals(memberEmail) && !member.equals(userEmail)) {
                Optional<User> existingMemberOptional = kanbanRepository.findById(member);
                User existingMember=null;
                if (existingMemberOptional.isPresent()) {
                    existingMember = existingMemberOptional.get();
                    List<KanbanBoard> blist=existingMember.getKanbanBoardList();
                    if(blist==null)
                    {
                        existingMember.setKanbanBoardList(Arrays.asList(board));
                    }
                    else {
                        Iterator<KanbanBoard> iterator = blist.iterator();
                        while (iterator.hasNext()) {
                            KanbanBoard b = iterator.next();
                            if (b.getBoardId() == (boardId)) {
                                iterator.remove();
                            }
                        }
                        existingMember.getKanbanBoardList().add(board);
                    }
                }
                kanbanRepository.save(existingMember);
            }
        }
        kanbanRepository.save(userKanban);
        return kanbanRepository.save(userDetail);
    }

    @Override
    public List<String> getAllKanbanBoardNames(String email) throws KanbanBoardNotFoundException {
        List<KanbanBoard> boardsList=kanbanRepository.findById(email).get().getKanbanBoardList();
        List<String> boardName=new ArrayList<>();
        for(KanbanBoard b:boardsList){
            boardName.add(b.getBoardName());
        }
        if(!(boardName.isEmpty()))
            return boardName;
        else
            throw new KanbanBoardNotFoundException();
    }

    private KanbanBoard findBoardByName(User user, int boardId)
    {
        List<KanbanBoard> boardList = user.getKanbanBoardList();
        for (KanbanBoard board : boardList)
        {
            if (board.getBoardId()==(boardId))
            {
                return board;
            }
        }
        return null;
    }
    @Override
    public List<User> getAllUsers() throws Exception{
        return kanbanRepository.findAll();
    }
}
