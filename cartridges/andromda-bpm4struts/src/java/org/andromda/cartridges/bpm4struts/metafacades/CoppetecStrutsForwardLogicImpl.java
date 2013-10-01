package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.CoppetecFrontEndPackage;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.CoppetecUMLMetafacadeProperties;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsForward.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsForward
 */
public class CoppetecStrutsForwardLogicImpl
extends CoppetecStrutsForwardLogic
{

	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getUseCase().getPackage())).replaceOutletWithWebModuleName(outlet); 
	}
    
    public CoppetecStrutsForwardLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    protected java.lang.String handleGetForwardPath()
    {
    	String forwardPath = null;
        
        final StateVertexFacade target = this.getTarget();
        
        if (isEnteringPage())
        {
            forwardPath = ((StrutsJsp)target).getFullPath() + ".jsp";
        }
        else if (isEnteringFinalState())
        {
            forwardPath = ((StrutsFinalState)target).getFullPath();
  
            if(((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_EXTERNAL_APPLICATION_NAME)== null || ((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_EXTERNAL_APPLICATION_NAME).equals("")){
	            if(((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK) == null){
	            	// caso tagged value não preenchida.. entao considera que NÃO é troca de modulo
	            	
	        		
	            }
	            else{
	            	if(((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK_MODULO) == null)
	            		forwardPath = "/" + getConfiguredProperty(CoppetecUMLMetafacadeProperties.CONTEXTO_PRINCIPAL) +  
                        ((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK).toString();
	            	else
	            		forwardPath = "/" + getConfiguredProperty(CoppetecUMLMetafacadeProperties.CONTEXTO_PRINCIPAL) + "/" + 
	            					  ((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK_MODULO).toString() +
	            					  ((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK).toString();
	            		
	            	
	            }
            }else{
            	String modulo ="";
            	String link = "";
            	if(((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK_MODULO)!=null)
            	modulo = ((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK_MODULO).toString();
            	if(((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK)!=null)
                link = ((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK).toString();
            	forwardPath = "/" + 
            					((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_EXTERNAL_APPLICATION_NAME).toString()
            				+ "/" + 
            				  	modulo
            				  	  +
            				  	link;
            }
        }
        return forwardPath.replaceAll("//", "/");
    }
    
    protected boolean handleIsModuloRedirect(){
    	final StateVertexFacade target = this.getTarget();
    	
    	if (isEnteringFinalState())
        {
    		
    		if(((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK) != null)
    			return true;
    		
    		// caso caso de uso alvo não esteja no modelo.. considera que NÃO é troca de módulo
    		if (((StrutsFinalState)target).getTargetUseCase() == null)
    			return false;
    		
            CoppetecFrontEndPackage pacoteAtual = (CoppetecFrontEndPackage)this.getUseCase().getPackage();
            CoppetecFrontEndPackage pacoteDestino = (CoppetecFrontEndPackage)((StrutsFinalState)target).getTargetUseCase().getPackage();
            
            if(!pacoteAtual.getWebModuleName().equals(pacoteDestino.getWebModuleName())){
            		return true;
            }
        }
    	
    	
    	
    	return false;
    }
    
    protected String handleGetTargetContext(){
    	final StateVertexFacade target = this.getTarget();
    	if (isEnteringFinalState())
        {
    		if(((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK) != null){
    			String contexto = null;
    			String modulo = null;
    			String tagged = null;
    	
    			if(((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK_MODULO) != null){
    				contexto = ((CoppetecFrontEndPackage)(getUseCase().getPackage())).getContexto();
    				modulo = ((CoppetecPackageFacade)(getUseCase().getPackage())).getWebModuleName(); 
    				tagged = (String)((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK_MODULO);
    				
					contexto = (String)getConfiguredProperty(CoppetecUMLMetafacadeProperties.CONTEXTO_PRINCIPAL);

					if(!modulo.equals("/"))			
						contexto = contexto + "/" + tagged;

					return contexto.replaceAll("//","/");
    			}
    		}
    			
    		
            CoppetecFrontEndPackage pacoteAtual = (CoppetecFrontEndPackage)this.getUseCase().getPackage();
            CoppetecFrontEndPackage pacoteDestino = (CoppetecFrontEndPackage)((StrutsFinalState)target).getTargetUseCase().getPackage();
            
            if(!pacoteAtual.getWebModuleName().equals(pacoteDestino.getWebModuleName()))
            	return pacoteDestino.getContexto();
            
            return null;
        }
    	else
    		return ((CoppetecStrutsPackage)(getUseCase().getPackage())).getContexto();
 
    }
	protected String handleGetStrutsActionName() {
		String actionName = null;
		final StateVertexFacade target = this.getTarget();
		if (isEnteringFinalState()) {
			actionName = ((StrutsFinalState) target).getFullPath();
			Object taggedValue = ((StrutsFinalState) target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
			if (taggedValue != null) {
				String linkExterno = taggedValue.toString();
				if (linkExterno != null && !linkExterno.equals("")) {
					actionName = linkExterno;
				}
			}
			actionName = actionName.substring(1).replace("//", "/");
		}
		return actionName;
	}

	protected String handleGetStrutsNamespace() {
		final StateVertexFacade target = this.getTarget();
		if(isEnteringFinalState()){
			String appName = (String)((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_EXTERNAL_APPLICATION_NAME);
			
			String contextoPrincipal="/";
			
			if(appName!=null && !appName.equals("")){
				contextoPrincipal+=appName;
			}else{
				contextoPrincipal+=getConfiguredProperty(CoppetecUMLMetafacadeProperties.CONTEXTO_PRINCIPAL).toString();
			}
			String modulo=(String)((StrutsFinalState)target).findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK_MODULO);
			
			if(modulo!=null && !modulo.equals("")){
				return (contextoPrincipal+"/"+modulo).replace("//", "/");
			}
			String externalLink= (String)((StrutsFinalState)target).findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
			if(externalLink!=null && !externalLink.equals("")){
				//O caso de uso de destino está no módulo principal
				return contextoPrincipal.replace("//", "/");
			}
			if(this.getUseCase().getPackage()!=null){
				ModelElementFacade pkg = getUseCase().getPackage();
				while(!(pkg instanceof CoppetecStrutsPackage) && pkg!=null){
					pkg = pkg.getPackage();
				}
				if(pkg!=null){
					return ((CoppetecStrutsPackage)pkg).getContexto().replace("//", "/");
				}else{
					return ((CoppetecFrontEndPackage)(getUseCase().getPackage())).getContexto().replace("//", "/") ;
				}
			}
		}
			
		return "";
	}
}