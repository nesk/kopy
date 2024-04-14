

package com.javiersc.kotlin.kopy.compiler;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link com.javiersc.kotlin.kopy.compiler.GenerateKotlinCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("test-data/box")
@TestDataPath("$PROJECT_ROOT")
public class BoxTestGenerated extends AbstractBoxTest {
  @Test
  public void testAllFilesPresentInBox() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/box"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
  }

  @Test
  @TestMetadata("nest-copy-set.kt")
  public void testNest_copy_set() {
    runTest("test-data/box/nest-copy-set.kt");
  }

  @Test
  @TestMetadata("nest-copy-update.kt")
  public void testNest_copy_update() {
    runTest("test-data/box/nest-copy-update.kt");
  }

  @Test
  @TestMetadata("no-nest-copy-set.kt")
  public void testNo_nest_copy_set() {
    runTest("test-data/box/no-nest-copy-set.kt");
  }

  @Test
  @TestMetadata("no-nest-copy-update-each-no-it.kt")
  public void testNo_nest_copy_update_each_no_it() {
    runTest("test-data/box/no-nest-copy-update-each-no-it.kt");
  }

  @Test
  @TestMetadata("no-nest-copy-update-no-it.kt")
  public void testNo_nest_copy_update_no_it() {
    runTest("test-data/box/no-nest-copy-update-no-it.kt");
  }

  @Test
  @TestMetadata("no-nest-copy-update-with-it.kt")
  public void testNo_nest_copy_update_with_it() {
    runTest("test-data/box/no-nest-copy-update-with-it.kt");
  }

  @Nested
  @TestMetadata("test-data/box/edge")
  @TestDataPath("$PROJECT_ROOT")
  public class Edge {
    @Test
    public void testAllFilesPresentInEdge() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/box/edge"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("simple-1.kt")
    public void testSimple_1() {
      runTest("test-data/box/edge/simple-1.kt");
    }
  }

  @Nested
  @TestMetadata("test-data/box/nested-copy")
  @TestDataPath("$PROJECT_ROOT")
  public class Nested_copy {
    @Test
    public void testAllFilesPresentInNested_copy() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/box/nested-copy"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("simple-1.kt")
    public void testSimple_1() {
      runTest("test-data/box/nested-copy/simple-1.kt");
    }
  }

  @Nested
  @TestMetadata("test-data/box/repeated-properties")
  @TestDataPath("$PROJECT_ROOT")
  public class Repeated_properties {
    @Test
    public void testAllFilesPresentInRepeated_properties() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/box/repeated-properties"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("complex-1.kt")
    public void testComplex_1() {
      runTest("test-data/box/repeated-properties/complex-1.kt");
    }

    @Test
    @TestMetadata("complex-2.kt")
    public void testComplex_2() {
      runTest("test-data/box/repeated-properties/complex-2.kt");
    }
  }
}
