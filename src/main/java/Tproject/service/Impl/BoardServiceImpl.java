package Tproject.service.Impl;

import Tproject.dto.BoardUpdateDto;
import Tproject.model.Board;
import Tproject.model.Project;
import Tproject.model.TaskList;
import Tproject.model.User;
import Tproject.repository.BoardRepository;
import Tproject.repository.ProjectRepository;
import Tproject.repository.TaskListRepository;
import Tproject.service.BoardService;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Override
    public Board getById(Long id, HttpServletRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"));
        User user = userUtil.getUserByRequest(request);
        if(!board.getProject().getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return board;
    }

    @Override
    public List<Board> getByProjectId(Long projectId,HttpServletRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        User user = userUtil.getUserByRequest(request);
        if(!project.getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return project.getBoards();
    }

    @Override
    public Board create(Long projectId,HttpServletRequest request, String title) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден")) ;
        User user = userUtil.getUserByRequest(request);
        if(!project.getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Board board = new Board();
        board.setTitle(title);
        board.setProject(project);
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board update(Long boardId,HttpServletRequest request, BoardUpdateDto updateDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"));
        User user = userUtil.getUserByRequest(request);
        if(!board.getProject().getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
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
    public String delete(Long id, HttpServletRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"));
        User user = userUtil.getUserByRequest(request);
        if(!board.getProject().getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        boardRepository.delete(board);
        return "Удалено";
    }
}
