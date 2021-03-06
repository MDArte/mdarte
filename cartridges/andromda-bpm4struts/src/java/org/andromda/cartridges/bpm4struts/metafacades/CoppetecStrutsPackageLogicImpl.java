package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsPackage.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsPackage
 */
public class CoppetecStrutsPackageLogicImpl
    extends CoppetecStrutsPackageLogic
{

    public String insertModuleNameIntoOutlet(String outlet){
    	return ((CoppetecPackageFacade)(this)).replaceOutletWithWebModuleName(outlet); 
    }
    
    
    
    public CoppetecStrutsPackageLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    
    /** 
     * Retorna uma cole��o contendo os modos de opera��o do m�dulo.
     * Busca esses valores nas tagged values definidas nas transi��es e nos par�metros.
     */
    public Collection handleGetModosOperacao() {
    	
    	Set setModosOperacao = new HashSet();
    	Iterator packageUseCases = getUseCases().iterator();

    	while (packageUseCases.hasNext()) {
    		CoppetecStrutsUseCase useCase = (CoppetecStrutsUseCase) packageUseCases.next();
    		setModosOperacao.addAll(useCase.getModosOperacao());
    	}
    	
    	return setModosOperacao;
    }
    

}