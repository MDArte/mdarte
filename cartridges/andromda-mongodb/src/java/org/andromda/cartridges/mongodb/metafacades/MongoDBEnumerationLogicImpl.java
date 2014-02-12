package org.andromda.cartridges.mongodb.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.mongodb.metafacades.MongoDBEnumeration.
 *
 * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEnumeration
 */
public class MongoDBEnumerationLogicImpl
    extends MongoDBEnumerationLogic
{
    // ---------------- constructor -------------------------------
    public MongoDBEnumerationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The pattern to use when constructing the enumeration name.
     */
    private static final String ENUMERATION_NAME_PATTERN = "enumerationNamePattern";

    /**
     * Returns the value of the enumeration name pattern.
     *
     * @return the enumeration name pattern.
     */
    private String getEnumerationNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(ENUMERATION_NAME_PATTERN));
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEnumeration#getEnumerationName()
     */
    protected String handleGetEnumerationName()
    {
        return StringUtils.trimToEmpty(this.getEnumerationNamePattern()).replaceAll(
            "\\{0\\}",
            super.getName());
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEnumeration#getFullyQualifiedMongoDBEnumerationType()
     */
    protected String handleGetFullyQualifiedMongoDBEnumerationType()
    {
        return MongoDBMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEnumerationName(),
            null);
    }

	public boolean validateLiterals() {
		// TODO Auto-generated method stub
		return true;
	}
}