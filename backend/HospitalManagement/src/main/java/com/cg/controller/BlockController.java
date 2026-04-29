package com.cg.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.BlockDTO;
import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.BlockService;

@RestController
@RequestMapping("/blocks")
public class BlockController {

	@Autowired
	private BlockService blockService;

	private BlockDTO convertToDTO(Block block) {
		return new BlockDTO(block.getId().getBlockFloor(), block.getId().getBlockCode());
	}

	@GetMapping
	public ResponseEntity<List<BlockDTO>> getAllBlocks() {

		List<BlockDTO> blockDTOs = blockService.getAllBlocks().stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(blockDTOs);
	}

	@GetMapping("/id")
	public ResponseEntity<BlockDTO> getBlockById(@RequestParam Integer floor, @RequestParam Integer code) {
		BlockId blockId = new BlockId(floor, code);

		Block block = blockService.getBlockById(blockId).orElseThrow(() -> new ResourceNotFoundException(
				"Block not found with block floor " + floor + " and " + " block code " + code));

		return ResponseEntity.ok(convertToDTO(block));
	}

	@GetMapping("/{floor}/{code}")
	public ResponseEntity<BlockDTO> getBlockByFloorAndCode(@PathVariable Integer floor, @PathVariable Integer code) {
		Block block = blockService.getBlockByFloorAndCode(floor, code).orElseThrow(() -> new ResourceNotFoundException(
				"Block not found with block floor " + floor + " and " + " block code " + code));
		;

		return ResponseEntity.ok(convertToDTO(block));
	}
}