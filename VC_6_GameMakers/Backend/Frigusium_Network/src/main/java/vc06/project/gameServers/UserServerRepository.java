package vc06.project.gameServers;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jiwoo Kim
 */
public interface UserServerRepository extends JpaRepository<UserServer, Integer> {
}