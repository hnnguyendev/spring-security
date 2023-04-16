package com.nghianh.security.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleDao {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public void createUserRole(Long userId, Long roleId) {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO system_userrole VALUES (:userId, :roleId)");

		entityManager.getTransaction().begin();
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);
		query.executeUpdate();
		entityManager.getTransaction().commit();
	}
}
