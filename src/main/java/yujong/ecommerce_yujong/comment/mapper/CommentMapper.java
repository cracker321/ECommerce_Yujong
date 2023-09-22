package yujong.ecommerce_yujong.comment.mapper;

import org.mapstruct.Mapper;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.comment.dto.CommentPatchDto;
import yujong.ecommerce_yujong.comment.dto.CommentPostDto;
import yujong.ecommerce_yujong.comment.dto.CommentResponseDto;
import yujong.ecommerce_yujong.comment.entity.Comment;
import yujong.ecommerce_yujong.member.entity.Member;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    default Comment commentPostDtoToComment(CommentPostDto commentPostDto) {
        if ( commentPostDto == null ) {
            return null;
        }
        Member member = new Member();
        member.setMemberId(commentPostDto.getMemberId());

        Board board = new Board();
        board.setBoardId(commentPostDto.getBoardId());

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setBoard(board);
        comment.setContext(commentPostDto.getContext());

        return comment;
    }





    default Comment commentPatchDtoToComment(CommentPatchDto commentPatchDto) {
        if ( commentPatchDto == null ) {
            return null;
        }

        Member member = new Member();
        member.setMemberId(commentPatchDto.getMemberId());

        Board board = new Board();
        board.setBoardId(commentPatchDto.getBoardId());

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setBoard(board);
        comment.setCommentId( commentPatchDto.getCommentId());
        comment.setContext( commentPatchDto.getContext());

        return comment;
    }





    default CommentResponseDto commentToCommentResponseDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        Member member = new Member();
        member.setMemberId(comment.getMember().getMemberId());

        comment.setCommentId(comment.getCommentId());

        Board board = new Board();
        board.setBoardId(comment.getBoard().getBoardId());

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setMemberId(member.getMemberId());
        commentResponseDto.setCommentId(comment.getCommentId());
        commentResponseDto.setBoardId(board.getBoardId());
        commentResponseDto.setContext(comment.getContext());
        commentResponseDto.setName(comment.getMember().getName());
        commentResponseDto.setCreatedAt(comment.getCreatedAt());
        commentResponseDto.setModifiedAt(comment.getModifiedAt());

        return commentResponseDto;
    }

    List<CommentResponseDto> commentToCommentResponseDtos(List<Comment> comments);
}