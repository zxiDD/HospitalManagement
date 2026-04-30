package com.cg.WebTest;

import com.cg.controller.BlockController;
import com.cg.dto.BlockDTO;
import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.BlockService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class BlockControllerTest {

    @Mock
    private BlockService blockService;

    @InjectMocks
    private BlockController controller;

    private Block block;
    private BlockId blockId;

    @BeforeEach
    void setUp() {
        blockId = new BlockId(1, 101);

        block = new Block();
        block.setId(blockId);
    }

    // ✅ getAllBlocks
    @Test
    void testGetAllBlocks() {
        Mockito.when(blockService.getAllBlocks())
                .thenReturn(List.of(block));

        ResponseEntity<List<BlockDTO>> response = controller.getAllBlocks();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().size());

        Mockito.verify(blockService).getAllBlocks();
    }

    // ✅ getBlockById SUCCESS
    @Test
    void testGetBlockById_Success() {
        Mockito.when(blockService.getBlockById(blockId))
                .thenReturn(Optional.of(block));

        ResponseEntity<BlockDTO> response =
                controller.getBlockById(1, 101);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().getBlockFloor());
        Assertions.assertEquals(101, response.getBody().getBlockCode());

        Mockito.verify(blockService).getBlockById(blockId);
    }

    // ❌ getBlockById NOT FOUND
    @Test
    void testGetBlockById_NotFound() {
        Mockito.when(blockService.getBlockById(blockId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> controller.getBlockById(1, 101));

        Mockito.verify(blockService).getBlockById(blockId);
    }

    // ✅ getBlockByFloorAndCode SUCCESS
    @Test
    void testGetBlockByFloorAndCode_Success() {
        Mockito.when(blockService.getBlockByFloorAndCode(1, 101))
                .thenReturn(Optional.of(block));

        ResponseEntity<BlockDTO> response =
                controller.getBlockByFloorAndCode(1, 101);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().getBlockFloor());

        Mockito.verify(blockService).getBlockByFloorAndCode(1, 101);
    }

    // ❌ getBlockByFloorAndCode NOT FOUND
    @Test
    void testGetBlockByFloorAndCode_NotFound() {
        Mockito.when(blockService.getBlockByFloorAndCode(1, 101))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> controller.getBlockByFloorAndCode(1, 101));

        Mockito.verify(blockService).getBlockByFloorAndCode(1, 101);
    }
}