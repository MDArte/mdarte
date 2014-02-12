package org.andromda.cartridges.mongodb;

import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;


import org.andromda.cartridges.mongodb.metafacades.MongoDBGlobals;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;


/**
 * Contains utilities used within the MongoDB cartridge.
 *
 * @author Chad Brandon
 */
public class MongoDBUtils
{
    /**
     * Retrieves all roles from the given <code>services</code> collection.
     *
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection getAllRoles(Collection services)
    {
        final Collection allRoles = new HashSet();
        CollectionUtils.forAllDo(
            services,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object != null && Service.class.isAssignableFrom(object.getClass()))
                    {
                        allRoles.addAll(((Service)object).getAllRoles());
                    }
                }
            });
        return allRoles;
    }

    /**
     * Stores the version of MongoDB we're generating for.
     */
    private String mongodbVersion;

    /**
     * Sets the version of MongoDB we're generating for.
     *
     * @param mongodbVersion The version to set.
     */
    public void setMongoDBVersion(final String mongodbVersion)
    {
        this.mongodbVersion = mongodbVersion;
    }
    
    public static Collection getMetafacadesByStereotype(String stereotype){
		MetafacadeFactory factory = MetafacadeFactory.getInstance();
		return factory.getMetafacadesByStereotype(stereotype);
	}

	public static String moreSpecificPackageName(String pacote){
		StringTokenizer st = new StringTokenizer(pacote, ".");
		StringBuffer sb = new StringBuffer();
		String[] tokens = new String[st.countTokens()];
		
		int position = st.countTokens() - 1;
		
		while(st.hasMoreTokens()){
			tokens[position--] = st.nextToken();
		}
		
		for(int i = 0;i < tokens.length;i++){
			String point = "";
			if(i != tokens.length -1) point = ".";
			sb.append(tokens[i] + point);
		}
		return  sb.toString();
	}
	
	public static Collection getEntity(){
		return getMetafacadesByStereotype(MongoDBProfile.STEREOTYPE_ENTITY);
	}
	
	public static Collection getDocument(){
		return getMetafacadesByStereotype(MongoDBProfile.STEREOTYPE_DOCUMENT);
	}
}