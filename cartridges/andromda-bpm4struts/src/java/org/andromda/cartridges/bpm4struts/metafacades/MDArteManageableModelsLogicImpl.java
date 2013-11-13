package org.andromda.cartridges.bpm4struts.metafacades;


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
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.MDArteManageableModels#splitPackageName(java.lang.String)
     */
    protected java.util.Collection handleSplitPackageName(java.lang.String packageName)
    {
        // TODO: put your implementation here.
        return null;
    }

}