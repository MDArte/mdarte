package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecAssociationEndFacade.
 *
 * @see org.andromda.metafacades.uml.CoppetecAssociationEndFacade
 */
public class CoppetecAssociationEndFacadeLogicImpl
    extends CoppetecAssociationEndFacadeLogic
{

    public CoppetecAssociationEndFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

	protected String handleGetNameWithoutPluralization() {
		String name = ((org.omg.uml.foundation.core.ModelElement)metaObject).getName();

        // if name is empty, then get the name from the type
        if (StringUtils.isEmpty(name))
        {
            final ClassifierFacade type = this.getType();
            if (type != null)
            {
                name = StringUtils.uncapitalize(StringUtils.trimToEmpty(type.getName()));
            }
        }
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_PROPERTY_NAME_MASK));
        return NameMasker.mask(name, nameMask);
	}
}