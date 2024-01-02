package com.niit.kanbanService.repository;

import com.niit.kanbanService.domain.KanbanBoard;
import com.niit.kanbanService.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KanbanRepository extends MongoRepository<User,String> {

    @Query("{'kanbanBoardList.boardId':{$in:[?0]}}")
    Optional<KanbanBoard> findByBoardId(int boardId);
}
