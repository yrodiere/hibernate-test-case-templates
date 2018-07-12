package org.hibernate.bugs;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
@Access(AccessType.PROPERTY)
public class JPAAccessibleResource extends AccessibleResourcePojo {
    private static final long serialVersionUID = -7104999184521380061L;

    public JPAAccessibleResource() {
        super();
    }

    @Override
    @Column(nullable = false)
    public String getProtocol() {
        return super.getProtocol();
    }

    @Override
    @Column(nullable = false)
    public String getHost() {
        return super.getHost();
    }

    @Override
    @Column(nullable = false)
    public int getPort() {
        return super.getPort();
    }

    @Override
    @Column
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @Column
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    @Column(nullable = false)
    public String getPath() {
        return super.getPath();
    }

    @Override
    @Transient
    public Map<String, String> getParams() {
        return super.getParams();
    }
}
