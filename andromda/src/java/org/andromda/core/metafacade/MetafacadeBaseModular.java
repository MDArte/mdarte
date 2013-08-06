package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Base class for all metafacades.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class MetafacadeBaseModular extends MetafacadeBase{
	
	public MetafacadeBaseModular(final Object metaObject, final String context){
		super(metaObject, context);
	}
	
	public String insertModuleNameIntoOutlet(String outlet){
		return outlet;
	}
}