package org.andromda.cartridges.mongodb.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd;
import org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEndLogic;
import org.andromda.cartridges.mongodb.metafacades.MongoDBEntity;
import org.andromda.cartridges.mongodb.metafacades.MongoDBGlobals;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd.
 *
 * @see org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd
 */
public class MongoDBAssociationEndLogicImpl
    extends MongoDBAssociationEndLogic
{
    public MongoDBAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Value for set
     */
    private static final String COLLECTION_TYPE_SET = "set";

    /**
     * Value for map
     */
    private static final String COLLECTION_TYPE_MAP = "map";

    /**
     * Value for bags
     */
    private static final String COLLECTION_TYPE_BAG = "bag";

    /**
     * Value for list
     */
    private static final String COLLECTION_TYPE_LIST = "list";

    /**
     * Value for collections
     */
    private static final String COLLECTION_TYPE_COLLECTION = "collection";

    /**
     * Stores the valid collection types
     */
    private static final Collection collectionTypes = new ArrayList();

    static
    {
        collectionTypes.add(COLLECTION_TYPE_SET);
        collectionTypes.add(COLLECTION_TYPE_MAP);
        collectionTypes.add(COLLECTION_TYPE_BAG);
        collectionTypes.add(COLLECTION_TYPE_LIST);
        collectionTypes.add(COLLECTION_TYPE_COLLECTION);
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntityAssociationEnd#isOne2OnePrimary()
     */
    protected boolean handleIsOne2OnePrimary()
    {
        boolean primaryOne2One = super.isOne2One();
        MongoDBAssociationEnd otherEnd = (MongoDBAssociationEnd)this.getOtherEnd();

        if (primaryOne2One)
        {
            primaryOne2One = super.isAggregation() || this.isComposition();
        }

        // if the flag is false delegate to the super class
        if (!primaryOne2One)
        {
            primaryOne2One = super.isOne2One() && !otherEnd.isAggregation() && !otherEnd.isComposition();
        }

        return primaryOne2One;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        String getterSetterTypeName = super.getGetterSetterTypeName();

        if (!this.isMany())
        {
            ClassifierFacade type = this.getType();

            if (type instanceof MongoDBEntity)
            {
                final String typeName = ((MongoDBEntity)type).getFullyQualifiedEntityName();

                if (StringUtils.isNotEmpty(typeName))
                {
                    getterSetterTypeName = typeName.concat("MongoDB");
                }
            }
        }

        if (this.isMany())
        {
            final boolean specificInterfaces =
                Boolean.valueOf(
                    ObjectUtils.toString(this.getConfiguredProperty(MongoDBGlobals.SPECIFIC_COLLECTION_INTERFACES)))
                       .booleanValue();

            final TypeMappings mappings = this.getLanguageMappings();
            if (mappings != null)
            {
                if (this.isMap())
                {
                    getterSetterTypeName = mappings.getTo(UMLProfile.MAP_TYPE_NAME);
                }
                else if (specificInterfaces)
                {
                    if (this.isSet())
                    {
                        getterSetterTypeName = mappings.getTo(UMLProfile.SET_TYPE_NAME);
                    }
                    else if (this.isList())
                    {
                        getterSetterTypeName = mappings.getTo(UMLProfile.LIST_TYPE_NAME);
                    }
                }
                else
                {
                    getterSetterTypeName =
                        ObjectUtils.toString(this.getConfiguredProperty(MongoDBGlobals.DEFAULT_COLLECTION_INTERFACE));
                }
            }
            else
            {
                getterSetterTypeName =
                    ObjectUtils.toString(this.getConfiguredProperty(MongoDBGlobals.DEFAULT_COLLECTION_INTERFACE));
            }
        }

        return getterSetterTypeName;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntityAssociationEnd#isOne2OneSecondary()
     */
    protected boolean handleIsOne2OneSecondary()
    {
        boolean secondary = false;
        Object type = this.getType();
        Object otherType = this.getOtherEnd().getType();

        if ((type != null) && MongoDBEntity.class.isAssignableFrom(type.getClass()) && (otherType != null) &&
            MongoDBEntity.class.isAssignableFrom(otherType.getClass()))
        {
            MongoDBEntity entity = (MongoDBEntity)type;
            MongoDBEntity otherEntity = (MongoDBEntity)otherType;
            secondary =
                (this.isChild() ) ||
                (!this.isNavigable() && this.getOtherEnd().isNavigable() && !this.isOne2OnePrimary());
        }

        return secondary;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntityAssociationEnd#isMongoDBInverse()
     */
    protected boolean handleIsMongoDBInverse()
    {
        // inverse can only be true if the relation is bidirectional
        boolean inverse = this.isNavigable() && this.getOtherEnd().isNavigable();

        if (inverse)
        {
            inverse = this.isMany2One();

            // for many-to-many we just put the flag on the side that
            // has the lexically longer fully qualified name for
            // it's type
            if (this.isMany2Many() && !inverse)
            {
                String endTypeName = StringUtils.trimToEmpty(this.getType().getFullyQualifiedName(true));
                String otherEndTypeName =
                    StringUtils.trimToEmpty(this.getOtherEnd().getType().getFullyQualifiedName(true));
                int compareTo = endTypeName.compareTo(otherEndTypeName);

                // if for some reason the fully qualified names are equal,
                // compare the names.
                if (compareTo == 0)
                {
                    String endName = StringUtils.trimToEmpty(this.getName());
                    String otherEndName = StringUtils.trimToEmpty(this.getOtherEnd().getName());
                    compareTo = endName.compareTo(otherEndName);
                }

                inverse = compareTo < 0;
            }
        }

        return inverse;
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        Object type = this.getType();

        if ((type != null) && MongoDBEntity.class.isAssignableFrom(type.getClass()))
        {
            MongoDBEntity entity = (MongoDBEntity)type;

            if ((entity.getGeneralization() != null))
            {
                required = false;
            }
        }

        return required;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd#getCollectionType()
     */
    protected String handleGetCollectionType()
    {
        String collectionType = "";

        if (!collectionTypes.contains(collectionType))
        {
            if (this.isOrdered())
            {
                collectionType = COLLECTION_TYPE_LIST;
            }
        }

        return collectionType;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd#isMap()
     */
    protected boolean handleIsMap()
    {
        boolean isMap = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_MAP);

        if (isMap)
        {
            isMap = !this.isOrdered();
        }

        return isMap;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd#isList()
     */
    protected boolean handleIsList()
    {
        boolean isList = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_LIST);

        if (!isList)
        {
            isList = this.isOrdered();
        }

        return isList;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd#isSet()
     */
    protected boolean handleIsSet()
    {
        boolean isSet = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_SET);

        if (isSet)
        {
            isSet = !this.isOrdered();
        }

        return isSet;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd#isBag()
     */
    protected boolean handleIsBag()
    {
        return this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_BAG);
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBAssociationEnd#getCollectionTypeImplementation()
     */
    protected String handleGetCollectionTypeImplementation()
    {
        StringBuffer implementation = new StringBuffer();

        if (this.isMany())
        {
            implementation.append("new ");

            if (this.isSet())
            {
                implementation.append(this.getConfiguredProperty(MongoDBGlobals.SET_TYPE_IMPLEMENTATION));
            }
            else if (this.isMap())
            {
                implementation.append(this.getConfiguredProperty(MongoDBGlobals.MAP_TYPE_IMPLEMENTATION));
            }
            else if (this.isBag())
            {
                implementation.append(this.getConfiguredProperty(MongoDBGlobals.BAG_TYPE_IMPLEMENTATION));
            }
            else if (this.isList())
            {
                implementation.append(this.getConfiguredProperty(MongoDBGlobals.LIST_TYPE_IMPLEMENTATION));
            }
            else
            {
                implementation.append(this.getConfiguredProperty(MongoDBGlobals.MONGODB_LIST_TYPE_IMPLEMENTATION));
            }

            implementation.append("()");
        }

        return implementation.toString();
    }

}