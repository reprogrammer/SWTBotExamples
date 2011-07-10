/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.github.reprogrammer.swtbot.examples;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * @author Mohsen Vakilian
 * 
 */
public class TestInlineLocalVariableViaQuickAssist extends SWTBotTest {

	@Override
	protected void doExecuteRefactoring() {
		bot.selectElementToRefactor(getTestFileName(), 8, 15, "localVariable".length());
		SWTWorkbenchBot swtBot= bot.getBot();
		swtBot.activeEditor().toTextEditor().quickfix("Inline local variable");
//		final SWTBotEclipseEditor editor= bot.getTextEditor(getTestFileName());
//		bot.getBot().waitUntil(new DefaultCondition() {
//
//			@Override
//			public boolean test() {
//				return editor.isActive();
//			}
//
//			@Override
//			public String getFailureMessage() {
//				return "Failed to activate the Java editor.";
//			}
//		});
//		assertEquals(getTestOuputContents(), bot.getEditorContents(getTestName()));
	}

}
