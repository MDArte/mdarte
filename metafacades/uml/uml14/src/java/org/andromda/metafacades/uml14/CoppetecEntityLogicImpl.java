package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.CoppetecEntityMetafacadeUtils;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecEntity.
 *
 * @see org.andromda.metafacades.uml.CoppetecEntity
 */
public class CoppetecEntityLogicImpl
    extends CoppetecEntityLogic
{

    public CoppetecEntityLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

	protected String handleGetTableName() {
		final String prefixProperty = UMLMetafacadeProperties.TABLE_NAME_PREFIX;
        final String tableNamePrefix =
            this.isConfiguredProperty(prefixProperty)
            ? ObjectUtils.toString(this.getConfiguredProperty(prefixProperty)) : null;
        return CoppetecEntityMetafacadeUtils.getSqlNameFromTaggedValueAbreviado(
            tableNamePrefix,
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
            this.getMaxSqlNameLength(),
            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR),
            getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR));
	}

}