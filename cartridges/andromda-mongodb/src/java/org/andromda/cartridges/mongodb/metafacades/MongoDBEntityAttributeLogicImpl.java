package org.andromda.cartridges.mongodb.metafacades;

import org.andromda.cartridges.mongodb.MongoDBProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.mongodb.metafacades.MongoDBEntityAttribute.
 *
 * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntityAttribute
 */
public class MongoDBEntityAttributeLogicImpl
    extends MongoDBEntityAttributeLogic
{
    // ---------------- constructor -------------------------------
    public MongoDBEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        if (this.getOwner() instanceof MongoDBEntity)
        {
            MongoDBEntity entity = (MongoDBEntity)this.getOwner();
            if (entity.getGeneralization() != null)
            {
                required = false;
            }
        }
        return required;
    }
    
    /**
     * Override to provide java specific handling of the default value.
     * 
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String fullyQualifiedName = StringUtils.trimToEmpty(type.getFullyQualifiedName());
            /*if ("java.lang.String".equals(fullyQualifiedName))
            {
                defaultValue = "\"" + defaultValue + "\"";
            }
            else if (fullyQualifiedName.startsWith("java.lang"))
            {
                defaultValue = fullyQualifiedName + ".valueOf(" + defaultValue + ")";
            }*/
        }
        return defaultValue;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntityAttribute#concatColumnName(java.lang.String,
     *      java.lang.String)
     */
    protected String handleConcatColumnName(
        java.lang.String prefix,
        java.lang.String name)
    {
        String returnValue = name;
        if (StringUtils.isNotBlank(prefix))
        {
            returnValue = prefix + this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) + name;

            // handle maxSqlNameLength
            Short maxSqlNameLength =
                Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
            returnValue = EntityMetafacadeUtils.ensureMaximumNameLength(returnValue, maxSqlNameLength);
        }
        return returnValue;
    }
    
}