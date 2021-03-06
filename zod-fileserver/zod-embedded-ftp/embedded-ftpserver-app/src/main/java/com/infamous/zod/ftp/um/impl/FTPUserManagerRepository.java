package com.infamous.zod.ftp.um.impl;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.service.PasswordEncryptor;
import com.infamous.framework.sensitive.service.SaltedPasswordEncryptor;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.model.FTPUser;
import com.infamous.zod.ftp.model.FTPUserKey;
import com.infamous.zod.ftp.um.FTPUserDAO;
import com.infamous.zod.ftp.um.FTPUserManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;

@Transactional
public class FTPUserManagerRepository implements FTPUserManager {

    private FTPUserDAO m_dao;
    private PasswordEncryptor m_passwordEncryptor;
    private FTPServerConfigProperties m_serverConfig;

    public FTPUserManagerRepository() {

    }

    public FTPUserManagerRepository(FTPUserDAO dao, PasswordEncryptor passwordEncryptor,
                                    FTPServerConfigProperties serverConfig) {
        this.m_dao = dao;
        this.m_passwordEncryptor = passwordEncryptor;
        this.m_serverConfig = serverConfig;
    }

    @Override
    public String hashPassword(String rawPassword) {
        if (m_passwordEncryptor instanceof SaltedPasswordEncryptor) {
            SaltedPasswordEncryptor saltedPE = (SaltedPasswordEncryptor) m_passwordEncryptor;
            return saltedPE.encrypt(MessageDigestAlgorithm.MD5, rawPassword, m_serverConfig.getSalted());
        }
        return m_passwordEncryptor.encrypt(rawPassword);
    }

    @Override
    public User getUserByName(String s) throws FtpException {
        FTPUser user = Optional.ofNullable(m_dao.findById(new FTPUserKey(s)))
            .orElseThrow(() -> new FtpException("User [" + s + "] doesn't exist"));
        user.addDefaultAuthorities();
        return user;
    }

    @Override
    public String[] getAllUserNames() {
        return Optional.ofNullable(m_dao.findAll())
            .map(users -> users.stream().map(FTPUser::getName).collect(Collectors.toList()))
            .orElse(Collections.emptyList())
            .toArray(new String[]{});
    }

    @Override
    public void delete(String s) {
        m_dao.deleteByID(new FTPUserKey(s));
    }

    @Override
    public void save(User user) throws FtpException {
        if (!(user instanceof FTPUser)) {
            throw new FtpException("User is not instance of FTPUser class");
        }
        FTPUser ftpUser = (FTPUser) user;
        m_dao.merge(ftpUser);
    }

    @Override
    public boolean doesExist(String s) {
        return m_dao.findById(new FTPUserKey(s)) != null;
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (!(authentication instanceof UsernamePasswordAuthentication)) {
            throw new AuthenticationFailedException("Authentication failed");
        }
        UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
        checkAuth(!isEmptyString(auth.getUsername()), "You must provide a username");
        checkAuth(!isEmptyString(auth.getPassword()), "You must provide a password");
        User u;
        try {
            u = getUserByName(auth.getUsername());
        } catch (FtpException e) {
            throw new AuthenticationFailedException(e);
        }
        if (!m_passwordEncryptor.matches(auth.getPassword(), u.getPassword())) {
            throw new AuthenticationFailedException("Authentication failed");
        }

        return u;
    }

    @Override
    public String getAdminName() {
        List list = m_dao
            .findByNativeQuery("SELECT username FROM FTPUser WHERE isAdmin = 'true'");
        return !isEmptyList(list) ? (String) list.get(0) : null;
    }

    @Override
    public boolean isAdmin(String s) {
        return m_dao.findById(new FTPUserKey(s)).isAdmin();
    }

    private void checkAuth(boolean condition, String msg) throws AuthenticationFailedException {
        if (!condition) {
            throw new AuthenticationFailedException(msg);
        }
    }

    private boolean isEmptyString(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }
}
