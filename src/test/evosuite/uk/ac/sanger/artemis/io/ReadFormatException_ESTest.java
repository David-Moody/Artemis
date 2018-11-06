/*
 * This file was automatically generated by EvoSuite
 * Thu Sep 20 14:16:04 GMT 2018
 */

package uk.ac.sanger.artemis.io;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import uk.ac.sanger.artemis.io.ReadFormatException;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, useJEE = true) 
public class ReadFormatException_ESTest extends ReadFormatException_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      ReadFormatException readFormatException0 = new ReadFormatException((String) null, 199);
      int int0 = readFormatException0.getLineNumber();
      assertEquals(199, int0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      ReadFormatException readFormatException0 = new ReadFormatException("V=g0YN", (-1059));
      int int0 = readFormatException0.getLineNumber();
      assertEquals((-1059), int0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      ReadFormatException readFormatException0 = new ReadFormatException("@#A");
      int int0 = readFormatException0.getLineNumber();
      assertEquals(0, int0);
  }
}