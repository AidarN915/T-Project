package Tproject.service;

import Tproject.dto.BoardUpdateDto;
import Tproject.model.Board;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BoardService {
    public Board getById(Long boardId, HttpServletRequest request);
    public List<Board> getByProjectId(Long projectId,HttpServletRequest request);
    public Board create(Long projectId,HttpServletRequest request,String title);
    public Board update(Long boardId,HttpServletRequest request, BoardUpdateDto updateDto);
    public String delete(Long boardId, HttpServletRequest request);
}
