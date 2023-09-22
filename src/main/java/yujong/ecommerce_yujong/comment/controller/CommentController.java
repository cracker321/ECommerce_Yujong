package yujong.ecommerce_yujong.comment.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yujong.ecommerce_yujong.comment.dto.CommentPatchDto;
import yujong.ecommerce_yujong.comment.dto.CommentPostDto;
import yujong.ecommerce_yujong.comment.entity.Comment;
import yujong.ecommerce_yujong.comment.mapper.CommentMapper;
import yujong.ecommerce_yujong.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("comments")
@AllArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseEntity postComment(@Valid @RequestBody CommentPostDto commentPostDto) {
        Comment comment = commentService.createComment(
                commentMapper.commentPostDtoToComment(commentPostDto), commentPostDto.getMemberId());

        return new ResponseEntity<>((commentMapper.commentToCommentResponseDto(comment)), HttpStatus.CREATED);
    }



    @GetMapping("/{board-Id}")
    public ResponseEntity getComment(@PathVariable("board-Id") @Positive Long boardId,
                                     @Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {

        Page<Comment> commentPage = commentService.findCommentByBoard(boardId,page - 1, size);
        List<Comment> commentList = commentPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(commentMapper.commentToCommentResponseDtos(commentList),
                        commentPage), HttpStatus.OK);
    }

    @PatchMapping("/{comment-Id}")
    public ResponseEntity patchComment(@PathVariable("comment-Id") @Positive Long commentId,
                                       @Valid @RequestBody CommentPatchDto commentPatchDto) {
        commentPatchDto.setCommentId(commentId);
        Comment comment = commentService.updateComment(
                commentMapper.commentPatchDtoToComment(commentPatchDto), commentPatchDto.getMemberId());

        return new ResponseEntity<>(commentMapper.commentToCommentResponseDto(comment), HttpStatus.OK);
    }

    @DeleteMapping("/{comment-Id}")
    public ResponseEntity deleteComment(@PathVariable("comment-Id") @Positive Long commentId,
                                        @Positive @RequestParam Long memberId) {
        commentService.deleteComment(commentId, memberId);
        String message = "Success!";

        return ResponseEntity.ok(message);
    }
}