package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Date;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.MDArteManageableModels.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.MDArteManageableModels
 */
public class MDArteManageableModelsLogicImpl
    extends MDArteManageableModelsLogic
{

        public String insertModuleNameIntoOutlet(String outlet){
		return outlet;
    }
    
    public MDArteManageableModelsLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.MDArteManageableModels#getDataAtual()
     */
    protected java.lang.String handleGetDataAtual()
    {
        return new Date().toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.MDArteManageableModels#splitPackageName(java.lang.String)
     */
    protected java.util.Collection handleSplitPackageName(java.lang.String packageName)
    {
    	ArrayList lista = new ArrayList();
    	
    	String[] packages = packageName.split("\\.");
    	
    	for (int i = 0; i < packages.length ; i++) lista.add(packages[i]);
    	
    	return lista;
    }

}