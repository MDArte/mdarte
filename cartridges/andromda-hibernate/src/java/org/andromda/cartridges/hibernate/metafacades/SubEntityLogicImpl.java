package org.andromda.cartridges.hibernate.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.SubEntity.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.SubEntity
 */
public class SubEntityLogicImpl
    extends SubEntityLogic
{

        public String insertModuleNameIntoOutlet(String outlet){
		return outlet;
    }
    
    public SubEntityLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
}