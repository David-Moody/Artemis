/*
 * This file was automatically generated by EvoSuite
 * Thu Sep 20 12:47:41 GMT 2018
 */

package uk.ac.sanger.artemis.components.genebuilder;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;
import uk.ac.sanger.artemis.components.QualifierTextArea;
import uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel;
import uk.ac.sanger.artemis.components.genebuilder.OpenSectionButton;
import uk.ac.sanger.artemis.components.genebuilder.ReferencesPanel;
import uk.ac.sanger.artemis.components.genebuilder.cv.CVPanel;
import uk.ac.sanger.artemis.components.genebuilder.gff.PropertiesPanel;
import uk.ac.sanger.artemis.components.genebuilder.ortholog.MatchPanel;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, useJEE = true) 
public class GeneEditorPanel_ESTest extends GeneEditorPanel_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      JMenu jMenu0 = new JMenu();
      // Undeclared exception!
      try { 
        GeneEditorPanel.addOpenClosePanel("w;=[/!Kypn$d ]lE", jMenu0, (JPanel) null, "ZvF");
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel", e);
      }
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      JLayer<JLayeredPane> jLayer0 = new JLayer<JLayeredPane>();
      // Undeclared exception!
      try { 
        GeneEditorPanel.getSeparator(jLayer0, false);
        fail("Expecting exception: UnsupportedOperationException");
      
      } catch(UnsupportedOperationException e) {
         //
         // Adding components to JLayer is not supported, use setView() or setGlassPane() instead
         //
         verifyException("javax.swing.JLayer", e);
      }
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      // Undeclared exception!
      try { 
        GeneEditorPanel.addLightSeparator((JComponent) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel", e);
      }
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      JCheckBox jCheckBox0 = new JCheckBox();
      // Undeclared exception!
      try { 
        GeneEditorPanel.addLightSeparator(jCheckBox0);
        fail("Expecting exception: IncompatibleClassChangeError");
      
      } catch(IncompatibleClassChangeError e) {
         //
         // Expected static method org.evosuite.runtime.mock.javax.swing.MockJComponent.getPreferredSize()Ljava/awt/Dimension;
         //
         verifyException("uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel", e);
      }
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      JComboBox<Object> jComboBox0 = new JComboBox<Object>();
      // Undeclared exception!
      try { 
        GeneEditorPanel.addDarkSeparator(jComboBox0);
        fail("Expecting exception: IncompatibleClassChangeError");
      
      } catch(IncompatibleClassChangeError e) {
         //
         // Expected static method org.evosuite.runtime.mock.javax.swing.MockJComponent.getPreferredSize()Ljava/awt/Dimension;
         //
         verifyException("uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel", e);
      }
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      // Undeclared exception!
      try { 
        GeneEditorPanel.getSeparator((JComponent) null, false);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel", e);
      }
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      JProgressBar jProgressBar0 = new JProgressBar();
      // Undeclared exception!
      try { 
        GeneEditorPanel.getSeparator(jProgressBar0, true);
        fail("Expecting exception: IncompatibleClassChangeError");
      
      } catch(IncompatibleClassChangeError e) {
         //
         // Expected static method org.evosuite.runtime.mock.javax.swing.MockJComponent.getPreferredSize()Ljava/awt/Dimension;
         //
         verifyException("uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel", e);
      }
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      JSpinner jSpinner0 = new JSpinner();
      JSpinner.NumberEditor jSpinner_NumberEditor0 = new JSpinner.NumberEditor(jSpinner0);
      OpenSectionButton openSectionButton0 = GeneEditorPanel.addOpenClosePanel("Cre", jSpinner0, jSpinner_NumberEditor0, "Cre");
      assertTrue(openSectionButton0.getFocusTraversalKeysEnabled());
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      JPanel jPanel0 = new JPanel();
      OpenSectionButton openSectionButton0 = GeneEditorPanel.addOpenClosePanel((String) null, jPanel0, jPanel0, (String) null);
      assertTrue(openSectionButton0.getFocusTraversalKeysEnabled());
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      // Undeclared exception!
      try { 
        GeneEditorPanel.addDarkSeparator((JComponent) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("uk.ac.sanger.artemis.components.genebuilder.GeneEditorPanel", e);
      }
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      JLayer<JLayeredPane> jLayer0 = new JLayer<JLayeredPane>();
      // Undeclared exception!
      try { 
        GeneEditorPanel.addLightSeparator(jLayer0);
        fail("Expecting exception: UnsupportedOperationException");
      
      } catch(UnsupportedOperationException e) {
         //
         // Adding components to JLayer is not supported, use setView() or setGlassPane() instead
         //
         verifyException("javax.swing.JLayer", e);
      }
  }
}