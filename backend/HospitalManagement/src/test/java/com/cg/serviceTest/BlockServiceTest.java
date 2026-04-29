package com.cg.serviceTest;

import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.repo.BlockRepository;
import com.cg.service.BlockServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BlockRepository repo;

    @InjectMocks
    private BlockServiceImpl service;

    private Block block;
    private BlockId blockId;

    @BeforeEach
    void setUp() {
        blockId = new BlockId(1, 101);

        block = new Block();
        block.setId(blockId);  
    }

    @Test
    void testGetAllBlocks() {
        when(repo.findAll()).thenReturn(List.of(block));

        List<Block> result = service.getAllBlocks();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testGetBlockById_Success() {
        when(repo.findById(blockId)).thenReturn(Optional.of(block));

        Optional<Block> result = service.getBlockById(blockId);

        assertTrue(result.isPresent());
        assertEquals(blockId, result.get().getId());
        verify(repo).findById(blockId);
    }

    @Test
    void testGetBlockById_NotFound() {
        when(repo.findById(blockId)).thenReturn(Optional.empty());

        Optional<Block> result = service.getBlockById(blockId);

        assertFalse(result.isPresent());
        verify(repo).findById(blockId);
    }

    @Test
    void testGetBlockByFloorAndCode_Success() {
        when(repo.findById(any(BlockId.class)))
                .thenReturn(Optional.of(block));

        Optional<Block> result = service.getBlockByFloorAndCode(1, 101);

        assertTrue(result.isPresent());
        verify(repo).findById(any(BlockId.class));
    }

    @Test
    void testGetBlockByFloorAndCode_NotFound() {
        when(repo.findById(any(BlockId.class)))
                .thenReturn(Optional.empty());

        Optional<Block> result = service.getBlockByFloorAndCode(1, 101);

        assertFalse(result.isPresent());
        verify(repo).findById(any(BlockId.class));
    }
}