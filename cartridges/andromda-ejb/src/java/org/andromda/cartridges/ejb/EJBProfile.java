package org.andromda.cartridges.ejb;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * The EJB profile. Contains the profile information (tagged values, and stereotypes) for the EJB cartridge.
 *
 * @author Chad Brandon
 */
public class EJBProfile
    extends UMLProfile
{
	/**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();
	
    /* ----------------- Stereotypes -------------------- */
    public static final String STEREOTYPE_CREATE_METHOD = "CreateMethod";
    public static final String STEREOTYPE_SELECT_METHOD = "SelectMethod";
    public static final String STEREOTYPE_ENV_ENTRY = "EnvEntry";
    public static final String STEREOTYPE_WEB_SERVICE_DATA = "WebServiceData";
    public static final String STEREOTYPE_OPEN_ACCESS = profile.get("OPEN_ACCESS");

    /**
     * Represents a reference to a value object.
     */
    public static final String STEREOTYPE_VALUE_REF = "ValueRef";

    /* ----------------- Tagged Values -------------------- */
    public static final String TAGGEDVALUE_GENERATE_CMR = "@andromda.ejb.generateCMR";
    public static final String TAGGEDVALUE_EJB_QUERY = "@andromda.ejb.query";
    public static final String TAGGEDVALUE_EJB_VIEWTYPE = "@andromda.ejb.viewType";
    public static final String TAGGEDVALUE_EJB_TRANSACTION_TYPE = "@andromda.ejb.transaction.type";
    public static final String TAGGEDVALUE_EJB_NO_SYNTHETIC_CREATE_METHOD = "@andromda.ejb.noSyntheticCreateMethod";
    public static final String TAGGEDVALUE_WEB_SERVICE_CLIENT_WSDL_LOCATION = "@andromda.web.service.client.wsdl.location";
}