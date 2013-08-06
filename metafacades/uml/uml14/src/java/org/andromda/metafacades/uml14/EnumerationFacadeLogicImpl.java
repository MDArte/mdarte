package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationFacade
 */
public class EnumerationFacadeLogicImpl
        extends EnumerationFacadeLogic
{
    
    public EnumerationFacadeLogicImpl(org.omg.uml.foundation.core.Classifier metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(
                this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiterals()
     */
    protected java.util.Collection handleGetLiterals()
    {
        return this.getAttributes();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationSignature()
     */
    protected String handleGetFromOperationSignature()
    {
        final StringBuffer signature = new StringBuffer(this.getFromOperationName());
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            signature.append('(');
            signature.append(type.getFullyQualifiedName());
            signature.append(" value)");
        }
        return signature.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationName()
     */
    protected String handleGetFromOperationName()
    {
        final StringBuffer name = new StringBuffer("from");
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            name.append(StringUtils.capitalize(type.getName()));
        }
        return name.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiteralType()
     */
    protected Object handleGetLiteralType()
    {
        Object type = null;
        final Collection literals = this.getLiterals();
        if (literals != null && !literals.isEmpty())
        {
            type = ((AttributeFacade)literals.iterator().next()).getType();
        }
        return type;
    }

	protected String handleGetValorVazio()
	{
		return Short.toString(Short.MAX_VALUE);
	}
	
	protected boolean handleValidateLiterals()
	{
		if (getAllGeneralizations().isEmpty())
		{
			if (!validatingLiterals(this,new java.util.ArrayList()))
			{
				return false;
			}
		}

		return true;
	}
	
	protected boolean validatingLiterals(EnumerationFacadeLogic node, java.util.ArrayList values)
	{
		java.util.ArrayList newValues = new java.util.ArrayList();
		
		newValues.addAll(values);
		
		for (java.util.Iterator iterator = node.getLiterals().iterator(); iterator.hasNext();)
		{
			final AttributeFacade item = (AttributeFacade) iterator.next();
			
			if (newValues.contains(item.getEnumerationValue())) 
			{
				return false;
			}
			
			newValues.add(item.getEnumerationValue());
		}
		
		for (java.util.Iterator iterator = node.getAllSpecializations().iterator(); iterator.hasNext();)
		{
			final EnumerationFacadeLogic item = (EnumerationFacadeLogic) iterator.next();
			
			if (!validatingLiterals(item, newValues))
			{
				return false;
			}
			
		}
		return true;
	}
}