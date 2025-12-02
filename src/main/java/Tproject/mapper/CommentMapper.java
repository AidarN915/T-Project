package Tproject.mapper;

import Tproject.dto.CommentDto;
import Tproject.model.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public abstract class CommentMapper {
    public abstract CommentDto toDto(Comment comment);
    public abstract List<CommentDto> toListDto(List<Comment> comments);
}
