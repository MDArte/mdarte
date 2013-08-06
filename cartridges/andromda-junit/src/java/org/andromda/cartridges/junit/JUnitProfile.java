package org.andromda.cartridges.junit;

import org.andromda.core.profile.Profile;

public class JUnitProfile
{
	/**
	 * The Profile instance from which we retrieve the mapped profile names.
	 */
	private static final Profile profile = Profile.instance();

	// Stereotypes
	public static final String STEREOTYPE_TEST_MODULE = profile.get("TEST_MODULE");
	public static final String STEREOTYPE_TEST_PROCESS = profile.get("TEST_PROCESS");

	// Tagged Values
	public static final String TAGGEDVALUE_IGNORE = profile.get("IGNORE");
	public static final String TAGGEDVALUE_PASSWORD = profile.get("PASSWORD");
	public static final String TAGGEDVALUE_PROJECT_NAME = profile.get("PROJECT_NAME");
	public static final String TAGGEDVALUE_SERVICE_NAME = profile.get("SERVICE_NAME");
	public static final String TAGGEDVALUE_SERVICE_PACKAGE= profile.get("SERVICE_PACKAGE");
	public static final String TAGGEDVALUE_USE_CASE_NAME = profile.get("USE_CASE_NAME");
	public static final String TAGGEDVALUE_USE_CASE_PACKAGE= profile.get("USE_CASE_PACKAGE");
}
