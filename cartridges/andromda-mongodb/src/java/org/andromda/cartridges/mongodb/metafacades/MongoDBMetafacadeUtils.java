package org.andromda.cartridges.mongodb.metafacades;

import java.util.Collection;

import org.andromda.cartridges.mongodb.MongoDBProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * Contains utilities for use with MongoDB metafacades.
 *
 * @author Chad Brandon
 */
class MongoDBMetafacadeUtils
{

    /**
     * Creates a fully qualified name from the given <code>packageName</code>,
     * <code>name</code>, and <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name the name of the model element.
     * @param suffix the suffix to append.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(
        String packageName,
        String name,
        String suffix)
    {
        StringBuffer fullyQualifiedName = new StringBuffer(StringUtils.trimToEmpty(packageName));
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append('.');
        }
        fullyQualifiedName.append(StringUtils.trimToEmpty(name));
        fullyQualifiedName.append(StringUtils.trimToEmpty(suffix));
        return fullyQualifiedName.toString();
    }

    /**
     * filters all static operations
     */
    static java.util.Collection filterBusinessOperations(Collection operations)
    {
        Collection businessOperations =
            new FilteredCollection(operations)
            {
                public boolean evaluate(Object object)
                {
                    return !((OperationFacade)object).isStatic();
                }
            };
        return businessOperations;
    }
}