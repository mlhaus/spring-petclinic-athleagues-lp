package org.springframework.samples.petclinic.school;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.Optional;

public interface SchoolRepository extends Repository<School, Integer> {
	/**
	 * Get a School by its domain.
	 */
	@Transactional(readOnly = true)
	@Query("SELECT s FROM School s WHERE s.domain = :domain")
	Optional<School> findByDomain(String domain);

	/**
	 * Retrieve all Schools from the data store.
	 */
	@Transactional(readOnly = true)
	Collection<School> findAll();

	/**
	 * Retrieve Schools by page (for pagination in the UI)
	 */
	@Transactional(readOnly = true)
	Page<School> findAll(Pageable pageable);

	/**
	 * Save a School to the data store, either inserting or updating it.
	 */
	void save(School school);

	/**
	 * Retrieve a School by its id.
	 */
	@Transactional(readOnly = true)
	Optional<School> findById(Integer id);
}
