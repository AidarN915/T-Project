package Tproject.service;

import Tproject.model.Board;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BoardService {
    public Board getById(Long boardId, Authentication auth);
    public List<Board> getByProjectId(Long projectId,Authentication auth);
    public Board create(Long projectId,Authentication auth,String title);
    public Board update(Long boardId,Authentication auth, String title);
    public String delete(Long boardId, Authentication auth);
}
