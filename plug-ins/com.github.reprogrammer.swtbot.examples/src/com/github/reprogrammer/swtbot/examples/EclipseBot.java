/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.github.reprogrammer.swtbot.examples;

import static org.junit.Assert.assertTrue;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * @author Mohsen Vakilian
 * 
 */
public class EclipseBot {

	private SWTWorkbenchBot bot= new SWTWorkbenchBot();

	private static final int SLEEPTIME= 3000;

	public void dismissWelcomeScreenIfPresent() {
		try {
			bot.viewByTitle("Welcome").close();
		} catch (WidgetNotFoundException exception) {
			// The welcome screen might not be shown so just ignore
		}
	}

	public void createNewJavaProject(String projectName) {
		bot.menu("File").menu("New").menu("Project...").click();

		final SWTBotShell shell= activateShellWithName("New Project");

		getCurrentTree().expandNode("Java").select("Java Project");
		bot.button(IDialogConstants.NEXT_LABEL).click();

		bot.textWithLabel("Project name:").setText(projectName);

		bot.button(IDialogConstants.FINISH_LABEL).click();

		bot.waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				return Conditions.shellCloses(shell).test() || bot.shell("Open Associated Perspective?").isVisible();
			}

			@Override
			public String getFailureMessage() {
				return "Failed to close the new project wizard.";
			}
		});
		dismissJavaPerspectiveIfPresent();
	}

	public void createNewJavaClass(String projectName, String packageName, String className) {
		selectJavaProject(projectName);

		bot.menu("File").menu("New").menu("Class").click();

		activateShellWithName("New Java Class");

		bot.textWithLabel("Package:").setText(packageName);
		bot.textWithLabel("Name:").setText(className);

		bot.button(IDialogConstants.FINISH_LABEL).click();
	}

	public SWTBotTree selectJavaProject(String projectName) {
		SWTBotView packageExplorerView= bot.viewByTitle("Package Explorer");
		packageExplorerView.show();

		Composite packageExplorerComposite= (Composite)packageExplorerView.getWidget();

		Tree swtTree= (Tree)bot.widget(WidgetMatcherFactory.widgetOfType(Tree.class), packageExplorerComposite);
		SWTBotTree tree= new SWTBotTree(swtTree);

		return tree.select(projectName);
	}

	public void prepareJavaTextInEditor(String testName) {
		String contents= SWTBotTest.getTestInputContents(testName);
		SWTBotEclipseEditor editor= getTextEditor(SWTBotTest.getTestFileName(testName));
		editor.setText(contents);
		editor.save();
	}

	public String getEditorContents(String testName) {
		String testFileName= SWTBotTest.getTestFileName(testName);
		return getTextEditor(testFileName).getText();
	}

	public void deleteProject(String projectName) {
		selectJavaProject(projectName).contextMenu("Delete").click();
		SWTBotShell shell= activateShellWithName("Delete Resources");
		if (!bot.checkBox().isChecked()) {
			bot.checkBox().click();
		}
		bot.button(IDialogConstants.OK_LABEL).click();
		bot.waitUntil(Conditions.shellCloses(shell));
	}

	public SWTBotShell activateShellWithName(String text) {
		SWTBotShell shell= bot.shell(text);
		shell.activate();
		return shell;
	}

	/**
	 * Selects the element at the given path from the package explorer view.
	 */
	public void selectFromPackageExplorer(String projectName, String... pathElements) {
		SWTBotTree tree= selectJavaProject(projectName);
		SWTBotTreeItem treeItem= tree.getTreeItem(projectName).expand();

		for (int i= 0; i < pathElements.length - 1; i++) {
			treeItem= treeItem.expandNode(pathElements[i]);
		}

		if (pathElements.length > 0) {
			treeItem.select(pathElements[pathElements.length - 1]);
		}
	}

	public void invokeRefactoringFromMenu(String refactoringMenuItemName) {
		SWTBotMenu refactorMenu= bot.menu("Refactor");
		assertTrue(refactorMenu.isEnabled());

		SWTBotMenu refactoringMenuItem= refactorMenu.menu(refactoringMenuItemName);
		assertTrue(refactoringMenuItem.isEnabled());

		refactoringMenuItem.click();
	}

	public void clickButtons(String... buttonNames) {
		for (String buttonName : buttonNames) {
			bot.button(buttonName).click();
		}
	}

	public SWTWorkbenchBot getBot() {
		return bot;
	}

	public SWTBotTree getCurrentTree() {
		return bot.tree();
	}

	public SWTBotEclipseEditor getTextEditor(String editorTitle) {
		return bot.editorByTitle(editorTitle).toTextEditor();
	}

	public void sleep() {
		bot.sleep(SLEEPTIME);
	}

	private void dismissJavaPerspectiveIfPresent() {
		try {
			bot.button(IDialogConstants.YES_LABEL).click();
		} catch (WidgetNotFoundException exception) {
			// The second and subsequent time this is invoked the Java perspective change dialog will not be shown.
		}
	}

}
