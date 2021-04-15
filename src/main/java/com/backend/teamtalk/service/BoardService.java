package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Board;
import com.backend.teamtalk.dto.BoardRequestDto;
import com.backend.teamtalk.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;


    //get one board
    public Board getOneBoard(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(IllegalArgumentException::new);
        return board;
    }

    //create board
    public void createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto);
        boardRepository.save(board);
    }

    //update board
    @Transactional
    public void updateBoard(Long board_id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(IllegalArgumentException::new);
        board.update(requestDto);
    }

    //delete board
    public void deleteBoard(Long board_id) {
        boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("There is no bulletin board.")
        );
        boardRepository.deleteById(board_id);
    }
}
