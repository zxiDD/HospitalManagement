package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.repo.BlockRepository;

@Service
public class BlockServiceImpl implements BlockService {
	// Get all blocks
	@Autowired
	private BlockRepository blockRepository;

	@Override
	public List<Block> getAllBlocks() {
		return blockRepository.findAll();
	}

	@Override
	public Optional<Block> getBlockById(BlockId id) {
		return blockRepository.findById(id);
	}

	@Override
	public Optional<Block> getBlockByFloorAndCode(Integer blockFloor, Integer blockCode) {
		BlockId blockId = new BlockId(blockFloor, blockCode);
		return blockRepository.findById(blockId);
	}
}
