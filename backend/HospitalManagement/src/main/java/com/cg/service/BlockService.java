package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.repo.BlockRepository;

public interface BlockService {
	public List<Block> getAllBlocks();

	// Get block by composite ID
	public Optional<Block> getBlockById(BlockId id);

	// Get block by floor and code
	public Optional<Block> getBlockByFloorAndCode(Integer blockFloor, Integer blockCode);

}
