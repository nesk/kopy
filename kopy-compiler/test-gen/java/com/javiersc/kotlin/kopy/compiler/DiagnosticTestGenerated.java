

package com.javiersc.kotlin.kopy.compiler;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link com.javiersc.kotlin.kopy.compiler.GenerateKotlinCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("test-data/diagnostics")
@TestDataPath("$PROJECT_ROOT")
public class DiagnosticTestGenerated extends AbstractDiagnosticTest {
  @Test
  public void testAllFilesPresentInDiagnostics() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/diagnostics"), Pattern.compile("^(.+)\\.kt$"), null, true);
  }

  @Nested
  @TestMetadata("test-data/diagnostics/invalid-call-chain")
  @TestDataPath("$PROJECT_ROOT")
  public class Invalid_call_chain {
    @Test
    public void testAllFilesPresentInInvalid_call_chain() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/diagnostics/invalid-call-chain"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("simple-deep-invalid-call-chain-deep-set.kt")
    public void testSimple_deep_invalid_call_chain_deep_set() {
      runTest("test-data/diagnostics/invalid-call-chain/simple-deep-invalid-call-chain-deep-set.kt");
    }

    @Test
    @TestMetadata("simple-deep-invalid-call-chain-deep-update.kt")
    public void testSimple_deep_invalid_call_chain_deep_update() {
      runTest("test-data/diagnostics/invalid-call-chain/simple-deep-invalid-call-chain-deep-update.kt");
    }

    @Test
    @TestMetadata("simple-deep-invalid-call-chain-deep-update-each.kt")
    public void testSimple_deep_invalid_call_chain_deep_update_each() {
      runTest("test-data/diagnostics/invalid-call-chain/simple-deep-invalid-call-chain-deep-update-each.kt");
    }
  }

  @Nested
  @TestMetadata("test-data/diagnostics/missing-data-class")
  @TestDataPath("$PROJECT_ROOT")
  public class Missing_data_class {
    @Test
    public void testAllFilesPresentInMissing_data_class() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/diagnostics/missing-data-class"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("multiple-scenarios-in-a-row.kt")
    public void testMultiple_scenarios_in_a_row() {
      runTest("test-data/diagnostics/missing-data-class/multiple-scenarios-in-a-row.kt");
    }

    @Test
    @TestMetadata("simple-1-level-deep-missing-and-deep-set.kt")
    public void testSimple_1_level_deep_missing_and_deep_set() {
      runTest("test-data/diagnostics/missing-data-class/simple-1-level-deep-missing-and-deep-set.kt");
    }

    @Test
    @TestMetadata("simple-1-level-deep-missing-and-deep-set-and-valid-set.kt")
    public void testSimple_1_level_deep_missing_and_deep_set_and_valid_set() {
      runTest("test-data/diagnostics/missing-data-class/simple-1-level-deep-missing-and-deep-set-and-valid-set.kt");
    }

    @Test
    @TestMetadata("simple-1-level-deep-missing-and-deep-update.kt")
    public void testSimple_1_level_deep_missing_and_deep_update() {
      runTest("test-data/diagnostics/missing-data-class/simple-1-level-deep-missing-and-deep-update.kt");
    }

    @Test
    @TestMetadata("simple-1-level-deep-set.kt")
    public void testSimple_1_level_deep_set() {
      runTest("test-data/diagnostics/missing-data-class/simple-1-level-deep-set.kt");
    }

    @Test
    @TestMetadata("simple-1-level-deep-update.kt")
    public void testSimple_1_level_deep_update() {
      runTest("test-data/diagnostics/missing-data-class/simple-1-level-deep-update.kt");
    }

    @Test
    @TestMetadata("simple-1-level-deep-update-each.kt")
    public void testSimple_1_level_deep_update_each() {
      runTest("test-data/diagnostics/missing-data-class/simple-1-level-deep-update-each.kt");
    }

    @Test
    @TestMetadata("simple-deep-set.kt")
    public void testSimple_deep_set() {
      runTest("test-data/diagnostics/missing-data-class/simple-deep-set.kt");
    }

    @Test
    @TestMetadata("simple-deep-update.kt")
    public void testSimple_deep_update() {
      runTest("test-data/diagnostics/missing-data-class/simple-deep-update.kt");
    }

    @Test
    @TestMetadata("simple-multiple-deep-set.kt")
    public void testSimple_multiple_deep_set() {
      runTest("test-data/diagnostics/missing-data-class/simple-multiple-deep-set.kt");
    }

    @Test
    @TestMetadata("simple-multiple-deep-update.kt")
    public void testSimple_multiple_deep_update() {
      runTest("test-data/diagnostics/missing-data-class/simple-multiple-deep-update.kt");
    }

    @Test
    @TestMetadata("simple-multiple-deep-update-each.kt")
    public void testSimple_multiple_deep_update_each() {
      runTest("test-data/diagnostics/missing-data-class/simple-multiple-deep-update-each.kt");
    }
  }

  @Nested
  @TestMetadata("test-data/diagnostics/no-copy-scope")
  @TestDataPath("$PROJECT_ROOT")
  public class No_copy_scope {
    @Test
    public void testAllFilesPresentInNo_copy_scope() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/diagnostics/no-copy-scope"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("simple-no-copy-scope-deep-set.kt")
    public void testSimple_no_copy_scope_deep_set() {
      runTest("test-data/diagnostics/no-copy-scope/simple-no-copy-scope-deep-set.kt");
    }

    @Test
    @TestMetadata("simple-no-copy-scope-deep-update.kt")
    public void testSimple_no_copy_scope_deep_update() {
      runTest("test-data/diagnostics/no-copy-scope/simple-no-copy-scope-deep-update.kt");
    }

    @Test
    @TestMetadata("simple-no-copy-scope-deep-update-each.kt")
    public void testSimple_no_copy_scope_deep_update_each() {
      runTest("test-data/diagnostics/no-copy-scope/simple-no-copy-scope-deep-update-each.kt");
    }

    @Test
    @TestMetadata("simple-no-immediate-copy-scope-deep-set.kt")
    public void testSimple_no_immediate_copy_scope_deep_set() {
      runTest("test-data/diagnostics/no-copy-scope/simple-no-immediate-copy-scope-deep-set.kt");
    }

    @Test
    @TestMetadata("simple-no-immediate-copy-scope-deep-update.kt")
    public void testSimple_no_immediate_copy_scope_deep_update() {
      runTest("test-data/diagnostics/no-copy-scope/simple-no-immediate-copy-scope-deep-update.kt");
    }

    @Test
    @TestMetadata("simple-no-immediate-copy-scope-deep-update-each.kt")
    public void testSimple_no_immediate_copy_scope_deep_update_each() {
      runTest("test-data/diagnostics/no-copy-scope/simple-no-immediate-copy-scope-deep-update-each.kt");
    }
  }

  @Nested
  @TestMetadata("test-data/diagnostics/non-data-class")
  @TestDataPath("$PROJECT_ROOT")
  public class Non_data_class {
    @Test
    public void testAllFilesPresentInNon_data_class() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/diagnostics/non-data-class"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() {
      runTest("test-data/diagnostics/non-data-class/simple.kt");
    }
  }

  @Nested
  @TestMetadata("test-data/diagnostics/valid")
  @TestDataPath("$PROJECT_ROOT")
  public class Valid {
    @Test
    public void testAllFilesPresentInValid() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("test-data/diagnostics/valid"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("multiple-scenarios-in-a-row.kt")
    public void testMultiple_scenarios_in_a_row() {
      runTest("test-data/diagnostics/valid/multiple-scenarios-in-a-row.kt");
    }

    @Test
    @TestMetadata("simple-copy.kt")
    public void testSimple_copy() {
      runTest("test-data/diagnostics/valid/simple-copy.kt");
    }

    @Test
    @TestMetadata("simple-copy-update-custom-parameter.kt")
    public void testSimple_copy_update_custom_parameter() {
      runTest("test-data/diagnostics/valid/simple-copy-update-custom-parameter.kt");
    }

    @Test
    @TestMetadata("simple-copy-update-each-custom-parameter.kt")
    public void testSimple_copy_update_each_custom_parameter() {
      runTest("test-data/diagnostics/valid/simple-copy-update-each-custom-parameter.kt");
    }

    @Test
    @TestMetadata("simple-deep-copy.kt")
    public void testSimple_deep_copy() {
      runTest("test-data/diagnostics/valid/simple-deep-copy.kt");
    }

    @Test
    @TestMetadata("simple-deep-copy-with-multiple-scenarios.kt")
    public void testSimple_deep_copy_with_multiple_scenarios() {
      runTest("test-data/diagnostics/valid/simple-deep-copy-with-multiple-scenarios.kt");
    }

    @Test
    @TestMetadata("simple-nested-copy.kt")
    public void testSimple_nested_copy() {
      runTest("test-data/diagnostics/valid/simple-nested-copy.kt");
    }

    @Test
    @TestMetadata("simple-nested-copy-assign.kt")
    public void testSimple_nested_copy_assign() {
      runTest("test-data/diagnostics/valid/simple-nested-copy-assign.kt");
    }

    @Test
    @TestMetadata("simple-no-nest-copy-deep-set.kt")
    public void testSimple_no_nest_copy_deep_set() {
      runTest("test-data/diagnostics/valid/simple-no-nest-copy-deep-set.kt");
    }
  }
}
