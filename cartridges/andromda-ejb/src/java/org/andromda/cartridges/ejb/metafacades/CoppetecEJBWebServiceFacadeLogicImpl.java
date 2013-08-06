package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBGlobals;
import org.andromda.cartridges.ejb.EJBUtils;
import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceFacade
 */
public class CoppetecEJBWebServiceFacadeLogicImpl
    extends CoppetecEJBWebServiceFacadeLogic
{

        public String insertModuleNameIntoOutlet(String outlet){
        	return super.insertModuleNameIntoOutlet(outlet);
        }
    
    public CoppetecEJBWebServiceFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceFacade#getServiceName()
     */
    protected java.lang.String handleGetServiceName()
    {
    	return super.getName() + "WS";   
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceFacade#getTargetNamespace()
     */
    protected java.lang.String handleGetTargetNamespace()
    {
        return "http://" + EJBUtils.moreSpecificPackageName(this.getPackage().getFullyQualifiedName());
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceFacade#getContext()
     */
    protected java.lang.String handleGetContext()
    {
    	String contextoPrincipal = "/" + (String)this.getConfiguredProperty(EJBGlobals.PROJECT_ID) + "-services";
    	String modulo = "";
    	
    	CoppetecPackageFacade pacote = (CoppetecPackageFacade)this.getPackage();
    	
    	if(pacote.getServiceModuleName() != null && !pacote.getServiceModuleName().equals("")) modulo = "-" + pacote.getServiceModuleName();
    	
    	return contextoPrincipal + modulo;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBWebServiceFacade#isJaxws()
     */
    protected boolean handleIsJaxws()
    {
        Boolean ejb3 = Boolean.valueOf((String)this.getConfiguredProperty(EJBGlobals.EJB3));
        return ejb3.booleanValue();
    }

	protected String handleGetUrlPattern() {
		String pattern = "/" + this.getName() + "Srv";
		return pattern;
	}
}