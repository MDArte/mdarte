package org.andromda.metafacades.uml14;

import org.apache.commons.lang.StringUtils;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecEntityAttribute.
 *
 * @see org.andromda.metafacades.uml.CoppetecEntityAttribute
 */
public class CoppetecEntityAttributeLogicImpl
    extends CoppetecEntityAttributeLogic
{

    public CoppetecEntityAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CoppetecEntityAttribute#getColumnUniqueGroup()
     */
    protected java.lang.String handleGetColumnUniqueGroup()
    {
        final String group = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_UNIQUE_GROUP);
        return group != null ? StringUtils.trimToEmpty(group) : null;
    }

}