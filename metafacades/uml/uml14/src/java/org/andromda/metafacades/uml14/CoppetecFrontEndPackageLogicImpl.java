package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.metafacades.uml.CoppetecFrontEndPackage;
import org.andromda.metafacades.uml.CoppetecUMLMetafacadeProperties;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.ModelElementFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecFrontEndPackage.
 *
 * @see org.andromda.metafacades.uml.CoppetecFrontEndPackage
 */
public class CoppetecFrontEndPackageLogicImpl
    extends CoppetecFrontEndPackageLogic
{

    public CoppetecFrontEndPackageLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CoppetecFrontEndPackage#getUrl()
     */
    
    protected java.lang.String handleGetUrl()
    {
    	return null; //not used
    }
    
    protected java.lang.String handleGetContexto()
    {
	boolean principal = false;
	for(ModelElementFacade packageFacade = this; packageFacade != null; packageFacade = packageFacade.getPackage())
		if (packageFacade.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_PRINCIPAL)){
			principal = true;
			break;
		}
			

    	if (principal)
    		return getConfiguredProperty(CoppetecUMLMetafacadeProperties.CONTEXTO_PRINCIPAL).toString();
    	else{
    		String contexto = getConfiguredProperty(CoppetecUMLMetafacadeProperties.CONTEXTO_PRINCIPAL).toString() + "/" +this.getWebModuleName();
    		
    		return contexto.replaceAll("//", "/");
    	}
    		
    }

}