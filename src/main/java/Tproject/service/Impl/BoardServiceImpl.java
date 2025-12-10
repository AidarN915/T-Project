package Tproject.service.Impl;

import Tproject.dto.BoardUpdateDto;
import Tproject.enums.OperationType;
import Tproject.model.Board;
import Tproject.model.Project;
import Tproject.model.TaskList;
import Tproject.model.User;
import Tproject.repository.BoardRepository;
import Tproject.repository.ProjectRepository;
import Tproject.repository.TaskListRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.BoardService;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserUtil userUtil;
    private final ProjectRepository projectRepository;
    private final TaskListRepository taskListRepository;
    private final CustomPermissionEvaluator permissionEvaluator;


    @Override
    public Board getById(Long id, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth, Target.board(id, OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"));

    }

    @Override
    public List<Board> getByProjectId(Long projectId,Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.project(projectId,OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        return project.getBoards();
    }

    @Override
    public Board create(Long projectId,Authentication auth, String title) {
        if(!permissionEvaluator.hasAccess(auth,Target.project(projectId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден")) ;

        Board board = new Board();
        board.setTitle(title);
        board.setProject(project);
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board update(Long boardId,Authentication auth, BoardUpdateDto updateDto) {
        if(!permissionEvaluator.hasAccess(auth,Target.board(boardId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"));

        board.setTitle(updateDto.getTitle());
        List<TaskList> taskLists = updateDto.getTaskLists().stream()
                        .map(dto -> taskListRepository.findById(dto.getId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список задач не найден")))
                                .toList();

        board.setTaskLists(taskLists);
        boardRepository.save(board);
        return board;
    }

    @Override
    public String delete(Long id, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.board(id,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"));

        board.markAsDeleted();
        boardRepository.save(board);
        return "Удалено";
    }
}
