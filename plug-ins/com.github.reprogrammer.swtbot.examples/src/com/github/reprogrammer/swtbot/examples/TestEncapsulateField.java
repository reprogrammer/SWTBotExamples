/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.github.reprogrammer.swtbot.examples;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;

/**
 * @author Mohsen Vakilian
 * 
 */
public class TestEncapsulateField extends SWTBotTest {

	@Override
	protected void doExecuteRefactoring() {
		bot.selectFromPackageExplorer(PROJECT_NAME, "src", PACKAGE_NAME, getTestFileName(), getTestName(), "field");
		bot.invokeRefactoringFromMenu("Encapsulate Field...");
		bot.getBot().radio("keep field reference").click();
		bot.clickButtons(IDialogConstants.OK_LABEL);
		bot.getBot().waitUntil(Conditions.shellCloses(bot.getBot().shell("Encapsulate Field")));
		assertEquals(getTestOuputContents(), bot.getEditorContents(getTestName()));
	}

}
