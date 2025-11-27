package Tproject.mapper;

import Tproject.dto.BoardDto;
import Tproject.model.Board;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {TaskListMapper.class})
public abstract class BoardMapper {
    public abstract BoardDto toDto(Board board);
    public abstract List<BoardDto> toListDto(List<Board> boards);
}
