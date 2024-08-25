

package com.javiersc.kotlin.kopy.compiler;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link com.javiersc.kotlin.kopy.compiler.GenerateKotlinCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("test-data/diagnostics-kopy-functions/copy")
@TestDataPath("$PROJECT_ROOT")
public class KopyCopyDiagnosticTestGenerated extends AbstractKopyCopyDiagnosticTest {
  @Test
  public void testAllFilesPresentInCopy() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/diagnostics-kopy-functions/copy"), Pattern.compile("^(.+)\\.kt$"), null, true);
  }

  @Test
  @TestMetadata("copy.kt")
  public void testCopy() {
    runTest("test-data/diagnostics-kopy-functions/copy/copy.kt");
  }
}
