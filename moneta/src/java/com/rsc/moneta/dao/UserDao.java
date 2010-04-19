/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.dao;

import com.rsc.moneta.Currency;
import com.rsc.moneta.action.Const;
import com.rsc.moneta.bean.Account;
import com.rsc.moneta.bean.Sms;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.util.PasswordGenerator;
import java.util.Collection;
import java.util.prefs.InvalidPreferencesFormatException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author sulic
 */
public class UserDao extends Dao {

    public UserDao(EntityManager em) {
        super(em);
    }

    public User createUserAndSendNotify(String phone, String name, String email) {
        em.getTransaction().begin();
        try {
            User user = new User();            
            user.setEmail(email);
            user.setName(name);
            user.setPassword(generatePassword());
            em.persist(user);

            Account account = new Account();
            account.setType(Currency.EURO);
            account.setUser(user);
            account.setBalance(0);            
            em.persist(account);

            account = new Account();
            account.setType(Currency.RUB);
            account.setUser(user);
            account.setBalance(0);
            em.persist(account);

            account = new Account();
            account.setType(Currency.USD);
            account.setUser(user);
            account.setBalance(0);
            em.persist(account);
            em.getTransaction().commit();
            user.setPhone(phone);
            persist(user);
            return user;
        } catch(Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            return null;
        }
    }

    public User getUserByPhone(String phone) {
        Query q = getEntityManager().createQuery("select u from User u where u.phone=:phone");
        try {
            q.setParameter("phone", phone);
            return (User) q.getSingleResult();
        } catch (NoResultException e1) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<User> getAllUserList() {
        Query q = getEntityManager().createQuery("select u from User u");
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    public User getUserByPhoneAndPassword(String phone, String password) {
        Query q = em.createQuery("select u from User u where u.phone=:phone and u.password=:pswd");
        try {
            q.setParameter("phone", phone);
            q.setParameter("pswd", password);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    public User getUserByEmailAndPassword(String email, String password) {
        Query q = em.createQuery("select u from User u where u.email=:email and u.password=:pswd");
        try {
            q.setParameter("email", email);
            q.setParameter("pswd", password);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }

    public long getUserCount() {
        Query q = em.createQuery("select count(u) from User u");
        return (Integer) q.getSingleResult();
    }

    public Collection<User> getUsers(int page) {
        Query q = em.createQuery("select u from User u");
        q.setFirstResult(page * Const.ROWS_COUNT);
        q.setMaxResults(Const.ROWS_COUNT);
        return q.getResultList();
    }

    public String generatePassword() throws InvalidPreferencesFormatException {
        // Генерация пароля
        PasswordGenerator passGen = new PasswordGenerator();
        passGen.clearTemplate();
        passGen.setNumbersIncluded(true);
        passGen.generatePassword();
        return passGen.getPassword();
    }

    public User getUserByEmail(String _contactEmail) {
        Query q = getEntityManager().createQuery("select u from User u where u.email=:email");
        try {
            q.setParameter("email", _contactEmail);
            return (User) q.getSingleResult();
        } catch (NoResultException e1) {
            return null;
        } catch (NonUniqueResultException e2) {
            return null;
        }
    }
}
