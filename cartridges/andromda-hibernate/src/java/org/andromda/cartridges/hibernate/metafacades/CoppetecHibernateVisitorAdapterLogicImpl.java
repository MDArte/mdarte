package org.andromda.cartridges.hibernate.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.CoppetecHibernateVisitorAdapter.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.CoppetecHibernateVisitorAdapter
 */
public class CoppetecHibernateVisitorAdapterLogicImpl
    extends CoppetecHibernateVisitorAdapterLogic
{

        public String insertModuleNameIntoOutlet(String outlet){
		return outlet;
    }
    
    public CoppetecHibernateVisitorAdapterLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    protected java.util.Collection handleGetEspecializacoes(){
    	 Collection entidades = new ArrayList();
         
         java.util.Iterator specializations = getAllSpecializations().iterator();
         
         while(specializations.hasNext()){
         	ModelElementFacade element = (ModelElementFacade)specializations.next(); 
         	
         	if(element.hasStereotype(UMLProfile.STEREOTYPE_ENTITY)){
         		entidades.add(element);
         	}
         }
         
         return entidades;
    }
}