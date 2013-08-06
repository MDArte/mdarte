package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceClientFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceClientFacade
 */
public class CoppetecEJBWebServiceClientFacadeLogicImpl
    extends CoppetecEJBWebServiceClientFacadeLogic{

    public String insertModuleNameIntoOutlet(String outlet){
    	return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithServiceModuleName(outlet);
    }
    
    public CoppetecEJBWebServiceClientFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceClientFacade#getWsdlLocation()
     */
    protected java.lang.String handleGetWsdlLocation()
    {
        return (String)findTaggedValue(EJBProfile.TAGGEDVALUE_WEB_SERVICE_CLIENT_WSDL_LOCATION);
    }

}