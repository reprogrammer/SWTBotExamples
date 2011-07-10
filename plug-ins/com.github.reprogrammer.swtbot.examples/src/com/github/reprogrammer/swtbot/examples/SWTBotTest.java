/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.github.reprogrammer.swtbot.examples;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.utils.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * @author Mohsen Vakilian
 * 
 */
public abstract class SWTBotTest {

	protected static EclipseBot bot;

	protected final static String PROJECT_NAME= "Prj";

	protected final static String PACKAGE_NAME= "com.github.reprogrammer";

	abstract protected void doExecuteRefactoring();

	protected String getTestName() {
		return getClass().getSimpleName();
	}

	protected String getTestFileName() {
		return getTestFileName(getTestName());
	}

	public static String getTestFileName(String testName) {
		return String.format("%s.java", testName);
	}

	private static String getPluginFileContents(String filePath) {
		Bundle bundle= Platform.getBundle(Activator.PLUGIN_ID);
		return FileUtils.read(bundle.getEntry(filePath));
	}

	public static String getTestInputContents(String testName) {
		String testFileName= SWTBotTest.getTestFileName(testName);
		return getPluginFileContents(String.format("test-files/input/%s", testFileName));
	}

	public static String getTestOutputContents(String testName) {
		String testFileName= SWTBotTest.getTestFileName(testName);
		return getPluginFileContents(String.format("test-files/output/%s", testFileName));
	}

	public String getTestOuputContents() {
		return getTestOutputContents(getTestName());
	}

	@BeforeClass
	public static void beforeClass() {
		bot= new EclipseBot();
		bot.dismissWelcomeScreenIfPresent();
	}

	@Test
	public final void setupProject() {
		bot.createNewJavaProject(PROJECT_NAME);
		bot.createNewJavaClass(PROJECT_NAME, PACKAGE_NAME, getTestName());
		bot.prepareJavaTextInEditor(getTestName());
	}

	@Test
	public final void shouldExecuteRefactoring() throws Exception {
		doExecuteRefactoring();
	}

	@Test
	public final void shouldCleanUpWorkspace() {
		bot.deleteProject(PROJECT_NAME);
	}

}
