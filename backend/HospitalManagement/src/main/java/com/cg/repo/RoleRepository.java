package com.cg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cg.entity.Role;
import com.cg.entity.RolePk;

public interface RoleRepository extends JpaRepository<Role, RolePk> {
	@Query("SELECT r.id.roleName FROM Role r WHERE r.id.userName = :username")
	List<String> findRolesByUsername(@Param("username") String username);
}