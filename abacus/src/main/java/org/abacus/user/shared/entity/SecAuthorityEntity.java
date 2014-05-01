package org.abacus.user.shared.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.abacus.common.shared.entity.StaticEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Entity
@Table(name = "sec_authority")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SecAuthorityEntity extends StaticEntity {

}