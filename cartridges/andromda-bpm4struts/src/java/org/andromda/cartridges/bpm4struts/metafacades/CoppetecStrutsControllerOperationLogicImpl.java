package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsControllerOperation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsControllerOperation
 */
public class CoppetecStrutsControllerOperationLogicImpl
    extends CoppetecStrutsControllerOperationLogic
{
	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getController().getUseCase().getPackage())).replaceOutletWithWebModuleName(outlet); 
	}
    
    public CoppetecStrutsControllerOperationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsControllerOperation#getInterfaceFormFields()
     */

	protected java.util.List handleGetInterfaceFormFields()
    {
    	final Map formFieldsMap = new HashMap();
        final Map formFieldsIn = new HashMap();
        final Map formFieldsInTemp = new HashMap();
        boolean tableAction;

        // get all actions deferring to this operation
        final List deferringActions = this.getDeferringActions();
        for (int i = 0; i < deferringActions.size(); i++)
        {
            final StrutsAction action = (StrutsAction)deferringActions.get(i);
            
            // store the action parameters
            final List actionFormFields = action.getActionFormFields();
            for (int j = 0; j < actionFormFields.size(); j++)
            {
            	final StrutsParameter parameter = (StrutsParameter)actionFormFields.get(j);
            		
            	// verifica se o parametro ja existe e se o tipo eh o mesmmo
            	if ((formFieldsIn.containsKey(parameter.getName()) && 
                		((StrutsParameter) formFieldsIn.get(parameter.getName())).getType().getFullyQualifiedName().equals(parameter.getType().getFullyQualifiedName()) ) || (i == 0))
                {
            		tableAction = false;
            		
            		// verifica se eh table action
            		if (parameter.getStrutsAction() != null)
            		{
            			if (parameter.getStrutsAction().isTableAction() && i != 0)
            			{
            				tableAction = true;
            				
            				if (((StrutsParameter)formFieldsIn.get(parameter.getName())).getStrutsAction() != null)
            					if ( ((StrutsParameter)formFieldsIn.get(parameter.getName())).getStrutsAction().isTableAction() )
            						formFieldsInTemp.put(parameter.getName(), parameter);
            			}
            		}
            		
            		if (!tableAction)
            		{
            			// verifica se o parametro antigo eh table action
            			if (i != 0)
            				if (((StrutsParameter)formFieldsIn.get(parameter.getName())).getStrutsAction() != null)
            					if ( ((StrutsParameter)formFieldsIn.get(parameter.getName())).getStrutsAction().isTableAction() )
            						tableAction = true;
        			
            			if (!tableAction)
            			{
		            		// verifica se widgetType eh radio
		            		if ("radio".equalsIgnoreCase(parameter.getWidgetType()) && i != 0)
		            			formFieldsInTemp.put(parameter.getName(), formFieldsIn.get(parameter.getName()));
		            		else
		            			formFieldsInTemp.put(parameter.getName(), parameter);
            			}
            		}
                }
            	
            }
            
            formFieldsIn.clear();
            formFieldsIn.putAll(formFieldsInTemp);
            formFieldsInTemp.clear();
        }
        
        formFieldsMap.putAll(formFieldsIn);        
        
        return new ArrayList(formFieldsMap.values());
    }    

}