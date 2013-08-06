package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.CoppetecEntityMetafacadeUtils;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecEntityAssociation.
 *
 * @see org.andromda.metafacades.uml.CoppetecEntityAssociation
 */
public class CoppetecEntityAssociationLogicImpl
    extends CoppetecEntityAssociationLogic
{

    public CoppetecEntityAssociationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityAssociationFacade#getTableName()
     */
    public String handleGetTableName()
    {
        String tableName = null;
        final Collection ends = this.getAssociationEnds();
        if (ends != null && !ends.isEmpty())
        {
            final AssociationEndFacade end = (AssociationEndFacade)ends.iterator().next();
            if (end.isMany2Many())
            {
                // prevent ClassCastException if the association isn't an
                // Entity
                if (Entity.class.isAssignableFrom(end.getType().getClass()))
                {
                    final String prefixProperty = UMLMetafacadeProperties.TABLE_NAME_PREFIX;
                    final String tableNamePrefix =
                        this.isConfiguredProperty(prefixProperty)
                        ? ObjectUtils.toString(this.getConfiguredProperty(prefixProperty)) : null;
                    tableName =
                        CoppetecEntityMetafacadeUtils.getSqlNameFromTaggedValueAbreviado(
                            tableNamePrefix,
                            this,
                            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                            ((Entity)end.getType()).getMaxSqlNameLength(),
                            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR),
                            getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR));
                }
            }
        }
        return tableName;
    }
}