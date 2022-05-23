package dts.com.vn.ilarc.repository;

import dts.com.vn.ilarc.entities.ArchiveSasReRequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRequestRepository extends JpaRepository<ArchiveSasReRequestMessage, Long> {
}
