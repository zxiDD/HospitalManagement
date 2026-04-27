package com.cg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Block;
import com.cg.entity.BlockId;

@Repository
public interface BlockRepository extends JpaRepository<Block, BlockId> {

}
