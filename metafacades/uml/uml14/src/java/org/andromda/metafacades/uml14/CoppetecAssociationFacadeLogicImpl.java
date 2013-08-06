package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.CoppetecAssociationEndFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;



/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecAssociationFacade.
 *
 * @see org.andromda.metafacades.uml.CoppetecAssociationFacade
 */
public class CoppetecAssociationFacadeLogicImpl
    extends CoppetecAssociationFacadeLogic
{

    public CoppetecAssociationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CoppetecAssociationFacade#isWebServiceFormat()
     */
    protected boolean handleIsWebServiceFormat()
    {
        List ends = getAssociationEnds();
        AssociationEndFacade end1 = (AssociationEndFacade)ends.get(0);
        AssociationEndFacade end2 = (AssociationEndFacade)ends.get(1);
        
        boolean valido = true;
        if(!end1.getType().getName().equals(end2.getType().getName())){
        	if(end1.getType().hasStereotype("WebServiceData") && end2.getType().hasStereotype("WebServiceData")){
        		if(!((end1.hasStereotype("ExcludesWSD")) || (end2.hasStereotype("ExcludesWSD")))){
        			valido = false;
        		}
        	}
        }
        return valido;
    }

    protected String handleGetColumnUniqueGroup() {
		final String group = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_UNIQUE_GROUP);
	    return group != null ? StringUtils.trimToEmpty(group) : null;
	}
    
    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getRelationName()
     */
    public String handleGetRelationName()
    {
        final Collection ends = this.getAssociationEnds();
        final Iterator endIt = ends.iterator();
        final CoppetecAssociationEndFacade firstEnd = (CoppetecAssociationEndFacade)endIt.next();
        final CoppetecAssociationEndFacade secondEnd = (CoppetecAssociationEndFacade)endIt.next();
        
        final String relationName =
            MetafacadeUtils.toRelationName(
                firstEnd.getNameWithoutPluralization(),
                secondEnd.getNameWithoutPluralization(),
                String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR)));
        return relationName;
    }
}