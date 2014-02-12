package org.andromda.cartridges.mongodb;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * The MongoDB profile. Contains the profile information (tagged values, and stereotypes) for the MongoDB
 * cartridge.
 *
 * @author Chad Brandon
 * @author Carlos Cuenca
 */
public class MongoDBProfile
    extends UMLProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */
    
    /**
     * Indicates if the class is a document
     */
    
    public static final String STEREOTYPE_DOCUMENT = profile.get("DOCUMENT");
    
    /* ----------------- Tagged Values -------------------- */

    /**
     * Stores the aggregation type.
     */
    
    public static final String TAGGEDVALUE_MONGODB_ASSOCIATION_TYPE = profile.get("MONGODB_ASSOCIATION_TYPE");

}