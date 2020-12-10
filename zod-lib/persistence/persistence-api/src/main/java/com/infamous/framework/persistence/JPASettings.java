package com.infamous.framework.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

public class JPASettings {

    private @Getter final String m_databaseUrl;
    private @Getter final String m_user;
    private @Getter final String m_password;
    private @Getter final String m_datasourceClassName;

    private Map<String, Object> m_options = new HashMap<>();

    @Builder
    public JPASettings(String url, String user, String password, String datasourceClassName) {
        Objects.requireNonNull(url, "Url can not be null");
        Objects.requireNonNull(user, "User/Username can not be null");
        Objects.requireNonNull(password, "Password can not be null");
        Objects.requireNonNull(datasourceClassName, "DataSourceClassName can not be null");

        this.m_databaseUrl = url;
        this.m_user = user;
        this.m_password = password;
        this.m_datasourceClassName = datasourceClassName;
    }

    public void put(String key, Object val) {
        m_options.put(key, val);
    }

    public Optional<Object> get(String key) {
        return Optional.ofNullable(m_options.get(key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JPASettings that = (JPASettings) o;
        return Objects.equals(m_databaseUrl, that.m_databaseUrl) &&
            Objects.equals(m_user, that.m_user) &&
            Objects.equals(m_password, that.m_password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_databaseUrl, m_user, m_password);
    }
}
