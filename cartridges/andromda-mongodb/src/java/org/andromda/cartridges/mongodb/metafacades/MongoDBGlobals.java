package org.andromda.cartridges.mongodb.metafacades;


/**
 * Stores Globals specific to the MongoDB cartridge.
 *
 * @author Chad Brandon
 */
public class MongoDBGlobals
{
    /**
     * MongoDB version to use.
     */
    static final String MONGODB_VERSION = "mongoDBVersion";

    /**
     * The namespace property to specify the pattern for determining the entity
     * name.
     */
    static final String ENTITY_NAME_PATTERN = "entityNamePattern";

    /**
     * The property which stores the pattern defining the entity implementation
     * name.
     */
    static final String ENTITY_IMPLEMENTATION_NAME_PATTERN = "entityImplementationNamePattern";

    /**
     * The property which defines a default value for mongodb entities
     * versioning.
     */
    static final String MONGODB_VERSION_PROPERTY = "versionProperty";
    
    /**
     * The 'mongoDBlist' type implementation to use.
     */
    static final String MONGODB_LIST_TYPE_IMPLEMENTATION = "mongoDBListTypeImplementation";
    
    /**
     * The 'list' type implementation to use.
     */
    static final String LIST_TYPE_IMPLEMENTATION = "listTypeImplementation";

    /**
     * The 'set' type implementation to use.
     */
    static final String SET_TYPE_IMPLEMENTATION = "setTypeImplementation";

    /**
     * The 'map' type implementation to use.
     */
    static final String MAP_TYPE_IMPLEMENTATION = "mapTypeImplementation";

    /**
     * The 'bag' type implementation to use.
     */
    static final String BAG_TYPE_IMPLEMENTATION = "bagTypeImplementation";

    /**
     * A flag indicating whether or not specific (java.util.Set, java.util.List,
     * etc) collection interfaces should be used in assocation mutators and
     * accessors or whether the generic java.util.Collection interface should be
     * used.
     */
    static final String SPECIFIC_COLLECTION_INTERFACES = "specificCollectionInterfaces";

    /**
     * The property that defines the default collection interface, this is the
     * interface used if the property defined by
     * {@link #SPECIFIC_COLLECTION_INTERFACES} is true.
     */
    static final String DEFAULT_COLLECTION_INTERFACE = "defaultCollectionInterface";
    
}